package com.eric.projects.bargainrush.domain;

import lombok.Data;

@Data
public class BargainRushOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
