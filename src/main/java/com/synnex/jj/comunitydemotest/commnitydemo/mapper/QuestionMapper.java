package com.synnex.jj.comunitydemotest.commnitydemo.mapper;

import com.synnex.jj.comunitydemotest.commnitydemo.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {

    @Insert("Insert into publish(title,content,tag,gmt_create,gmt_modified,creator) values " +
            "(#{title},#{content},#{tag},#{gmtCreate},#{gmtModified},#{creator})")
    void create(Question question);
}
