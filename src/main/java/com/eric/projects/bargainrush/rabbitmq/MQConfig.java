package com.eric.projects.bargainrush.rabbitmq;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";

    public static final String BARGAINRUSH_QUEUE = "bargainrush_queue";

    @Bean
    public Queue bargainRushQueue() {
        return new Queue(BARGAINRUSH_QUEUE, true);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
}
