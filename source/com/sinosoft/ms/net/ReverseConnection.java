package com.sinosoft.ms.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.sinosoft.ms.exception.BusinessException;

/**
 * 消息反向推送的触发器。
 * 
 * @author HanYan
 * @date 2014-11-28
 */
public class ReverseConnection implements Connection {
	
	private final static Logger log = Logger.getLogger(ReverseConnection.class);
	
	public String connect(String url, String content) {
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			URL urlAll = new URL(url + (content != null ? ("?TaskId=" + content) : ""));
			httpUrlConn = (HttpURLConnection) urlAll.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("content-type", "text/html");
			httpUrlConn.setAllowUserInteraction(true);
			httpUrlConn.connect();
			inputStream = httpUrlConn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (IOException e) {
			throw new BusinessException("链接时I/O异常！", e);
		} catch (Exception e) {
			throw new BusinessException("链接时异常！", e);
		} finally {
			try {
				if (inputStream != null) inputStream.close();
			} catch (Exception e) {
				log.warn("InputStream close warning...");
			}
			try {
				if (reader != null) reader.close();
			} catch (Exception e) {
				log.warn("BufferedReader close warning...");
			}
			try {
				if (httpUrlConn != null) httpUrlConn.disconnect();
			} catch (Exception e) {
				log.warn("HttpURLConnection disconnect warning...");
			}
		}
	}
}
