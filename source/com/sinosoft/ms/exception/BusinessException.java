package com.sinosoft.ms.exception;

/**
 * 封装的运行时异常，在必要的时候抛出，可以区分其它异常。
 * 并可以选择在最外围是否进行捕获并给予合理的处理。
 * 
 * @author HanYan
 * @date 2014-08-26
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 897858078317740120L;
	
	public BusinessException() {
		super("未知异常!");
	}
	
	public BusinessException(String errMsg) {
		super(errMsg);
	}
	
	public BusinessException(String errMsg, Exception e) {
		super(errMsg, e);
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
}
