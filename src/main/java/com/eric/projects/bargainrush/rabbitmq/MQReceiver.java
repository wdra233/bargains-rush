package com.eric.projects.bargainrush.rabbitmq;

import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.service.BargainRushService;
import com.eric.projects.bargainrush.service.GoodsService;
import com.eric.projects.bargainrush.service.OrderService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    BargainRushService bargainRushService;

    @RabbitListener(queues = {MQConfig.BARGAINRUSH_QUEUE})
    public void receive(String message) {
        log.info("Receiving message: {}", message);
        BargainRushMessage bargainRushMessage = RedisService.stringToBean(message, BargainRushMessage.class);
        User user = bargainRushMessage.getUser();
        long goodsId = bargainRushMessage.getGoodsId();

        GoodsVo goods = goodsService.getByGoodsId(goodsId);
        int stock = goods.getGoodsStock();
        if (stock <= 0) {
            return;
        }

        BargainRushOrder order = orderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        bargainRushService.goBargain(user, goods);
    }
}
