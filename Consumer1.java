package com.example;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class Consumer1 {

    public static void main(String[] args) throws InterruptedException {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        IQueue<Integer> queue = hazelcastInstance.getQueue("lab-2-queue");

        while (true) {
            int number = queue.take();
            System.out.println("Received: " + number);

            Thread.sleep(1000);
        }
    }

}
