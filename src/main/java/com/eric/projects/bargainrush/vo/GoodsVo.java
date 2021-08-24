package com.eric.projects.bargainrush.vo;

import com.eric.projects.bargainrush.domain.Goods;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsVo extends Goods {
    private Double bargainPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
