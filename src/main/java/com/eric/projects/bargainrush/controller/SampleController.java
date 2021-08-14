package com.eric.projects.bargainrush.controller;

import com.eric.projects.bargainrush.Result;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.redis.UserKey;
import com.eric.projects.bargainrush.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/hello")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "eric");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGetTest() {
        User user = userService.getUserById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTxTest() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<String> redisGetTest() {
        final String value = redisService.get(UserKey.getById, "key1", String.class);
        return Result.success(value);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSetTest() {

        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, "key1", user);
        return Result.success(true);
    }




}
