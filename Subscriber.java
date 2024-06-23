import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;

public class Subscriber {
    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        client.getTopic("my-topic").addMessageListener(new MessageListener<String>() {
            @Override
            public void onMessage(Message<String> message) {
                System.out.println("Received: " + message.getMessageObject());
            }
        });

        // Keep the program running
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.shutdown();
    }
}
