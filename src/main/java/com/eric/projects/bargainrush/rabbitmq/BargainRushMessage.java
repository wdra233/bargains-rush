package com.eric.projects.bargainrush.rabbitmq;

import com.eric.projects.bargainrush.domain.User;
import lombok.Data;

@Data
public class BargainRushMessage {
    private User user;
    private long goodsId;
}
