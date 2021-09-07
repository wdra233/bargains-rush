package com.eric.projects.bargainrush.service;

import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.OrderInfo;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.BargainRushKey;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BargainRushService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo goBargain(User user, GoodsVo goods) {
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            return orderService.createOrder(user, goods);
        }
        setOutOfGoods(goods.getId());
        return null;
    }

    private void setOutOfGoods(Long goodsId) {
        redisService.set(BargainRushKey.isOutOfGoods, ""+goodsId, true);
    }

    private boolean getOutOfGoods(long goodsId) {
        return redisService.exists(BargainRushKey.isOutOfGoods, ""+goodsId);
    }

    public long getResult(Long userId, long goodsId) {
        BargainRushOrder order = orderService.getByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean exist = getOutOfGoods(goodsId);
            if (exist) {
                return -1;
            } else {
                return 0;
            }
        }

    }
}
