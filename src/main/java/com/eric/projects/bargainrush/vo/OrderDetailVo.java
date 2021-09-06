package com.eric.projects.bargainrush.vo;

import com.eric.projects.bargainrush.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
