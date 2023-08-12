package com.ianmu.wiki.mapper;

import com.ianmu.wiki.entity.WikiUser;
import com.ianmu.wiki.entity.WikiUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WikiUserMapper {
    long countByExample(WikiUserExample example);

    int deleteByExample(WikiUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WikiUser row);

    int insertSelective(WikiUser row);

    List<WikiUser> selectByExample(WikiUserExample example);

    WikiUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") WikiUser row, @Param("example") WikiUserExample example);

    int updateByExample(@Param("row") WikiUser row, @Param("example") WikiUserExample example);

    int updateByPrimaryKeySelective(WikiUser row);

    int updateByPrimaryKey(WikiUser row);
}