package com.mariia.messagesservice.messaging;

import com.mariia.messagesservice.Message;
import com.mariia.messagesservice.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
@Service
public class MessageStreamListener {

    private final Logger log = LoggerFactory.getLogger(MessageStreamListener.class);

    @Bean
    public Consumer<Message> messages() {
        return message -> {
            log.info("Received message: {}", message);
            // Handle the message
        };
    }
}
