package com.sinosoft.ms.test;

import sun.misc.BASE64Encoder;

public class Base64Util {

	public static void main(String[] args) throws Exception {
		BASE64Encoder encoder = new BASE64Encoder();
		System.out.println(encoder.encode("name".getBytes()));
		System.out.println(encoder.encode("password".getBytes()));
	}
}
