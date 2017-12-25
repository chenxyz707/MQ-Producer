package com.chenxyz.mq.rabbitmq.consumerconfirm;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者发送消息供消费者异步接收消息
 *
 * @author chenxyz
 * @version 1.0
 * @date 2017-12-21
 */
public class ConsumerConfirmProducer {

    private final static String EXCHANAGE_NAME = "direct_cc_confirm";

    private final static String ROUTE_KEY = "error";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANAGE_NAME, BuiltinExchangeType.DIRECT);

        for(int i=0; i<10; i++) {
            String message = "Hello world_" + (i+1);

            channel.basicPublish(EXCHANAGE_NAME, ROUTE_KEY,  null, message.getBytes());
            System.out.println("Sent " + ROUTE_KEY + message);
        }

        channel.close();
        connection.close();

    }
}
