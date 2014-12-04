package com.sinosoft.ms.action;

import java.util.Map;

import com.sinosoft.ms.service.AjaxService;
import com.sinosoft.ms.utils.StringUtil;

/**
 * 处理Ajax请求的Action。
 * 此类专门处理Ajax请求并返回JSON数据的Action在struts.xml中的配置较特殊。
 * 
 * @author HanYan
 * @date 2014-10-21
 */
public class AjaxAction {
	
	private AjaxService ajaxService;
	
	public void setAjaxService(AjaxService ajaxService) {
		this.ajaxService = ajaxService;
	}

	/**
	 * 获取下拉框数据：所有推送方式。
	 * @return
	 */
	public String selectKind() {
		options = ajaxService.findAllKind();
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 获取下拉框数据：所有邮件发送器。
	 * @return
	 */
	public String selectMailSender() {
		options = ajaxService.findAllMailSender();
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 校验邮件ID是否存在。
	 * @return
	 */
	public String checkMailInfoId() {
		errorMessage = ajaxService.checkMailInfoId(id);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 获取下拉框数据：所有短信发送器。
	 * @return
	 */
	public String selectShortMsgSender() {
		options = ajaxService.findAllShortMsgSender();
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 校验短信内容ID是否存在。
	 * @return
	 */
	public String checkShortMsgInfoId() {
		errorMessage = ajaxService.checkShortMsgInfoId(id);
		return StringUtil.SUCCESS;
	}
	
	/**
	 * 校验给定的反向请求信息ID(ReverseId)是否存在。
	 * @return
	 */
	public String checkReverseId() {
		errorMessage = ajaxService.checkReverseId(id);
		return StringUtil.SUCCESS;
	}
	
	/** -------------以下为Action参数------------- */
	
	private Map<String, String> options;
	private String id;
	private String errorMessage;

	public Map<String, String> getOptions() {
		return options;
	}
	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
