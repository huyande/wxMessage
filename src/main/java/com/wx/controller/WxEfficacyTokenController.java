package com.wx.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wx.utils.SignUtil;

@RestController
public class WxEfficacyTokenController {
	Logger log = LoggerFactory.getLogger(WxEfficacyTokenController.class);

	
	@Value("${wx.token}")
	private String token;

	@GetMapping("wx")
	public String efficacyToken(String signature, String echostr, String timestamp, String nonce) {
		log.info("验证");
		// SHA1加密
		boolean checkSignature = SignUtil.checkSignature(signature, timestamp, nonce,token);
		if (checkSignature) {
			return echostr;
		}
		return null;
	}
}
