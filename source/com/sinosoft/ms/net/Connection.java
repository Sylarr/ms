package com.sinosoft.ms.net;

/**
 * 用于提交HTTP请求并接收响应的连接器。
 * 
 * @author HanYan
 * @date 2014-08-28
 */
public interface Connection {
	
	/**
	 * 链接方法
	 * @param url
	 * @param content 传送的数据
	 * @return 返回响应的数据。
	 */
	public String connect(String url, String content);
}
