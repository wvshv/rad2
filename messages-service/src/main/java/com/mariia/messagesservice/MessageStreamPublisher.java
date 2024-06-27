package com.mariia.facadeservice.api.messagingservice;

import com.mariia.messagesservice.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageStreamPublisher {
    private final Logger log = LoggerFactory.getLogger(MessageStreamPublisher.class);
    private final StreamBridge streamBridge;
    private final MimeType protobufMimeType;

    public MessageStreamPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.protobufMimeType = MimeTypeUtils.APPLICATION_JSON;
    }

    public void publishMessage(Message message) {
        log.info("Publishing message: {}", message);
        streamBridge.send("messages-out-0", message, protobufMimeType);
    }
}
