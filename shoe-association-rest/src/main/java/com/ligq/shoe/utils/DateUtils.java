package com.ligq.shoe.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String composeUTCTime(Long date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		return dateFormat.format(date);
	}
	
	public static Date composeUTCTime(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		Date date;
		try {
			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			date = null;
		}
		return date;
	}
	
	public static String composeUTCTime(Date date) {
		if(date==null){
			return null;
		}
		Long lDate=date.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		
		return dateFormat.format(lDate);
	}
	
	/**
	 * 
	 * @param String date
	 * @return Date yyyy-MM-dd
	 */
	public static String composeUTCDate(String dateString) {
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		String date;
		try {
			date = sdf.format(sdf.parse(dateString));
		} catch (Exception e) {
			date = null;
		}
		return date;
	}
}

