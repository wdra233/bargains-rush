package com.eric.projects.bargainrush.controller;

import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.OrderInfo;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.result.CodeMsg;
import com.eric.projects.bargainrush.result.Result;
import com.eric.projects.bargainrush.service.BargainRushService;
import com.eric.projects.bargainrush.service.GoodsService;
import com.eric.projects.bargainrush.service.OrderService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bargain")
public class BargainController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    BargainRushService bargainRushService;

    @Autowired
    OrderService orderService;

    @PostMapping("/do_bargain")
    @ResponseBody
    public Result<OrderInfo> list(User user,
                       @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        GoodsVo goods = goodsService.getByGoodsId(goodsId);
        int stockCount = goods.getStockCount();
        if (stockCount <= 0) {
            return Result.error(CodeMsg.BARGAIN_COMPLETE);
        }

        BargainRushOrder order = orderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.DUPLICATE_BARGAIN);
        }

        // reduce stock,create an orderInfo using bargainrush_service
        final OrderInfo orderInfo = bargainRushService.goBargain(user, goods);
        return Result.success(orderInfo);
    }
}
