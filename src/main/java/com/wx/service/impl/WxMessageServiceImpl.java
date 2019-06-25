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
import com.wx.bean.TImageshare;
import com.wx.bean.TQuestion;
import com.wx.bean.TSubject;
import com.wx.bean.TextMessage;
import com.wx.bean.result.AnswerSet;
import com.wx.dao.TAnswerMapper;
import com.wx.dao.TImageshareMapper;
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
	
	//存储access token
	private Map<String,Date> accessMap = new HashMap<String, Date>();
	//存储用户 上一次操作的代码
	private Map<String,String> handleLastMap = new HashMap<String, String>();
	
	private int flagShareOrBiaob =0; // 1 代表 表白  2 代表分享图片
	
	@Autowired
	private TQuestionMapper questionMapper;
	
	@Autowired
	private TSubjectMapper subjectMapper;
	
	@Autowired
	private TAnswerMapper answerMapper;
	
	@Autowired
	private TImageshareMapper imageshareMapper;

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
			case WXmlUtils.MESSAGE_TEXT:
				StringBuilder sb = new StringBuilder();
				//如果集合中 没有当前的用记录的  则进入 看用户回复的什么
				if(handleLastMap.containsKey(msgXmlMap.get("FromUserName"))) { //如果包含当前用户的数据 
					if(handleLastMap.get(msgXmlMap.get("FromUserName")).equals("1")) {
						if(msgXmlMap.get("Content").equals("0")) {
							flagShareOrBiaob = 0; //标记 恢复
							String menuShow = menuShow();
							sb.append("退出表白 感谢使用").append("\n\n").append(menuShow);
							msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							handleLastMap.remove(msgXmlMap.get("FromUserName"));
						}else {
							sb.append("此功能正在开发中...【回复0】退出").append("\n\n");
							msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
						}
						
					}else if(handleLastMap.get(msgXmlMap.get("FromUserName")).equals("2")) {
						if(msgXmlMap.get("Content").equals("0")) {
							flagShareOrBiaob = 0;//标记 恢复
							String menuShow = menuShow();
							sb.append("退出图片分享 感谢使用").append("\n\n").append(menuShow);;
							msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							handleLastMap.remove(msgXmlMap.get("FromUserName"));
						}else {
							sb.append("你发送的不是图片哦~，请分享张照片给我吧！").append("\n\n");
							sb.append("【回复 0 退出 分享图片】").append("\n\n");
							switch (MsgType) {
								default:
									msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
									break;
							}
							msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
						}
						
					}else if(handleLastMap.get(msgXmlMap.get("FromUserName")).equals("3")) {//执行想对应的东西 
						if(msgXmlMap.get("Content").equals("0")) {
							String menuShow = menuShow();
							sb.append("退出搜题  感谢使用").append("\n\n").append(menuShow);;
							msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							handleLastMap.remove(msgXmlMap.get("FromUserName"));
						}else {
							PageInfo<AnswerSet> answerSet = searchQuestByContentLike(1,10,msgXmlMap.get("Content").replaceAll("[\\pP‘’“”]", ""));
							List<AnswerSet> list = answerSet.getList();
							if(list.size()>0) {
								for(AnswerSet ans :list) {
									sb.append("【问题 】："+ans.getQuestion()).append("\n\n").append("【答案】："+ans.getAnswerStr()).append("\n\n");
									if(ans.getAnswers()!=null && ans.getAnswers().size()>0) {
										for(TAnswer tAns :ans.getAnswers()) {
											sb.append("☞"+tAns.getAnswer()).append("\n\n");
										}
									}
									
									sb.append("----/:rose《"+ans.getSubjectName()+"》/:rose----").append("\n\n");
								}
								//<a href='www.baidu.com'>点我立即充值</a>
								sb.append("<a href='https://mp.weixin.qq.com/s/FTpQ140rSHYlPTrmXjDhGA'>点我了〔解微信小程序〕 查询答案  更方便！回复【0】 退出搜题</a>").append("\n\n");
								//发送消息
								msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							}else {
								sb.append("/::O 您输入的内容 “"+msgXmlMap.get("Content")+"” 未找到答案，请输入题目内容前部分，【点击菜单，使用微信小程序 反馈给我相关课程信息】").append("\n\n");
								sb.append("<a href='https://mp.weixin.qq.com/s/FTpQ140rSHYlPTrmXjDhGA'>点我了〔解微信小程序〕 查询答案  更方便！</a>").append("\n\n");
								msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
							}
						}
						
					}
				}else {
					if(msgXmlMap.get("Content").equals("1")) {
						flagShareOrBiaob = 1; //标记 进入了表白
						sb.append("欢迎进入表白功能 此功能还在开发中，回复【0】退出").append("\n\n");
						msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
						handleLastMap.put(msgXmlMap.get("FromUserName"), "1");
					}else if(msgXmlMap.get("Content").equals("2")) {
						flagShareOrBiaob = 2; //标记 进入了分享图片
						sb.append("----------/:rose---------").append("\n\n");
						sb.append("欢迎进入校园图鉴功能").append("\n\n");
						sb.append("如果你有【校园美照】，请在此页面发送给我吧！").append("\n\n");
						sb.append("我会精心编辑 分享给大家").append("\n\n");
						sb.append("----------/:rose---------").append("\n\n");
						msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
						handleLastMap.put(msgXmlMap.get("FromUserName"), "2");
					}else if(msgXmlMap.get("Content").equals("3")){
						sb.append("----------/:rose---------").append("\n\n");
						sb.append("欢迎使用尔雅课答案查询功能").append("\n\n");
						sb.append("将问题回复到此页面 请等待一段时间").append("\n\n");
						sb.append("<a href='https://mp.weixin.qq.com/s/FTpQ140rSHYlPTrmXjDhGA'>点击我了解如何使用</a>").append("\n\n");
						sb.append("【回复 0 退出 搜题】").append("\n\n");
						sb.append("----------/:rose---------").append("\n\n");
						msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
						handleLastMap.put(msgXmlMap.get("FromUserName"), "3");
					}else {
						//菜单
						String menuShow = menuShow();
						sb.append(menuShow);
						msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb.toString());
					}
				}
				
				break;
			case WXmlUtils.MESSAGE_IMAGE:
				//调用发送图片的接口 
				//msg = ReplyMsgUtils.replyImageMsg(msgXmlMap, accessMap, WxUser, appid, secret, wXfileImagePath);
				if(flagShareOrBiaob==1) {
					//表白分享的照片
					msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,"感谢你的使用，此功能还在开发中");
				}else if(flagShareOrBiaob==2){
					//图鉴分享的照片
					TImageshare imageShare = new TImageshare();
					imageShare.setOpenid(msgXmlMap.get("FromUserName"));
					imageShare.setImageurl(msgXmlMap.get("PicUrl"));
					imageshareMapper.insert(imageShare);
					msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,"感谢你的分享，我会精心编辑分享给大家！");
				}
				break;
			case WXmlUtils.MESSAGE_EVENT:
				StringBuilder sb_event = new StringBuilder();
				//订阅事件
				if(msgXmlMap.get("Event").equals(WXmlUtils.MESSAGE_SUBSCRIBE)) {
					String menuShow = menuShow();
					sb_event.append("欢迎来到中卫校区校园服务。既然关注了，请不要轻易的取消关注吆！").append("\n\n")
					.append("【本站主打栏目】\n\n").append(menuShow);
					msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,sb_event.toString());
				}
				break;
			default:
				String menuShow = menuShow();
				msg = ReplyMsgUtils.replyTextMsg(msgXmlMap,msgXmlMap.get("ToUserName") ,menuShow);
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
	
	/**
	 * 菜单
	 * @return
	 */
	public String menuShow() {
		 StringBuilder sb = new StringBuilder();
		 sb.append("请回复对应的数字 进入相对应的功能\n\n").append("-----/:rose菜单STRT/:rose-----").append("\n\n")
		.append("〖1〗 告白\n\n").append("〖2〗 校园图片分享\n\n")
		.append("〖3〗 尔雅超星答案查询\n\n")
		.append("-------/:roseEND/:rose-------");
		 
		 return sb.toString();
	}

}
