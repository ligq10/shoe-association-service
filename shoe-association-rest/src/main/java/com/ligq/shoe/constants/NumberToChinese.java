package com.ligq.shoe.constants;

public enum NumberToChinese {
	ZERO(0,"零"),
	ONE(1,"一"),
	TWO(2,"二"),
	THREE(3,"三"),
	FOUR(4,"四"),
	FIVE(5,"五"),
	SIX(6,"六"),
	SEVEN(7,"七"),
	EIGHT(8,"八"),
	NINE(9,"九");
	
	private final int value;

	private final String chinese;
	
	private NumberToChinese(int value, String chinese) {
		this.value = value;
		this.chinese = chinese;
	}

	public int getValue() {
		return value;
	}

	public String getChinese() {
		return chinese;
	}

	/*
	 * 根据活动状态的值获取枚举对象。 
	 * 
	 * @param vale 活动状态的值 
	 * @return 枚举对象 
	 */
	public static NumberToChinese getNumberToChineseByValue(int value){
		for(NumberToChinese numberToChinese : NumberToChinese.values()){
			if(value == numberToChinese.getValue()){
				return numberToChinese;
			}
		}
		throw new IllegalArgumentException("value值非法，没有符合的枚举对象");
	}
}
