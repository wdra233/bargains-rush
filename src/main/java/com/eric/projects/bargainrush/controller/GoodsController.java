package com.eric.projects.bargainrush.controller;

import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.service.GoodsService;
import com.eric.projects.bargainrush.service.UserService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, User user,
                         @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        final GoodsVo goods = goodsService.getByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int bargainStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            bargainStatus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        } else if (now > endAt) {
            bargainStatus = 2;
            remainSeconds = -1;
        } else {
            bargainStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("bargainStatus", bargainStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

}
