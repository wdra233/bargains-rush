package com.eric.projects.bargainrush.redis;

public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }

    // KeyPrefix naming space
    public static UserKey getById = new UserKey("id");

    public static UserKey getByName = new UserKey("name");
}
