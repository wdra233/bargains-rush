package com.eric.projects.bargainrush.controller;

import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.GoodsKey;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.result.Result;
import com.eric.projects.bargainrush.service.GoodsService;
import com.eric.projects.bargainrush.service.UserService;
import com.eric.projects.bargainrush.vo.GoodsDetailVo;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        // page caching
        String html = redisService.get(GoodsKey.getGoodsList, "" , String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        WebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail(HttpServletRequest request, HttpServletResponse response, Model model, User user,
                         @PathVariable("goodsId") long goodsId) {

        // retrieve cache
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }

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
        WebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(User user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getByGoodsId(goodsId);

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

        GoodsDetailVo detailVo = new GoodsDetailVo();
        detailVo.setGoods(goods);
        detailVo.setBargainStatus(bargainStatus);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setUser(user);
        return Result.success(detailVo);
    }

}
