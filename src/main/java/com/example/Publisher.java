package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;

public class Publisher {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("localhost:5701", "localhost:5702", "localhost:5703");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        ITopic<Integer> topic = client.getTopic("example-topic");

        for (int i = 1; i <= 100; i++) {
            topic.publish(i);
            System.out.println("Published: " + i);
            try {
                Thread.sleep(100); // Slow down the publishing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        client.shutdown();
    }
}
