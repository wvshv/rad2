package com.mariia.messagesservice.messaging;

import java.util.Objects;

public class Message implements Comparable<Message> {
    private String uuid;
    private String message;

    public Message() {
    }

    public Message(String uuid, String message) {
        this.uuid = uuid;
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int compareTo(Message other) {
        return this.uuid.compareTo(other.uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(uuid, message1.uuid) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, message);
    }

    @Override
    public String toString() {
        return "Message{" +
                "uuid='" + uuid + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

