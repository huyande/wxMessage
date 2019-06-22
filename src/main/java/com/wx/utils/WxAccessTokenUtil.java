package com.wx.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
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
	
	/**
	 * 判断access_token 是否失效 如果失效 则重新生成 没有失效 则返回
	 */
	public static void iSaccessTokenTimeOut(Map<String,Date> accessMap,String appid,String secret) {
		String accessToken ="";
		if(accessMap.size()==0) {
			accessToken = getAccessToken(appid,secret);
			accessMap.put(accessToken, new Date());
			System.err.println("新建");
		}else {
			System.err.println("取map");
			int deltaTime=0; //时间差
			for(Map.Entry<String, Date> entry: accessMap.entrySet()) {
				Date oldTime = entry.getValue();
				deltaTime = DateUtil.getSecondsBetween(oldTime, new Date());
				System.err.println(deltaTime);
				if(deltaTime>7200) {
					//如果时间差大于 7200 秒  重新生成一个 access token  并且先清除map 集合 在添加
					accessMap.clear();
					accessToken = getAccessToken(appid,secret);
					accessMap.put(accessToken, new Date());
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Date convertStrToDate;
		try {
			convertStrToDate = DateUtil.convertStrToDate("2019-6-22 13:49:47");
			int secondsBetween = DateUtil.getSecondsBetween(convertStrToDate,new Date());
			System.out.println(new Date());
			System.out.println(convertStrToDate);
			System.out.println(secondsBetween);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
}
