package com.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.bean.TAnswer;


public interface TAnswerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TAnswer record);

    int insertSelective(TAnswer record);

    TAnswer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TAnswer record);

    int updateByPrimaryKey(TAnswer record);

	List<TAnswer> selectByQuestId(Integer questId);

	int deleteByQuestId(@Param(value="questId") Integer questId);
}