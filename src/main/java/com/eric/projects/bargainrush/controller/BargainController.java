package com.eric.projects.bargainrush.controller;

import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.OrderInfo;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.rabbitmq.BargainRushMessage;
import com.eric.projects.bargainrush.rabbitmq.MQSender;
import com.eric.projects.bargainrush.redis.GoodsKey;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.result.CodeMsg;
import com.eric.projects.bargainrush.result.Result;
import com.eric.projects.bargainrush.service.BargainRushService;
import com.eric.projects.bargainrush.service.GoodsService;
import com.eric.projects.bargainrush.service.OrderService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/bargain")
public class BargainController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    BargainRushService bargainRushService;

    @Autowired
    OrderService orderService;

    @Autowired
    MQSender sender;

    private static final Map<Long, Boolean> localMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }

        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getGoodsStock, ""+goods.getId(), goods.getStockCount());
            localMap.put(goods.getId(), false);
        }


    }

    @PostMapping("/{path}/do_bargain")
    @ResponseBody
    public Result<Integer> doBargain(User user,
                       @RequestParam("goodsId") long goodsId, @PathVariable("path")String path) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean isValidPath = bargainRushService.checkPath(user, goodsId, path);
        if (!isValidPath) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        boolean complete = localMap.get(goodsId);
        if (complete) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        long stock = redisService.decr(GoodsKey.getGoodsStock, ""+goodsId);
        if (stock < 0) {
            localMap.put(goodsId, true);
            return Result.error(CodeMsg.BARGAIN_COMPLETE);
        }

        BargainRushOrder order = orderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.DUPLICATE_BARGAIN);
        }

        BargainRushMessage message = new BargainRushMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);

        sender.sendBargainRushMessage(message);
        return Result.success(0); // enqueue
    }

    @GetMapping("/result")
    @ResponseBody
    public Result<Long> bargainResult(User user, @RequestParam("goodsId")long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = bargainRushService.getResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @GetMapping("/path")
    @ResponseBody
    public Result<String> getBargainPath(User user, @RequestParam("goodsId")long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String path = bargainRushService.createBargainPath(user, goodsId);
        return Result.success(path);
    }
}
