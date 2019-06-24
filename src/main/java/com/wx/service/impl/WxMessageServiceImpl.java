package com.wx.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wx.bean.Image;
import com.wx.bean.ImageMessage;
import com.wx.bean.TAnswer;
import com.wx.bean.TQuestion;
import com.wx.bean.TSubject;
import com.wx.bean.TextMessage;
import com.wx.bean.result.AnswerSet;
import com.wx.dao.TAnswerMapper;
import com.wx.dao.TQuestionMapper;
import com.wx.dao.TSubjectMapper;
import com.wx.service.WxMessageService;
import com.wx.utils.DownloadPicFromURLUtil;
import com.wx.utils.ReplyMsgUtils;
import com.wx.utils.WXmlUtils;
import com.wx.utils.WxAccessTokenUtil;
import com.wx.utils.WxUploadUtil;

@Service
public class WxMessageServiceImpl implements WxMessageService{
	
	private Map<String,Date> accessMap = new HashMap<String, Date>();
	
	@Autowired
	private TQuestionMapper questionMapper;
	
	@Autowired
	private TSubjectMapper subjectMapper;
	
	@Autowired
	private TAnswerMapper answerMapper;

	@Override
	public Map<String, String> receptionMsg(HttpServletRequest request) {
		Map<String, String> xmlToMap =null;
		try {
			xmlToMap = WXmlUtils.xmlToMap(request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return xmlToMap;
	}

	@Override
	public String sendDealMsg(Map<String, String> msgXmlMap,String WxUser,String appid,String secret,String wXfileImagePath) {
		//System.out.println(msgXmlMap);
		//消息类型
		String MsgType = msgXmlMap.get("MsgType");
		String msg="";
		
		switch (MsgType) {
			case WXmlUtils.MESSAGE_IMAGE:
				//调用发送图片的接口 
				msg = ReplyMsgUtils.replyImageMsg(msgXmlMap, accessMap, WxUser, appid, secret, wXfileImagePath);
				break;
			default:
				PageInfo<AnswerSet> answerSet = searchQuestByContentLike(1,10,msgXmlMap.get("Content").replaceAll("[\\pP‘’“”]", ""));
				List<AnswerSet> list = answerSet.getList();
				StringBuilder sb = new StringBuilder();
				for(AnswerSet ans :list) {
					sb.append("【问题：】"+ans.getQuestion()).append("\n\n").append("【答案：】"+ans.getAnswerStr()).append("\n\n");
				}
				//<a href='www.baidu.com'>点我立即充值</a>
				msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
				break;
		}
		return msg;
	}
	
	
	public PageInfo<AnswerSet> searchQuestByContentLike(Integer currentpage, Integer pagesize, String search) {
		int page = currentpage==null?1:currentpage;
		int pageSize = pagesize==null?10:pagesize;
		PageHelper.startPage(page,pageSize);
		//查到所有的问题列表
		List<TQuestion> questList = questionMapper.searchQuestByContentLike(search);
		List<AnswerSet> answerList = new ArrayList<>();
		for(TQuestion quest:questList){
			AnswerSet answerSet = new AnswerSet();
			TSubject subject = subjectMapper.selectByPrimaryKey(quest.getSubId());
			//查询答案集合 
			List<TAnswer> answersList = answerMapper.selectByQuestId(quest.getId());
			List<String> ansStrList = new ArrayList<>();
			StringBuilder anStrsb = new StringBuilder();
			//标识 是否是判断题还是选择题  false 判断题  true 选择题 
			boolean flag =false; 
			for (TAnswer tAnswer : answersList) {
				//如果答案的长度大于1  说明是选择题
				if(tAnswer.getAnswer().length()>1){
					String substring = tAnswer.getAnswer().substring(0, tAnswer.getAnswer().indexOf('.'));
					ansStrList.add(substring);
					flag=true;
				}else{ //其他则是判断题 判断题 无需截取字符串 
					ansStrList.add(tAnswer.getAnswer());
				}
			}
			Collections.sort(ansStrList);
			for (String str : ansStrList) {
				anStrsb.append(str+" ");
			}
			//设置问题
			//answerSet.setQuestion(quest.getContent());
			answerSet.setQuestId(quest.getId());
			answerSet.setQuestion(quest.getOriginalContent());
			answerSet.setSubjectName(subject.getSubName());
			if(flag){
				answerSet.setAnswers(answersList);
			}
			answerSet.setAnswerStr(anStrsb.toString());
			answerList.add(answerSet);
		}
		int count = questionMapper.searchQuestByContentLikeCount(search);
		
		PageInfo<AnswerSet> pageInfo = new PageInfo<>();
		pageInfo.setList(answerList);
		pageInfo.setTotal(count);
		return pageInfo;
	}

}
