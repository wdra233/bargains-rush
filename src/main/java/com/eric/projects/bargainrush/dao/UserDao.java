package com.eric.projects.bargainrush.dao;

import com.eric.projects.bargainrush.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    @Select("select * from bargainrush_user where id = #{id}")
    public User getById(@Param("id") long id);

    @Update("update bargainrush_user set password=#{password} where id=#{id}")
    public void update(User toBeUpdated);
}
