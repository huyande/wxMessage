package com.wx.bean;

import java.util.Date;

/**
 * 
 * 
 * @author huyande
 * 
 * @date 2019-05-26
 */
public class TQuestion {
    private Integer id;

    /**
     * 课程主键
     */
    private Integer subId;

    /**
     * 问题内容
     */
    private String content;
    
    /**
     * 原始信息
     */
    private String OriginalContent;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getOriginalContent() {
		return OriginalContent;
	}

	public void setOriginalContent(String originalContent) {
		OriginalContent = originalContent;
	}
    
    
}