package com.mariia.logginservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class LoggingController {

    private final HazelcastInstance hzInstance;
    private final IMap<UUID, String> messageMap;
    private final ObjectMapper objectMapper;


    public LoggingController(HazelcastInstance hzInstance,
                             ObjectMapper objectMapper) {
        this.hzInstance = hzInstance;
        this.messageMap = hzInstance.getMap("lab-messages");
        this.objectMapper = objectMapper;
    }

    @PostMapping("/save")
    public void saveMessage(@RequestBody String messageBody) {
        try {
            Message message = objectMapper.readValue(messageBody, Message.class);
            messageMap.put(UUID.fromString(message.uuid), message.message);
            System.out.println("Received message: " + messageBody);
        } catch (Exception e) {
            System.out.println("Unable to deserialize message " + e);
        }
    }

    @GetMapping("/messages")
    public String getAllMessages() {
        List<String> messages = hzInstance.getMap("lab-messages")
                .values()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        return String.join("\n", messages);
    }

    static class Message {
        public String uuid;
        public String message;

        @Override
        public String toString() {
            return uuid + " " + message;
        }
    }
}
