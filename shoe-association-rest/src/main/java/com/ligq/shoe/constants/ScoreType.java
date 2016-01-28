package com.ligq.shoe.constants;

import org.springframework.util.StringUtils;

/**
 * 评分类型
 * @author ligq
 *
 */
public enum ScoreType {

	PLUS("plus","加分"),
	REDUCE("reduce","减分");
	
	private String value;
	private String desc;
	
	private ScoreType(String value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	public static ScoreType getScoreTypeByValue(String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}
		
		for(ScoreType scoreType : ScoreType.values()){
			if(value.equals(scoreType.getValue())){
				return scoreType;
			}
		}
		
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
