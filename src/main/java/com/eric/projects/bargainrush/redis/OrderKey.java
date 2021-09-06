package com.eric.projects.bargainrush.redis;

public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getBargainRushOrderByUidGid = new OrderKey("BargainRushOrder");

}
