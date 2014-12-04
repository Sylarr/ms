package com.sinosoft.ms.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.sinosoft.ms.exception.BusinessException;

/**
 * 查询核心系统的连接器。
 * 
 * @author HanYan
 * @date 2014-08-27
 */
public class QueryConnection implements Connection {
	
	private final static Logger log = Logger.getLogger(QueryConnection.class);
	
	public String connect(String url, String content) {
		HttpURLConnection httpUrlConn = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String startDate = format.format(cal.getTime());
		cal.add(Calendar.DATE, 7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String endDate = format.format(cal.getTime());
		
		log.info(url + "的起止日期为：" + startDate + " - " + endDate);
		try {
			URL urlAll = new URL(url + "&startDate=" + startDate + "&endDate=" + endDate);
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
