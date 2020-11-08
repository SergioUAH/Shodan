package com.uah.shodan_tfg.entrypoints;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.logging.Logger;
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
	private static Logger LOGGER = Logger
			.getLogger(MessageLoggingController.class);

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/logger")
	@SendTo("/topic/connectionLog")
	public LogOutput receiveMessage(@Payload LogMessageDTO message)
			throws Exception {
		LOGGER.info("Received request " + message.getText());
		String time = "["
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
				+ "]";
		message.setText("Socket connected");
		LOGGER.info(message.getText());
		return new LogOutput(message.getText(), time);
	}

	public void send(String text) {
		LogMessageDTO message = new LogMessageDTO();
		String time = "["
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())
				+ "]";
		message.setText(time + "  >>>  " + text);
		LOGGER.info(message.getText());
		LOGGER.info("Sending message --> " + message.getText());
		this.template.convertAndSend("/topic/connectionLog", message);
	}

}
