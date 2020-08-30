package com.uah.shodan_tfg.entrypoints;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uah.shodan_tfg.entrypoints.dto.LogMessageDTO;
import com.uah.shodan_tfg.entrypoints.dto.LogOutput;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/messageLogging")
@RestController
public class MessageLoggingController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public LogOutput send(LogMessageDTO message) throws Exception {
	String time = "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]";
	return new LogOutput(message.getText(), time);
    }

}
