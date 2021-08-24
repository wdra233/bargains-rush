package com.eric.projects.bargainrush.dao;

import com.eric.projects.bargainrush.domain.BargainRushGoods;
import com.eric.projects.bargainrush.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.bargain_price from bargainrush_goods mg left join goods g on mg.goods_id=g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.bargain_price from bargainrush_goods mg left join goods g on mg.goods_id=g.id where g.id=#{goodsId}")
    public GoodsVo getByGoodsId(@Param("goodsId")long goodsId);

    @Update("update bargainrush_goods set stock_count=stock_count-1 where goods_id=#{goodsId}")
    public int reduceStock(BargainRushGoods goods);
}
