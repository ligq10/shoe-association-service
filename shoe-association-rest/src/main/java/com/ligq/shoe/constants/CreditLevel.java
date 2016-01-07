package com.ligq.shoe.constants;

public enum CreditLevel {

	BEST(0,"优"),
	GENERAL(1,"良"),
	MEDIUM(2,"中"),
	INFERIOR(3,"差");
	
	private final int value;

	private final String desc;

	private CreditLevel(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	/**
	 * Return the integer value of this credit level.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Return the description of this credit level.
	 */
	public String getDesc() {
		return desc;
	}
/*
   * 根据活动状态的值获取枚举对象。 
   * 
   * @param vale 活动状态的值 
   * @return 枚举对象 
   */
  public static CreditLevel getCreditDesc(int value) { 
	CreditLevel[] allCredit = CreditLevel.values(); 
    for (CreditLevel credit : allCredit) { 
      if (credit.getValue()==(value)) { 
        return credit; 
      } 
    } 
    throw new IllegalArgumentException("value值非法，没有符合的枚举对象"); 
  } 

}
