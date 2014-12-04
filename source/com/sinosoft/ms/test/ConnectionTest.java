package com.sinosoft.ms.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sinosoft.ms.exception.BusinessException;

public class ConnectionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpURLConnection httpUrlConn = null;
		InputStream is = null;
		BufferedReader reader = null;
		OutputStream os = null;
		BufferedWriter writer = null;
		StringBuffer sb = new StringBuffer();
		
		String url = "http://172.17.1.80:8080/ms/remote";
//		String url = "http://localhost:8080/ms/remote";
		String content = "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<xml>"
			+ "<service>"
			+ "<serviceName>ShorMsg</serviceName>"
			+ "<version>1.0</version>"
			+ "<methodName>send</methodName>"
			+ "</service>"
			+ "<dto>"
			+ "<task>"
			+ "<taskId>RemoteTest</taskId>"
			+ "</task>"
			+ "<sender>"
			+ "<senderId>RemoteTest</senderId>"
			+ "<account>test</account>"
			+ "<password>cbff36039c3d0212b3e34c23dcde1456</password>"
			+ "<url>http://172.17.1.81:8080/ema/httpSms/sendSms</url>"
			+ "</sender>"
			+ "<msgs>"
			+ "<msgId>RemoteTest</msgId>"
			+ "<phoneNumber>13810247664</phoneNumber>"
			+ "<content>测试群发中文1</content>"
			+ "<constant>1</constant>"
			+ "</msgs>"
			+ "</dto>"
			+ "</xml>";
		
		try {
			URL urlAll = new URL(url);
			httpUrlConn = (HttpURLConnection) urlAll.openConnection();
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("content-type", "text/html");
//			httpUrlConn.setAllowUserInteraction(true);
			httpUrlConn.connect();
			os = httpUrlConn.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(content);
			writer.flush();
			
			is = httpUrlConn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			System.out.println(sb.toString());
		} catch (IOException e) {
			throw new BusinessException("链接时I/O异常！", e);
		} catch (Exception e) {
			throw new BusinessException("链接时异常！", e);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (Exception e) {
				System.err.println("reader close warning...");
			}
			try {
				if (is != null) is.close();
			} catch (Exception e) {
				System.err.println("is close warning...");
			}
			try {
				if (writer != null) writer.close();
			} catch (Exception e) {
				System.err.println("writer close warning...");
			}
			try {
				if (os != null) os.close();
			} catch (Exception e) {
				System.err.println("os close warning...");
			}
			try {
				if (httpUrlConn != null) httpUrlConn.disconnect();
			} catch (Exception e) {
				System.err.println("HttpURLConnection disconnect warning...");
			}
		}
	}
}
