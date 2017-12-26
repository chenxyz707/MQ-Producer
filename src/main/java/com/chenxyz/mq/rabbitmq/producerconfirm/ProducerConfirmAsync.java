package com.chenxyz.mq.rabbitmq.producerconfirm;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送方异步确认
 *
 * @author chenxyz
 * @version 1.0
 * @date 2017-12-26
 */
public class ProducerConfirmAsync {

    private final static String EXCHANGE_NAME = "producer_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.confirmSelect();

        // deliveryTag代表了channel的唯一投递
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Ack deliveryTag=" + deliveryTag + ", multiple = " + multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Nack deliveryTag=" + deliveryTag + ", multiple = " + multiple);
            }
        });

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("replyText:"+replyText);
                System.out.println("exchange:"+exchange);
                System.out.println("routingKey:"+routingKey);
                System.out.println("msg:"+msg);
            }
        });

        String[] servers = {"error", "info", "warning"};

        for(int i=0; i < servers.length; i++) {
            String server = servers[i%3];
            String message = "Hello world_" + (i+1) + "_" + System.currentTimeMillis();
            channel.basicPublish(EXCHANGE_NAME, server, false, null, message.getBytes());

            System.out.println("Sent Message: [" + server + "]:" + message);

            Thread.sleep(200);
        }

        channel.close();
        connection.close();

    }
}
