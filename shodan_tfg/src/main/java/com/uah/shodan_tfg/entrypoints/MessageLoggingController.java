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

@CrossOrigin(origins = "ws://localhost:4200")
@RequestMapping("/log")
@RestController
public class MessageLoggingController {

    @MessageMapping("/chat")
    @SendTo("/logger/connectionLog")
    public LogOutput send(LogMessageDTO message) throws Exception {
	String time = "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "]";
	return new LogOutput(message.getText(), time);
    }

}
