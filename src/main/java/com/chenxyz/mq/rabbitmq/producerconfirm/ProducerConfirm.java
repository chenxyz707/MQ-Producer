package com.chenxyz.mq.rabbitmq.producerconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送方确认
 *
 * @author chenxyz
 * @version 1.0
 * @date 2017-12-26
 */
public class ProducerConfirm {

    private static final String EXCHANGE_NAME = "producer_confirm";

    private static final String ROUTE_KEY = "error";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.confirmSelect();

        for(int i=0; i<2; i++) {
            String msg = "Hello_" + (i+1);
            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY, null, msg.getBytes());
            if (channel.waitForConfirms()) {
                System.out.println(ROUTE_KEY + ":" + msg);
            }
        }

        channel.close();
        connection.close();
    }

}
