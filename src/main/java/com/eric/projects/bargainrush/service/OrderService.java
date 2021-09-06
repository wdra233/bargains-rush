package com.eric.projects.bargainrush.service;

import com.eric.projects.bargainrush.dao.OrderDao;
import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.OrderInfo;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.OrderKey;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;

    public BargainRushOrder getByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getBargainRushOrderByUidGid, ""+userId+"_"+goodsId, BargainRushOrder.class);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }


    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getBargainPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);

        BargainRushOrder bargainRushOrder = new BargainRushOrder();
        bargainRushOrder.setGoodsId(goods.getId());
        bargainRushOrder.setOrderId(orderInfo.getId());
        bargainRushOrder.setUserId(user.getId());
        orderDao.insertBargainRushOrder(bargainRushOrder);

        redisService.set(OrderKey.getBargainRushOrderByUidGid, ""+user.getId()+"_"+goods.getId(), bargainRushOrder);

        return orderInfo;
    }


}
