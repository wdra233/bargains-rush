package com.eric.projects.bargainrush.service;

import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.OrderInfo;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.BargainRushKey;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.util.MD5Util;
import com.eric.projects.bargainrush.util.UUIDUtil;
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

    public String createBargainPath(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(BargainRushKey.getBargainPath, ""+user.getId() + "_"+ goodsId, str);
        return str;
    }

    public boolean checkPath(User user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String originalPath = redisService.get(BargainRushKey.getBargainPath, ""+user.getId()+"_"+goodsId, String.class);
        return path.equals(originalPath);
    }
}
