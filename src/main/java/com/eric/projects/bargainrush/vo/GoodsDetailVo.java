package com.eric.projects.bargainrush.vo;

import com.eric.projects.bargainrush.domain.User;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private int bargainStatus;
    private int remainSeconds;
    private GoodsVo goods;
    private User user;
}
