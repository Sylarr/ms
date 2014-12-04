package com.sinosoft.ms.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.sinosoft.ms.cache.SystemCache;
import com.sinosoft.ms.exception.BusinessException;

/**
 * 用于链接短信平台的连接器（短信平台客户端）。
 * 
 * @author HanYan
 * @date 2014-10-30
 */
public class SmsConnection implements Connection {
	
	private final static Logger log = Logger.getLogger(SmsConnection.class);
	
	public String connect(String url, String content) {
		HttpURLConnection httpUrlConn = null;
		InputStream is = null;
		BufferedReader reader = null;
		OutputStream os = null;
		BufferedWriter writer = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			URL urlAll = new URL(url);
			httpUrlConn = (HttpURLConnection) urlAll.openConnection();
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setRequestMethod("POST");
//			httpUrlConn.setRequestProperty("content-type", "text/html");
//			httpUrlConn.setAllowUserInteraction(true);
			httpUrlConn.connect();
			os = httpUrlConn.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os, SystemCache.getCache("short.msg.encoding")));
			writer.write(content);
			writer.flush();
			
			is = httpUrlConn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, SystemCache.getCache("short.msg.encoding")));
			
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (IOException e) {
			throw new BusinessException("链接短信平台时I/O异常！", e);
		} catch (Exception e) {
			throw new BusinessException("链接短信平台时异常！", e);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (Exception e) {
				log.warn("reader close warning...");
			}
			try {
				if (is != null) is.close();
			} catch (Exception e) {
				log.warn("is close warning...");
			}
			try {
				if (writer != null) writer.close();
			} catch (Exception e) {
				log.warn("writer close warning...");
			}
			try {
				if (os != null) os.close();
			} catch (Exception e) {
				log.warn("os close warning...");
			}
			try {
				if (httpUrlConn != null) httpUrlConn.disconnect();
			} catch (Exception e) {
				log.warn("HttpURLConnection disconnect warning...");
			}
		}
	}
}
