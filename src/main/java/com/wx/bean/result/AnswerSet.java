package com.wx.bean.result;

import java.util.List;

import com.wx.bean.TAnswer;



/**
 * @author Administrator
 *
 */
public class AnswerSet {
	private int questId;

	private String question;
	
	private String subjectName;
	
	private String answerStr;
	
	private List<TAnswer> answers;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<TAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<TAnswer> answers) {
		this.answers = answers;
	}

	public String getAnswerStr() {
		return answerStr;
	}

	public void setAnswerStr(String answerStr) {
		this.answerStr = answerStr;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}
		
}
