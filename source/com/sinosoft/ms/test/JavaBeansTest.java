package com.sinosoft.ms.test;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;


public class JavaBeansTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BeanInfo bif = Introspector.getBeanInfo(PrpDuser.class);
	       PropertyDescriptor[] pd = bif.getPropertyDescriptors();
	       //System.out.println(bif.getDefaultPropertyIndex());
	       for(int i=0;i<pd.length;i++){
	            System.out.println("name:"+pd[i].getName());
	            System.out.println("read method name:"+pd[i].getReadMethod().getName());
	           System.out.println("displayname:"+pd[i].getDisplayName());
	           System.out.println("WriteMethod():"+pd[i].getWriteMethod());
	        }
	   MethodDescriptor[] md = bif.getMethodDescriptors();
	    System.out.println("111111***********************************");
	    for(int m=0;m<md.length;m++){
	        System.out.println("name:"+md[m].getName());
	        System.out.println("method name:"+md[m].getMethod().getName());
	        System.out.println("displayname:"+md[m].getDisplayName());
	     }
	   System.out.println("2222222222***********************************");
	   BeanDescriptor bd = bif.getBeanDescriptor();
	   System.out.println("display name：" + bd.getDisplayName());
	    System.out.println("name：" + bd.getName());
	    //System.out.println("CustomizerClass name " +bd.getCustomizerClass  ().getName());
	     EventSetDescriptor[] esd = bif.getEventSetDescriptors();
	     for(int m=0;m<esd.length;m++){
	          System.out.println("name:"+esd[m].getName());
	          System.out.println("method name:"+esd[m].getGetListenerMethod().getName());
	            System.out.println("method name:"+esd[m].getRemoveListenerMethod().getName());
	            System.out.println("method name:"+esd[m].getAddListenerMethod().getName());
	            System.out.println("displayname:"+esd[m].getDisplayName());
	  }

	}

}