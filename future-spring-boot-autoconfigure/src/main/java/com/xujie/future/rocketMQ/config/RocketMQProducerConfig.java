package com.xujie.future.rocketMQ.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class RocketMQProducerConfig {

    @Bean
    public DefaultMQProducer defaultMQProducer(RocketMQProperties rocketMQProperties) {
        DefaultMQProducer producer = new DefaultMQProducer(rocketMQProperties.getProducer().getGroup());
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        try {
            producer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return producer;
    }
}
