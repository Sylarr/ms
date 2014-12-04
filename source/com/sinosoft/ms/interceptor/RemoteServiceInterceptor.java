package com.sinosoft.ms.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sinosoft.ms.dto.ResponseMessage;
import com.sinosoft.ms.dto.ServiceDto;
import com.sinosoft.ms.exception.BusinessException;
import com.sinosoft.ms.remote.service.LocalServiceFactory;
import com.sinosoft.ms.utils.XMLUtil;

/**
 * 拦截Web服务的请求，并将字节流转换成XML格式的字符串。
 * 这里服务调用后返回的提示信息并不健壮、明确、完全，待以后完善。
 * 
 * @author HanYan
 * @date 2014-11-11
 */
public class RemoteServiceInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -177783326919091748L;
	private final static Logger log = Logger.getLogger(RemoteServiceInterceptor.class);
	
	private LocalServiceFactory localServiceFactory;
	
	public void setLocalServiceFactory(LocalServiceFactory localServiceFactory) {
		this.localServiceFactory = localServiceFactory;
	}


	/**
	 * 拦截方法。
	 */
	@Override
	public String intercept(ActionInvocation invocation) {
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
		}
		
		SAXReader reader = new SAXReader();
		Document reqDoc = null;
		Document respDoc = null;
		InputStream is1 = null;
		InputStream is2 = null;
		StringBuffer buffer = new StringBuffer();
		BufferedReader readerr = null;
		PrintWriter out = null;
		ResponseMessage message = null;
		String errorId = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			/** 用于复制InputStream的中间量 HEAD **/
			byte[] bs = new byte[1024];
			int len;
			while ((len = req.getInputStream().read(bs)) > -1) {
			    baos.write(bs, 0, len);
			}
			baos.flush();
			is1 = new ByteArrayInputStream(baos.toByteArray());// 用于打印报文
			is2 = new ByteArrayInputStream(baos.toByteArray());// 作为参数传入业务逻辑中
			readerr = new BufferedReader(new InputStreamReader(is1));
			String strMessage = "";
			while ((strMessage = readerr.readLine()) != null) {
				buffer.append(strMessage);
			}
			log.info("发送报文：" + buffer.toString());
			// 解析请求报文。
			reqDoc = reader.read(is2);
			Element root = reqDoc.getRootElement();
			ServiceDto serviceDto = XMLUtil.marshal(root.element("service"), ServiceDto.class);
			Method method1 = invocation.getAction().getClass().getMethod("setServiceDto", ServiceDto.class);
			method1.invoke(invocation.getAction(), serviceDto);
			Method method2 = invocation.getAction().getClass().getMethod("setArgs", Object[].class);
			method2.invoke(invocation.getAction(), new Object[]{new Object[]{XMLUtil.marshal(root.element("dto"), 
					localServiceFactory.getMethodArgsClasses(
							serviceDto.getServiceName(), 
							serviceDto.getVersion(), 
							serviceDto.getMethodName())[0])}});
		} catch (IOException e) {
			errorId = String.valueOf(System.currentTimeMillis());
			log.error("调用远程服务前，I/O异常，错误ID["+errorId+"]：", e);
			message = new ResponseMessage("1", "服务调用失败：I/O异常，请根据错误ID["+errorId+"]联系服务提供方。");
		} catch (DocumentException e) {
			log.error("调用远程服务前，XML文档解析异常：", e);
			message = new ResponseMessage("1", "服务调用失败：文档格式不正确。");
		} catch (BusinessException e) {
			log.error("调用远程服务前，Business异常：", e);
			message = new ResponseMessage("1", "服务调用失败：" + e.getMessage());
		} catch (Exception e) {
			errorId = String.valueOf(System.currentTimeMillis());
			log.error("调用远程服务前，其它异常，错误ID["+errorId+"]：", e);
			message = new ResponseMessage("1", "服务调用失败：未知异常，请根据错误ID["+errorId+"]联系服务提供方。");
		} finally {
			try {
				if (is1 != null) is1.close();
			} catch (IOException e) {
				log.warn("The InputStream closed warning in RemoteServiceInterceptor");
			}
			try {
				if (is2 != null) is2.close();
			} catch (IOException e) {
				log.warn("The InputStream closed warning in RemoteServiceInterceptor");
			}
			try {
				if (baos != null) baos.close();
			} catch (IOException e) {
				log.warn("The ByteArrayOutputStream closed warning in RemoteServiceInterceptor");
			}
		}
		
		
		try {
			if (message == null) {
				invocation.invoke();
				message = new ResponseMessage("0", "服务调用成功。调用结果暂不返回，待以后完善");
			}
		} catch (BusinessException e) {
			log.error("调用远程服务时异常：", e);
			message = new ResponseMessage("1", "服务调用失败：" + e.getMessage());
		} catch (Exception e) {
			errorId = String.valueOf(System.currentTimeMillis());
			log.error("调用远程服务时，未知异常，错误ID["+errorId+"]：", e);
			message = new ResponseMessage("1", "服务调用失败：未知异常，请根据错误ID["+errorId+"]联系服务提供方。");
		}
		
		// 响应消息
		try {
			//组织返回报文。这里服务调用后返回的提示信息并不健壮、明确、完全，待以后完善。
			out = resp.getWriter();
			respDoc = DocumentHelper.createDocument();
			Element root1 = respDoc.addElement("xml");
			XMLUtil.unmarshal(root1, message);
			out.print(respDoc.asXML());
			out.flush();
		} catch (IOException e) {
			errorId = String.valueOf(System.currentTimeMillis());
			log.error("调用远程服务时，错误ID["+errorId+"]，未知异常：", e);
			if (out != null) {
				out.print("<xml>服务器端异常。请根据错误ID["+errorId+"]联系服务提供方。</xml>");
				out.flush();
			}
		} finally {
			out.close();
		}
		
		return null;
	}
}
