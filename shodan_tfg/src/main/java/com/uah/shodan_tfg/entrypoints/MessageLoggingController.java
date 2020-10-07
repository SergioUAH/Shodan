package com.uah.shodan_tfg.entrypoints;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.uah.shodan_tfg.entrypoints.dto.LogMessageDTO;
import com.uah.shodan_tfg.entrypoints.dto.LogOutput;

@Controller
public class MessageLoggingController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/logger")
    @SendTo("/topic/connectionLog")
    public LogOutput receiveMessage(@Payload LogMessageDTO message) throws Exception {
	System.out.println("Petición recibida " + message.getText());
	String time = "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]";
	message.setText("Probando logs");
	System.out.println(message.getText());
	return new LogOutput(message.getText(), time);
    }

    public void send(String text) {
	LogMessageDTO message = new LogMessageDTO();
	String time = "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]";
	message.setText(time + "  >>>  " + text);
	System.out.println(message.getText());
	System.out.println("Enviando mensaje --> " + message.getText());
	this.template.convertAndSend("/topic/connectionLog", message);
    }

}
