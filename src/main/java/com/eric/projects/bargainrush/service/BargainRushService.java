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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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

    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer originalCode = redisService.get(BargainRushKey.getVerifyCode, user.getId()+","+goodsId, Integer.class);
        if (originalCode == null || originalCode.intValue() != verifyCode) {
            return false;
        }
        redisService.delete(BargainRushKey.getVerifyCode, user.getId()+","+goodsId);
        return true;
    }

    public BufferedImage createVerifyCode(User user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //store verifyCode to redis
        int rnd = calc(verifyCode);
        redisService.set(BargainRushKey.getVerifyCode, user.getId()+","+goodsId, rnd);
        // output image
        return image;
    }


    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
