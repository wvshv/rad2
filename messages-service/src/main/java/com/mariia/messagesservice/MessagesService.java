package com.mariia.messagesservice;

import com.mariia.messagesservice.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagesService {

    private final Set<Message> messagesMap = new ConcurrentSkipListSet<>();

    public void saveMessage(Message message) {
        messagesMap.add(message);
    }

    public List<Message> getAllMessages() {
        return messagesMap.stream().collect(Collectors.toList());
    }
}
