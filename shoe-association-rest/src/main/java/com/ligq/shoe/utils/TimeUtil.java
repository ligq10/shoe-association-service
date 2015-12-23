package com.ligq.shoe.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat();
	public static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String formatDateToStringWithStyle(Date date, int style) {
		String strDate = "";
		if(date==null){
			return strDate;
		}
		
		switch (style) {
		case 1:
			strDate = simpleDateFormat1.format(date);
			break;
		case 2:
			strDate = simpleDateFormat2.format(date);
			break;
		default:
			strDate = simpleDateFormat0.format(date);

		}
		return strDate;
	}
}
