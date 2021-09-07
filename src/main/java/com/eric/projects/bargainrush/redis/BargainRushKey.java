package com.eric.projects.bargainrush.redis;

public class BargainRushKey extends BasePrefix {
    public BargainRushKey(String prefix) {
        super(prefix);
    }

    public static BargainRushKey isOutOfGoods = new BargainRushKey("OutOfGoods");
}
