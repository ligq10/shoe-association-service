package com.ligq.shoe.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringSubUtils {

	public static String subStringChapterTitle(String teamString){
		 String titleStr = "";   
		 String regex = "\\[(.*)\\]";
	     Pattern pattern = Pattern.compile(regex);
	     Matcher matcher = pattern.matcher(teamString);
	     if(matcher.find()){
	    	 titleStr = matcher.group(1);	    	   
	     }
	     return titleStr;
	}
	
	public static String subStringChapterFileName(String teamString){
		 String titleStr = "";   
		 String regex = "\\((.*)\\)";		 
	     Pattern pattern = Pattern.compile(regex);
	     Matcher matcher = pattern.matcher(teamString);
	     if(matcher.find()){
	    	 titleStr = matcher.group(1);	    	   
	     }
	     return titleStr;
	}
	
	public static int subStringChapterNo(String teamString){
		 int chapterNo = 0;
		 String chapterNoStr = "";   
		 String regex = "chapter(.*)\\.md";		 
	     Pattern pattern = Pattern.compile(regex);
	     Matcher matcher = pattern.matcher(teamString);
	     if(matcher.find()){
	    	 chapterNoStr = matcher.group(1);	    	   
	     }
	     if(StringUtils.isEmpty(chapterNoStr)){
	    	 return 0;
	     }
	     chapterNo = Integer.valueOf(chapterNoStr);
	     return chapterNo;
	}
	
}
