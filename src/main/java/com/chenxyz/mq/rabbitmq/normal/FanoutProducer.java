package com.chenxyz.mq.rabbitmq.normal;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * fanout交换器
 *
 * @author chenxyz
 * @version 1.0
 * @date 2017-11-08
 */
public class FanoutProducer {

    private final static String EXCHANGE_NAME = "fanout_logs";

    private final static BuiltinExchangeType EXCHANGE_TYPE = BuiltinExchangeType.FANOUT;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();//连接

        Channel channel = connection.createChannel();//信道

        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);//交换器

        String[]serverities = {"error","info","warning"};

        for(int i=0;i<3;i++){
            String server = serverities[i];
            String message = "Hello World, " + server + "!";

            channel.basicPublish(EXCHANGE_NAME, server, null, message.getBytes());
            System.out.println("Sent "+server+":"+message);

        }

        channel.close();
        connection.close();
    }
}
