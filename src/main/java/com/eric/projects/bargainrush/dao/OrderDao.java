package com.eric.projects.bargainrush.dao;

import com.eric.projects.bargainrush.domain.BargainRushOrder;
import com.eric.projects.bargainrush.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Select("select * from bargainrush_order where user_id=#{userId} and goods_id=#{goodsId}")
    public BargainRushOrder getBargainRushOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values(" +
            "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before=false, statement="select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into bargainrush_order(user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    public int insertBargainRushOrder(BargainRushOrder bargainRushOrder);


    @Select("select * from order_info where id = #{orderId}")
    public OrderInfo getOrderById(@Param("orderId") long orderId);
}
