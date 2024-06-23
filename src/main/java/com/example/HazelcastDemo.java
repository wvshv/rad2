package com.example;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.example.Publisher;
import com.example.Subscriber;
import java.util.Arrays;

public class HazelcastDemo {
    public static void main(String[] args)
        {
        if(Arrays.asList(args).contains("--publisher"))
        {
        Publisher.main(args);
        }
        else if (Arrays.asList(args).contains("--subscriber"))
        {
        Subscriber.main(args);
        }
        else
        {
        HazelcastDemo.run(args);
        }
        }

    public static void run(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");
        clientConfig.getNetworkConfig().addAddress("localhost:5701", "localhost:5702", "localhost:5703");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap<Integer, String> distributedMap = client.getMap("capitals");

        for (int i = 0; i < 1000; i++) {
            distributedMap.put(i, "Value " + i);
        }

        System.out.println("Map size: " + distributedMap.size());
        client.shutdown();
    }
}
