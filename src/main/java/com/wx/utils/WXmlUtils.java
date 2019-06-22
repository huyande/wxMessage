package com.wx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.wx.bean.Image;
import com.wx.bean.ImageMessage;
import com.wx.bean.TextMessage;

public class WXmlUtils {
	
	public static final String MESSAGE_SCAN="SCAN";//未关注公众号扫描二维码
	public static final String MESSAGE_TEXT="text";//文本
	public static final String MESSAGE_IMAGE="image";//图片
	public static final String MESSAGE_VOICE="voice";//语音
	public static final String MESSAGE_VIDEO="video";//视频
	public static final String MESSAGE_LINK="link";//连接
	public static final String MESSAGE_LOCATION="location";//地理位置事件
	public static final String MESSAGE_EVENT="event";//事件
	public static final String MESSAGE_SUBSCRIBE="subscribe";//关注
	public static final String MESSAGE_UNSUBSCRIBE="unsubscribe";//取消关注
	public static final String MESSAGE_CLICK="CLICK";//点击
	public static final String MESSAGE_VIEW="VIEW";//t跳转链接url
	
	/**
	 * xml转为map集合
	 * @MethodName：xmlToMap
	 *@author:maliran
	 *@ReturnType:Map<String,String>
	 *@param req
	 *@return
	 *@throws IOException
	 *@throws DocumentException
	 */
	public static Map<String,String> xmlToMap(HttpServletRequest req) throws IOException, DocumentException{
		
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();//log4j.jar
		InputStream ins = req.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for(Element e:list){
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	/**
	 * 文本转换为xml
	 * @MethodName：textMessageToXml
	 *@author:maliran
	 *@ReturnType:String
	 *@param textMessage
	 *@return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		
		XStream xstream = new XStream();//xstream.jar,xmlpull.jar
		xstream.alias("xml", textMessage.getClass());//置换根节点
		System.out.println(xstream.toXML(textMessage));
		return xstream.toXML(textMessage);
		
	}
	/**
	 * 图片转成xml
	 * @MethodName：textMessageToXml
	 *@author:maliran
	 *@ReturnType:String
	 *@param textMessage
	 *@return
	 */
    public static String imageMessageToXml(ImageMessage imageMessage){
		
		XStream xstream = new XStream();//xstream.jar,xmlpull.jar
		xstream.alias("xml", imageMessage.getClass());//置换根节点
		//System.out.println(xstream.toXML(imageMessage));
		return xstream.toXML(imageMessage);
		
	}
	
    /**
     * 组装图片xml
     * @MethodName：initImageMessage
     *@author:maliran
     *@ReturnType:String
     *@param MediaId
     *@param toUserName
     *@param fromUserName
     *@return
     */
	public static String initImageMessage(String MediaId,String toUserName,String fromUserName){		
		String message = null;
		Image image = new Image();
		ImageMessage imageMessage = new ImageMessage();
		image.setMediaId(MediaId);
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setCreateTime(new Date().toString());
		imageMessage.setImage(image);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		message = imageMessageToXml(imageMessage);
		return message;
	}
	public static String initTextMessage(String content,String toUserName,String fromUserName){		
		String message = null;
		TextMessage textMessage = new TextMessage();
		textMessage.setFromUserName(toUserName);
		textMessage.setToUserName(fromUserName);
		textMessage.setCreateTime(new Date().toString());
		textMessage.setContent(content);
		textMessage.setMsgType(MESSAGE_TEXT);
		message = textMessageToXml(textMessage);
		return message;
	}
}
