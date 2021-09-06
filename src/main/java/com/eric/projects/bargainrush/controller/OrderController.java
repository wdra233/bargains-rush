package com.eric.projects.bargainrush.controller;


import com.eric.projects.bargainrush.domain.OrderInfo;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.result.CodeMsg;
import com.eric.projects.bargainrush.result.Result;
import com.eric.projects.bargainrush.service.GoodsService;
import com.eric.projects.bargainrush.service.OrderService;
import com.eric.projects.bargainrush.vo.GoodsVo;
import com.eric.projects.bargainrush.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @GetMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getByGoodsId(goodsId);
        OrderDetailVo detail = new OrderDetailVo();
        detail.setGoods(goods);
        detail.setOrder(order);
        return Result.success(detail);
    }
}
