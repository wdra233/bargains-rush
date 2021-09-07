package com.eric.projects.bargainrush.redis;

public class GoodsKey extends BasePrefix {
    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "goodlist");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "goodsdetail");
    public static GoodsKey getGoodsStock = new GoodsKey(0, "goodstock");
}
