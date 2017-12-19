package normal;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * direct交换器
 *
 * @author chenxyz
 * @version 1.0
 * @date 2017-11-08
 */
public class DirectProducer {

    private static final String EXCHANGE_NAME = "direct_logs";

    private static final BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.DIRECT;

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明队列
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

        String[] serverities = {"error","info","warning"};

        for (int i=0; i < serverities.length; i++) {
            String server = serverities[i];
            String message = "Hello World, " + server + "!";

            channel.basicPublish(EXCHANGE_NAME, server, null, message.getBytes());
            System.out.println("Sent " + server + ":" + message);
        }

        channel.close();
        connection.close();

    }

}
