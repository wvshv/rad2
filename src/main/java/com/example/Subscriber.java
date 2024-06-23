package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;

public class Subscriber {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("localhost:5701", "localhost:5702", "localhost:5703");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        ITopic<Integer> topic = client.getTopic("example-topic");

        topic.addMessageListener(new MessageListener<Integer>() {
            @Override
            public void onMessage(Message<Integer> message) {
                System.out.println("Received: " + message.getMessageObject());
            }
        });

        // Keep the subscriber running
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        client.shutdown();
    }
}
