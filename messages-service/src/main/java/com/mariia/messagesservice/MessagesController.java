package com.mariia.messagesservice;

import com.mariia.messagesservice.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messagesService.getAllMessages();
    }
}
