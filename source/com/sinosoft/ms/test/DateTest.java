package com.sinosoft.ms.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTest {
	public static void main (String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String startDate = format.format(cal.getTime());
		cal.add(Calendar.DATE, 7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String endDate = format.format(cal.getTime());
		
		System.out.println(startDate);
		System.out.println(endDate);
		
		System.out.println(String.class.getSimpleName());
		
		
		System.out.println(Integer.class.isPrimitive());
		System.out.println(new Integer(1).getClass() == Integer.class);
		System.out.println(new String[1].getClass());
		
	}
}
