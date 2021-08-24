package com.eric.projects.bargainrush.domain;

import lombok.Data;

import java.util.Date;

@Data
public class BargainRushGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
