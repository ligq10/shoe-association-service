package com.ligq.shoe.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.ligq.shoe.config.SendMsgConfig;
import com.ligq.shoe.constants.SendMsgResponseFlag;
import com.ligq.shoe.model.SendMsg;
import com.ligq.shoe.model.SendMsgProperties;

public class SendMsgUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SendMsgUtil.class); 

	
    /**
     * 发送短信消息
      * 方法说明
      * @Discription:扩展说明
      * @param phones
      * @param content
      * @return
      * @return String
      * @Author: ligq
      * @Date: 2015年12月12日  下午7:18:08
      * @ModifyUser：ligq
      * @ModifyDate: 2015年12月12日  下午7:18:08
     */
    public static ResponseEntity<?> sendMsg(String url,RestTemplate restTemplate){

    	// HttpHeaders headers = new HttpHeaders();
    	// headers.setContentType(MediaType.APPLICATION_JSON);
    	   
    	 HttpEntity<String> entity = new HttpEntity<String>("");
    	 ResponseEntity<String> responseBody =null;
		try {
			responseBody = restTemplate.postForEntity(url, entity, String.class);
			HttpStatus responseStatus = responseBody.getStatusCode();
			if(responseStatus.equals(HttpStatus.OK)){
				String result = responseBody.getBody();
				try {
					Integer resultValue = Integer.valueOf(result);
					String desc = SendMsgResponseFlag.getSendMsgResponseFlagByValue(resultValue).getDesc();
					return new ResponseEntity<String>(desc,HttpStatus.OK);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					return new ResponseEntity<String>(result,HttpStatus.OK);
				}
			}else{
				return new ResponseEntity<String>("验证码发送失败",HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(),e);
			return new ResponseEntity<String>("验证码发送失败",HttpStatus.INTERNAL_SERVER_ERROR);
		}

    }
 
    /**
     * 随机生成6位随机验证码
      * 方法说明
      * @Discription:扩展说明
      * @return
      * @return String
      * @Author: ligq
      * @Date: 2015年12月12日  下午7:19:02
      * @ModifyUser：ligq
      * @ModifyDate: 2015年12月12日  下午7:19:02
     */
    public static String createRandomVcode(){
        //验证码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
 
    /**
     * 测试
      * 方法说明
      * @Discription:扩展说明
      * @param args
      * @return void
      * @Author: ligq
      * @Date: 2015年12月12日 下午7:26:36
      * @ModifyUser：ligq
      * @ModifyDate: 2015年12月12日  下午7:26:36
     */
/*    public static void main(String[] args) {
        System.out.println(sendMsg("18123456789,15123456789", "尊敬的用户，您的验证码为" + SendMsgUtil.createRandomVcode() + "，有效期为60秒，如有疑虑请详询400-069-2886（客服电话）【XXX中心】"));
    }*/
}
