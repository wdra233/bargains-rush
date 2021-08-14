package com.eric.projects.bargainrush.redis;

public interface KeyPrefix {
    int expireSeconds();

    String getPrefix();
}
