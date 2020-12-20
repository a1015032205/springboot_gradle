package com.springboot.md.dao;

import com.springboot.md.pojo.BossJavaJob;

import java.util.List;

public interface BossJavaJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossJavaJob record);

    int insertSelective(BossJavaJob record);

    BossJavaJob selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BossJavaJob record);

    int updateByPrimaryKey(BossJavaJob record);

    int add(List<BossJavaJob> record);
}