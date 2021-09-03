package com.eric.projects.bargainrush.service;

import com.eric.projects.bargainrush.dao.UserDao;
import com.eric.projects.bargainrush.domain.User;
import com.eric.projects.bargainrush.exception.GlobalException;
import com.eric.projects.bargainrush.redis.BargainRushUserKey;
import com.eric.projects.bargainrush.redis.RedisService;
import com.eric.projects.bargainrush.result.CodeMsg;
import com.eric.projects.bargainrush.util.MD5Util;
import com.eric.projects.bargainrush.util.UUIDUtil;
import com.eric.projects.bargainrush.vo.LoginVo;
import jdk.nashorn.internal.objects.Global;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    public User getById(long id) {
        User user = redisService.get(BargainRushUserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        user = userDao.getById(id);
        if (user != null) {
            redisService.set(BargainRushUserKey.getById, "" + id, user);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        User user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        User toBeUpdated = new User();
        toBeUpdated.setId(id);
        toBeUpdated.setPassword(MD5Util.formPassToDbPass(formPass, user.getSalt()));
        userDao.update(toBeUpdated);

        // clear cache
        boolean delSuccess = redisService.delete(BargainRushUserKey.getById, ""+id);
        user.setPassword(toBeUpdated.getPassword());
        boolean updateSuccess = redisService.set(BargainRushUserKey.TOKEN, token, user);
        return delSuccess && updateSuccess;
    }

    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(BargainRushUserKey.TOKEN, token, User.class);
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDbPass(password, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(BargainRushUserKey.TOKEN, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge((int) BargainRushUserKey.TOKEN.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
