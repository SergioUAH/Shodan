package com.uah.shodan_tfg.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uah.shodan_tfg.core.services.IConnectionService;
import com.uah.shodan_tfg.core.services.IFileHelper;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;

@Service
public class ConnectionService implements IConnectionService {

    @Autowired
    IFileHelper fileService;

    private HttpClient createHttpClient() {
	HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).followRedirects(Redirect.NORMAL)
		.connectTimeout(Duration.ofSeconds(20))// .authenticator(Authenticator.getDefault())
		.build();
	return client;
    }

    @Override
    public String makeHttpRequest(String ip, Integer port) {
	String body = null;
	HttpClient client = createHttpClient();
	HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://" + ip + ":" + port))
		.setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
		.build();
	try {
	    // HttpResponse<String> response = client.send(request,
	    // HttpResponse.BodyHandlers.ofString());

	    var future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
	    body = future.thenApply(HttpResponse::body).get();
	    System.out.println(body);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}
	return body;
    }

    private void testSecurity(Integer id, String ip, Integer port) {
	try {
	    Socket clientSocket = new Socket(ip, port);
	    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    sendMessage("Test 1", in, out);
	    stopConnection(in, out, clientSocket);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private String sendMessage(String msg, BufferedReader in, PrintWriter out) {
	out.println(msg);
	String resp = "";
	try {
	    resp = in.readLine();
	    System.out.println(resp);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return resp;
    }

    private void stopConnection(BufferedReader in, PrintWriter out, Socket clientSocket) {
	try {
	    in.close();
	    out.close();
	    clientSocket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String connectToFtpServer(String ip, Integer port) {
	String result = "";
	List<String> userPassList = fileService.fileToList("src/main/resources/ftp-betterdefaultpasslist.txt");
	String username = "";
	String password = "";

	FTPClient ftpClient = new FTPClient();
	try {
	    ftpClient.connect(ip, port);
	    result = ftpClient.getReplyString();
	    System.out.println("FTP Server connection response --> " + result);
	    Integer replyCode = ftpClient.getReplyCode();
	    if (FTPReply.isNegativeTransient(replyCode)) {
		System.out.println("ERROR - FTP Server response --> " + replyCode);
	    } else if (FTPReply.isPositiveCompletion(replyCode)) {
		System.out.println("SUCCESS - FTP ready --> " + replyCode);
		System.out.println("IP--> " + ip);
		Integer counter = 0;
		for (String userPass : userPassList) {
		    if (counter == 3) {
			ftpClient.disconnect();
			ftpClient.connect(ip, port);
			counter = 0;
		    }
		    username = userPass.split(":")[0];
		    password = userPass.split(":")[1];
		    System.out.println("Credentials tested --> User: " + username + " | Password: " + password);
		    try {
			Boolean loggedIn = ftpClient.login(username, password);
			// Store login credentials
			if (loggedIn) {
			    System.out.println(
				    "Correct login credentials --> User: " + username + " | Password: " + password);
			} else {
			    System.out.println("Incorrect login credentials");
			    counter += 1;
			}
		    } catch (IOException e) {
			System.out.println("Incorrect login credentials --> " + e);
			ftpClient.disconnect();
			ftpClient.connect(ip, port);
			continue;
		    }
		}
		result = ftpClient.getReplyString();
		System.out.println("FTP Server login response --> " + result);
		ftpClient.enterLocalPassiveMode();
		FTPFile[] directories = ftpClient.listDirectories();
		System.out.println("SUCCESS - FPT Directories --> " + directories.toString());
		FTPFile[] files = ftpClient.listFiles();
		System.out.println("SUCCESS - FTP Files--> " + files.toString());
	    } else if (FTPReply.isPositiveIntermediate(replyCode)) {
		System.out.println("SUCCESS - May need login --> " + replyCode);
		ftpClient.login(username, password);
		result = ftpClient.getReplyString();
		System.out.println("FTP Server login response --> " + result);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return result;
    }

    @Override
    public String connectThroughSSH(String ip, Integer port) {
	String result = "";
	List<String> userPassList = fileService.fileToList("src/main/resources/ftp-betterdefaultpasslist.txt");
	String username = "";
	String password = "";

	for (String userPass : userPassList) {
	    SSHClient clientSSH = new SSHClient();
	    clientSSH.addHostKeyVerifier(new PromiscuousVerifier());
	    username = userPass.split(":")[0];
	    password = userPass.split(":")[1];
	    try {
		clientSSH.connect(ip);
		System.out.println("Credentials tested --> User: " + username + " | Password: " + password);
		clientSSH.authPassword(username, password);
		// Store login credentials
		if (clientSSH.isConnected() && clientSSH.isAuthenticated()) {
		    System.out.println("Correct login credentials --> User: " + username + " | Password: " + password);
		    SFTPClient clientSFTP = clientSSH.newSFTPClient();
		    clientSFTP.ls("/");
		    clientSSH.disconnect();
		} else {
		    System.out.println("Incorrect login credentials");
		}
	    } catch (UserAuthException uae) {
		System.out.println("Incorrect login credentials --> " + uae);
		try {
		    clientSSH.disconnect();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		continue;
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (IllegalThreadStateException e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

//    public void testSecurity(Integer id, String ip, Integer port) {
//	try {
//	    URL url = new URL(ip + ":" + port);
//	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//	    con.setRequestMethod("GET");
//	    con.setRequestProperty("Content-Type", "application/json");
//	    String contentType = con.getHeaderField("Content-Type");
//	    con.setConnectTimeout(5000);
//	    con.setReadTimeout(5000);
//	    int status = con.getResponseCode();
//	    if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM) {
//		String location = con.getHeaderField("Location");
//		URL newUrl = new URL(location);
//		con = (HttpURLConnection) newUrl.openConnection();
//	    }
//	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//	    String inputLine;
//	    StringBuffer content = new StringBuffer();
//	    while ((inputLine = in.readLine()) != null) {
//		content.append(inputLine);
//	    }
//	    in.close();
//	    con.disconnect();
//
//	    System.out.println("Llamada realizada");
//	} catch (MalformedURLException e2) {
//	    // TODO Auto-generated catch block
//	    e2.printStackTrace();
//	} catch (ProtocolException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	} catch (IOException e1) {
//	    // TODO Auto-generated catch block
//	    e1.printStackTrace();
//	}
//	System.out.println();
//
//    }

}
