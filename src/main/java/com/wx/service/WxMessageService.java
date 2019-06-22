package com.wx.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface WxMessageService {

	/**
	 * 接收用户信息
	 * @param request
	 * @return
	 */
	Map<String, String> receptionMsg(HttpServletRequest request);

	/**
	 *  回复用户消息 根据用户发送过来的消息类型进行回复
	 * @param msgXmlMap
	 * @return
	 */
	String sendDealMsg(Map<String, String> msgXmlMap,String WxUser,String appid,String secret,String wXfileImagePath);

}
