package com.eric.projects.bargainrush.rabbitmq;

import com.eric.projects.bargainrush.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendBargainRushMessage(BargainRushMessage message) {
        String msg = RedisService.beanToString(message);
        log.info("sending message: {}", msg);
        amqpTemplate.convertAndSend(MQConfig.BARGAINRUSH_QUEUE, msg);
    }

}
