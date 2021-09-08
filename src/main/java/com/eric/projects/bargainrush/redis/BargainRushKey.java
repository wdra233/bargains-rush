package com.eric.projects.bargainrush.redis;

public class BargainRushKey extends BasePrefix {
    public BargainRushKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static BargainRushKey isOutOfGoods = new BargainRushKey(0,"OutOfGoods");
    public static BargainRushKey getBargainPath = new BargainRushKey(60, "BargainPath");
}
