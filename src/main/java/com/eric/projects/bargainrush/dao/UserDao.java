package com.eric.projects.bargainrush.dao;

import com.eric.projects.bargainrush.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from bargainrush_user where id = #{id}")
    public User getById(@Param("id") long id);
}
