package com.ligq.shoe;

import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ShoeTest {

	@Test
	public void save(){
/*		String str = "长";
        System.out.println("小写输出：" + Pinyin4jUtil.getPinyinToLowerCase(str));  
        System.out.println("大写输出：" + Pinyin4jUtil.getPinyinToUpperCase(str));  
        System.out.println("首字母大写输出：" + Pinyin4jUtil.getPinyinFirstToUpperCase(str));  
        System.out.println("简拼输出：" + Pinyin4jUtil.getFirstPinyinAndUpperCase(str)); 
		System.out.println("O".toUpperCase());
		System.out.println(isNumber("长"));
		System.out.println(isNumber("1"));
    	String chineseNumber = NumberToChinese.getNumberToChineseByValue(Integer.valueOf("1")).getChinese();

		System.out.println(chineseNumber);
    	String firstPinyin = Pinyin4jUtil.getFirstPinyinAndUpperCase(chineseNumber);;
		System.out.println(firstPinyin);*/

	}
	
	public boolean isNumber(String str){
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();  
	}
}
