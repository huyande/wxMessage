package com.wx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.wx.bean.Image;
import com.wx.bean.ImageMessage;
import com.wx.bean.TextMessage;

/**
 * 消息回复 工具方法 
 * @author Administrator
 *
 */
public class ReplyMsgUtils {

	/**
	 * 发送图片的接口 
	 * @param msgXmlMap
	 * @param accessMap
	 * @param WxUser
	 * @param appid
	 * @param secret
	 * @param wXfileImagePath
	 * @return
	 */
	public  static String replyImageMsg(Map<String, String> msgXmlMap,Map<String,Date> accessMap,String WxUser,String appid,String secret,String wXfileImagePath) {
	    //素材id
	    String media_id = "";
		//获取图片的地址 上传图片
		String PicUrl = msgXmlMap.get("PicUrl");
		//上传图片到素材库中 
		media_id = uploadImage(PicUrl, wXfileImagePath, accessMap, appid, secret);
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setToUserName(msgXmlMap.get("FromUserName"));
        imageMessage.setFromUserName(WxUser);
        imageMessage.setCreateTime(new Date().toString());
        Image image = new Image();
        image.setMediaId(media_id);
		imageMessage.setImage(image);
		imageMessage.setMsgType("image");
		String sendImageMsg = sendImageMsg(imageMessage);
		return sendImageMsg;
	}
	
	/**
	 * 回复文本消息
	 * @param msgXmlMap
	 * @param WxUser
	 * @return
	 */
	public static String replyTextMsg(Map<String, String> msgXmlMap,String WxUser,String replyStr) {
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(msgXmlMap.get("FromUserName"));
		textMessage.setFromUserName(WxUser);
		textMessage.setCreateTime(new Date().toString());
		textMessage.setMsgType(WXmlUtils.MESSAGE_TEXT);
		textMessage.setContent(replyStr);
		String sendTextMsg = sendTextMsg(textMessage);
		return sendTextMsg;
	}
	
	
	/**
	 *  生成文本消息
	 * @param textMessage
	 * @return
	 */
	public static String sendTextMsg(TextMessage textMessage) {
		return WXmlUtils.textMessageToXml(textMessage);
	}
	/**
	 * 生成图片消息
	 * @param imageMessage
	 * @return
	 */
	public static String sendImageMsg(ImageMessage imageMessage) {
		return WXmlUtils.imageMessageToXml(imageMessage);
	}
	
	/**
	 * 获取到消息中的图片地址 将其上传到素材中
	 * 
	 * @return 返回 素材id
	 */
	public static String uploadImage(String PicUrl,String wXfileImagePath,Map<String,Date> accessMap,String appid,String secret) {
		String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
		//素材id
	    String media_id = "";
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
		return media_id;
	}
}
