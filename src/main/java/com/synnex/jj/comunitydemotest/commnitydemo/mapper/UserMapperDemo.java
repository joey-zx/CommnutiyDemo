package com.synnex.jj.comunitydemotest.commnitydemo.mapper;

import com.synnex.jj.comunitydemotest.commnitydemo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapperDemo {


    @Select("select * from user where token=#{token}")
    User findUserByToken(String token);

    @Select("insert into user(name,account_id,token,gmt_create,gmt_modified) " +
            "values(#{name},#{clientId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
