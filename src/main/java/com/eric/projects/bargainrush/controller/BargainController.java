package com.eric.projects.bargainrush.controller;

import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.OrderInfo;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.result.CodeMsg;
import com.eric.projects.bargainrush.service.BargainRushService;
import com.eric.projects.bargainrush.service.GoodsService;
import com.eric.projects.bargainrush.service.OrderService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/do_bargain")
    public String list(Model model, User user,
                       @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }

        GoodsVo goods = goodsService.getByGoodsId(goodsId);
        int stockCount = goods.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errmsg", CodeMsg.BARGAIN_COMPLETE.getMsg());
            return "bargain_fail";
        }

        BargainRushOrder order = orderService.getByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.DUPLICATE_BARGAIN.getMsg());
            return "bargain_fail";
        }

        // reduce stock,create an orderInfo using bargainrush_service
        final OrderInfo orderInfo = bargainRushService.goBargain(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
}
