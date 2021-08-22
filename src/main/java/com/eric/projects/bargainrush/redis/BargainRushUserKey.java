package com.eric.projects.bargainrush.redis;

public class BargainRushUserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600*24 * 2;
    private BargainRushUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static BargainRushUserKey TOKEN = new BargainRushUserKey(TOKEN_EXPIRE, "token");

}
