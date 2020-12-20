package com.springboot.md.dao;

import com.springboot.md.pojo.JavaJob51;

import java.util.List;

public interface JavaJob51Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JavaJob51 record);

    int insertSelective(JavaJob51 record);

    JavaJob51 selectByPrimaryKey(Integer id);

    List<JavaJob51> getLimit();

    int updateByPrimaryKeySelective(JavaJob51 record);

    int updateByPrimaryKey(JavaJob51 record);

    void addALL(List<JavaJob51> record);
}