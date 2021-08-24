package com.eric.projects.bargainrush.service;

import com.eric.projects.bargainrush.dao.GoodsDao;
import com.eric.projects.bargainrush.domain.BargainRushGoods;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getByGoodsId(long goodsId) {
        return goodsDao.getByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {
        BargainRushGoods bargainRushGoods = new BargainRushGoods();
        bargainRushGoods.setGoodsId(goods.getId());
        goodsDao.reduceStock(bargainRushGoods);
    }

}
