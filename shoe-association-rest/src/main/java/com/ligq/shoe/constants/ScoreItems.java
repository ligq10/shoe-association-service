package com.ligq.shoe.constants;

import org.springframework.util.StringUtils;

/**
 * 审核结果类型
 * @author ligq
 *
 */
public enum ScoreItems {

	CREDIT_SCORE("creditScore","诚信分"),
	QUALITY_SCORE("qualityScore","产品质量分"),
	SERVE_SCORE("serveScore","服务分");
	
	private String value;
	private String desc;
	
	private ScoreItems(String value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	
	public static ScoreItems getScoreItemsByValue(String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}
		
		for(ScoreItems scoreItem : ScoreItems.values()){
			if(value == scoreItem.getValue()){
				return scoreItem;
			}
		}
		
	    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
	}
}
