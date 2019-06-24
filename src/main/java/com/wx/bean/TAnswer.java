package com.wx.bean;

import java.util.Date;

/**
 * 
 * 
 * @author huyande
 * 
 * @date 2019-05-26
 */
public class TAnswer {
    private Integer id;

    private Integer questId;

    private String answer;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

}