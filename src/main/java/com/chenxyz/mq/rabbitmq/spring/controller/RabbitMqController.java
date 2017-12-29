package com.chenxyz.mq.rabbitmq.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description
 *
 * @author chenxyz
 * @version 1.0
 * @date 2017-12-28
 */
@Controller
@RequestMapping("/rabbitmq")
public class RabbitMqController {

    private Logger logger = LoggerFactory.getLogger(RabbitMqController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ResponseBody
    @RequestMapping("/fanoutSender")
    public String fanoutSender(@RequestParam("message") String message) {
        String ret = "";
        try {
            for (int i=0; i<3; i++) {
                String str = "Fanout, the message_" + i + " is : " + message;
                logger.info("Send Message:[" + str + "]");
                rabbitTemplate.send("fanout-exchange", "",
                        new Message(str.getBytes(), new MessageProperties()));
            }
            ret = "successful !!!";
        } catch (Exception e) {
            ret = e.getCause().toString();
        }
        return ret;
    }

    @ResponseBody
    @RequestMapping("/topicSender")
    public String topicSender(@RequestParam("message") String message) {
        String ret = "";
        try {
            String[] severities = {"error", "info", "warning"};
            String[] modules = {"email", "order", "user"};
            for(int i=0; i < severities.length; i++) {
                for(int j=0; j < modules.length; j++) {
                    String routeKey = severities[i] + "." + modules[j];
                    String str = "The message is [rk:" + routeKey + "][message:" + message + "]";
                    rabbitTemplate.send("topic-exchange", routeKey,
                            new Message(str.getBytes(), new MessageProperties()));
                }
            }
            ret = "successful !!!";
        } catch (Exception e) {
            ret = e.getCause().toString();
        }
        return ret;
    }

}
