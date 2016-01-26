package com.ligq.shoe.utils;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Utils{
	
	/**
	 * 将Json对象转换成Map
	 */
	public static Map<String, String> jsonToMap(String data){
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		Map<String, String> map = g.fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
		return map;
	}
	
}