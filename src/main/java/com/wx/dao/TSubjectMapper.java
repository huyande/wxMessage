package com.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.bean.TSubject;


public interface TSubjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSubject record);

    int insertSelective(TSubject record);

    TSubject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TSubject record);

    int updateByPrimaryKey(TSubject record);

	List<TSubject> selectByname(@Param(value="searchName") String searchName);

	int countByName(@Param(value="searchName")String searchName);
}