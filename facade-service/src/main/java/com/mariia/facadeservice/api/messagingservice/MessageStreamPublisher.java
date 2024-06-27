package com.mariia.facadeservice.api.messagingservice;

import com.mariia.facadeservice.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

@Component
public class MessageStreamPublisher {

    private final Logger log = LoggerFactory.getLogger(MessageStreamPublisher.class);
    private final StreamBridge streamBridge;
    private final MimeType protobufMimeType;

    public MessageStreamPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.protobufMimeType = new MimeType("application", "json");
    }

    public void publishMessage(Message message) {
        try {
            send(message);
        } catch (Exception e) {
            log.error("Can not publish message {}", message, e);
        }
    }

    private void send(Message message) {
        streamBridge.send("messages-out-0", MessageBuilder.withPayload(message)
                .setHeader("partitionKey", message.hashCode() % 10)
                .build(), protobufMimeType);
    }
}

