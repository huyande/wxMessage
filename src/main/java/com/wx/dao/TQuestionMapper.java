package com.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.bean.TQuestion;
import com.wx.bean.result.QuestionCount;

public interface TQuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TQuestion record);

    int insertSelective(TQuestion record);

    TQuestion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TQuestion record);

    int updateByPrimaryKey(TQuestion record);

	List<TQuestion> searchQuestByContentLike(@Param(value="search") String search);

	int searchQuestByContentLikeCount(@Param(value="search")String search);

	List<QuestionCount> searchQuestBySubId();

	int searchQuestBySubIdCount();

	int searchAllQuest();
	//查询改课程id下的问题数
	int countQuestBySubIdCount(@Param(value="subId")Integer subId);

	List<TQuestion> selectQuestBySubId(@Param(value="subId")Integer subId);
}