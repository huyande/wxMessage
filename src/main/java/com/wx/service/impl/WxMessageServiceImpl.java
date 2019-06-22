package com.wx.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.wx.bean.Image;
import com.wx.bean.ImageMessage;
import com.wx.bean.TextMessage;
import com.wx.service.WxMessageService;
import com.wx.utils.DownloadPicFromURLUtil;
import com.wx.utils.WXmlUtils;
import com.wx.utils.WxAccessTokenUtil;
import com.wx.utils.WxUploadUtil;

@Service
public class WxMessageServiceImpl implements WxMessageService{
	
	private Map<String,Date> accessMap = new HashMap<String, Date>();

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
				//上传图片的接口地址
			    String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
			    //素材id
			    String media_id = "";
				//获取图片的地址 上传图片
				String PicUrl = msgXmlMap.get("PicUrl");
				//下载图片到本地
				String realpath = DownloadPicFromURLUtil.downloadPicture(PicUrl, wXfileImagePath+UUID.randomUUID().toString()+".png");
				
				//调用获取token 
				//String accessToken = WxAccessTokenUtil.getAccessToken(appid, secret);
				//Map<String,Date> accessMap = new HashMap<String, Date>();
				WxAccessTokenUtil.iSaccessTokenTimeOut(accessMap,appid, secret);
				String accessToken ="";
				for(Map.Entry<String, Date> entry: accessMap.entrySet()) {
					accessToken = entry.getKey();
				}
				System.out.println(accessToken);
				//获取到本地的下载的图片 
				String replacedUrl = UPLOAD_FOREVER_MEDIA_URL
	                    .replace("ACCESS_TOKEN", accessToken)
	                    .replace("TYPE", "image");
				File file = new File(realpath);
				InputStream in;
				try {
					in = new FileInputStream(file);
					media_id = WxUploadUtil.uploadFile(replacedUrl, in, realpath);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ImageMessage imageMessage = new ImageMessage();
				imageMessage.setToUserName(msgXmlMap.get("FromUserName"));
		        imageMessage.setFromUserName(WxUser);
		        imageMessage.setCreateTime(new Date().toString());
		        Image image = new Image();
		        image.setMediaId(media_id);
				imageMessage.setImage(image);
				imageMessage.setMsgType("image");
				String sendImageMsg = sendImageMsg(imageMessage);
				msg = sendImageMsg;
				break;
			default:
				TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(msgXmlMap.get("FromUserName"));
				textMessage.setFromUserName(WxUser);
				textMessage.setCreateTime(new Date().toString());
				textMessage.setMsgType(WXmlUtils.MESSAGE_TEXT);
				textMessage.setContent("哈哈哈");
				String sendTextMsg = sendTextMsg(textMessage);
				msg = sendTextMsg;
				break;
		}
		return msg;
	}
	
	/**
	 *  生成文本消息
	 * @param textMessage
	 * @return
	 */
	public String sendTextMsg(TextMessage textMessage) {
		return WXmlUtils.textMessageToXml(textMessage);
	}
	/**
	 * 生成图片消息
	 * @param imageMessage
	 * @return
	 */
	public String sendImageMsg(ImageMessage imageMessage) {
		return WXmlUtils.imageMessageToXml(imageMessage);
	}

}
