package com.wx.service;

import java.io.File;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wx.bean.result.AnswerSet;
import com.wx.bean.result.SubjectCount;

public interface QuestionService {
	/**
	 * 分页查询 问题答案
	 * @param currentpage
	 * @param pagesize
	 * @param search
	 * @return
	 */
	PageInfo<AnswerSet> searchQuestByContentLike(Integer currentpage, Integer pagesize, String search);

}
