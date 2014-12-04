package com.sinosoft.ms.test;

public class ReflectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Class<ReflectDemo> c = ReflectDemo.class;
		
		c.getMethod("test1", Object.class).invoke(new ReflectDemo(), "111");
		
		//c.getMethod("test2", Object[].class).invoke(new ReflectDemo(), (Object[])new String[]{"111"});
		
		c.getMethod("test2", Object[].class).invoke(new ReflectDemo(), new Object[]{new Object[]{""}});

	}

}

class ReflectDemo {
	public void test1(Object obj) {
		System.out.println("test1...." + obj);
	}
	public void test2(Object[] obj) {
		System.out.println("test2....");
	}
}
