import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;

public class Publisher {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        ITopic<String> topic = client.getTopic("my-topic");
        for (int i = 1; i <= 100; i++) {
            topic.publish("Message " + i);
            System.out.println("Published: Message " + i);
        }

        client.shutdown();
    }
}
