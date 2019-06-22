package com.wx.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WxAccessTokenUtil {

	public static String URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	
	public static String getAccessToken(String appid,String secret) {
		 String replacedUrl = URL
                 .replace("APPID", appid)
                 .replace("SECRET", secret);
		 String access_token = "";
		 try {
			String boby = HttpClientUtil.get(replacedUrl);
			ObjectMapper mapper = new ObjectMapper();
			Map<String,String> data = mapper.readValue(boby, Map.class);
			access_token=data.get("access_token");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return access_token;
	}
	
	public static void main(String[] args) {
		String accessToken = getAccessToken("wx650208a7166d6fcc", "905cfbc2858b64a63989c70e0614eb00");
		System.out.println(accessToken);
	}
}
