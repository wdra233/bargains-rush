package com.eric.projects.bargainrush.redis;

public interface KeyPrefix {
    long expireSeconds();

    String getPrefix();
}
