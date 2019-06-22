package com.wx.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadPicFromURLUtil {
	/*
	 * public static void main(String[] args) { String url =
	 * "http://mmbiz.qpic.cn/mmbiz_jpg/ftQcB8PbW3HvnkDu7M86ticwquzs5icBUAkSIgbfibxo0KCOKmAWBsYcxRGAtWia9njW6jVvDc94ClUa0QgZaS50Ig/0";
	 * String path="C:\\Users\\Administrator\\Desktop\\图片\\pic.jpg";
	 * downloadPicture(url,path); }
	 */
	/**
	 * 更具链接下载图片
	 * @param urlList
	 * @param path
	 */
    public static String downloadPicture(String urlList,String path) {
        URL url = null;
        String realPath=path;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
 
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
 
            byte[] buffer = new byte[1024];
            int length;
 
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return realPath;
    }

}
