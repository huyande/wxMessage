package com.wx.bean;

import java.util.Date;

/**
 * 
 * 
 * @author huyande
 * 
 * @date 2019-05-26
 */
public class TSubject {
    private Integer id;

    private String subName;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName == null ? null : subName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}