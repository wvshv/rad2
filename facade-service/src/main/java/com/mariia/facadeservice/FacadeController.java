package com.mariia.facadeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariia.facadeservice.api.loggingservice.LoggingServiceConfig;
import com.mariia.facadeservice.api.messagingservice.MessageStreamPublisher;
import com.mariia.facadeservice.api.messagingservice.MessagingServiceConfig;
import com.mariia.messagesservice.messaging.Message; // Імпорт з messages-service
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
public class FacadeController {

    private final Logger log = LoggerFactory.getLogger(FacadeController.class);
    private final ObjectMapper objectMapper;
    private final MessageStreamPublisher messageStreamPublisher;
    private final LoggingServiceConfig loggingServiceConfig;
    private final MessagingServiceConfig messagingServiceConfig;
    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public FacadeController(ObjectMapper objectMapper,
                            MessageStreamPublisher messageStreamPublisher,
                            LoggingServiceConfig loggingServiceConfig,
                            MessagingServiceConfig messagingServiceConfig,
                            RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.messageStreamPublisher = messageStreamPublisher;
        this.loggingServiceConfig = loggingServiceConfig;
        this.messagingServiceConfig = messagingServiceConfig;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/message")
    public String receiveMessage(@RequestBody String messageBody) {
        List<Integer> ports = loggingServiceConfig.getTargetPorts();
        int randomPort = ports.get(random.nextInt(ports.size()));

        String uuid = UUID.randomUUID().toString();
        Message message = new Message(uuid, messageBody);
        restTemplate.postForObject("http://localhost:" + randomPort + "/save", message, Void.class);
        messageStreamPublisher.publishMessage(message);
        return "Received message with ID: " + uuid;
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        log.info("Fetching messages from logging service...");
        List<Integer> loggingServicePorts = loggingServiceConfig.getTargetPorts();

        for (int port : loggingServicePorts) {
            try {
                String response = restTemplate.getForObject("http://localhost:" + port + "/messages", String.class);
                log.info("Response from logging service at port {}: {}", port, response);
                List<Message> loggingMessages = parseMessages(response);
                log.info("Parsed messages from logging service: {}", loggingMessages);
                messages.addAll(loggingMessages);
                break;
            } catch (RestClientException e) {
                log.error("Failed to fetch messages from logging service at port {}", port, e);
            }
        }

        log.info("Fetching messages from messaging service...");
        List<Integer> messagingServicePorts = messagingServiceConfig.getTargetPorts();
        for (int port : messagingServicePorts) {
            try {
                String response = restTemplate.getForObject("http://localhost:" + port + "/messages", String.class);
                log.info("Response from messaging service at port {}: {}", port, response);
                List<Message> messagingMessages = parseMessages(response);
                log.info("Parsed messages from messaging service: {}", messagingMessages);
                messages.addAll(messagingMessages);
                break;
            } catch (RestClientException e) {
                log.error("Failed to fetch messages from messaging service at port {}", port, e);
            }
        }

        return messages;
    }

    private List<Message> parseMessages(String response) {
        try {
            return objectMapper.readValue(response, new TypeReference<List<Message>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing messages from response: {}", response, e);
            return new ArrayList<>();
        }
    }
}
