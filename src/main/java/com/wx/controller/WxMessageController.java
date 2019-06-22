package com.wx.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wx.service.WxMessageService;

@RestController
public class WxMessageController {
	
	Logger log = LoggerFactory.getLogger(WxMessageController.class);

	
	@Autowired
	private WxMessageService wxMessageService;
	
	@Value("${wx.number}")
	private String wxNumber;
	
	@Value("${wx.appid}")
	private String appid;
	
	@Value("${wx.secret}")
	private String secret;
	
	@Value("${wx.fileImagePath}")
	private String wXfileImagePath;
	
	@PostMapping("wx")
	public String message(HttpServletRequest request) {
		Map<String, String> msgXmlMap = wxMessageService.receptionMsg(request);
		String xmlMsg = wxMessageService.sendDealMsg(msgXmlMap,wxNumber,appid,secret,wXfileImagePath);
		return xmlMsg;
	}
}
