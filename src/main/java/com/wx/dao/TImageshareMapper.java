package com.wx.dao;

import com.wx.bean.TImageshare;

public interface TImageshareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TImageshare record);

    int insertSelective(TImageshare record);

    TImageshare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TImageshare record);

    int updateByPrimaryKey(TImageshare record);
}