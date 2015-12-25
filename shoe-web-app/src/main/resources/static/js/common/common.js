// 定义系统全局变量
// 服务器api的访问端口
// 默认pageSize=10

//分页
var  PAGESIZE_DEFAULT=20;
var  PAGESIZE_GROUP=15;
var CURRENTPAGE_INIT=0;
var SAVE_SUCCESS_MESSAGE="保存成功！";

// 接口请求异常处理方法
var dealAbnormalResponse=function(errorInfo){
    switch (errorInfo.status){
        case 401:
            //alert("无权限，请先登录");
            window.location.href="#/login";
            break;
        case 500:
            alert("系统错误！");
            window.location.href="#/home";
            break;
        case 400:
            var message='';
            for (var i=0;i<errorInfo.data.errors.length;i++){
                message+=errorInfo.data.errors[i].message+"\n";
            }
            alert(message);
            break;
    }
};

 /** <summary>
  *限制只能输入数字
  *</summary>
  */
 $.fn.onlyNum = function () {
     $(this).keypress(function (event) {
         var eventObj = event || e;
         var keyCode = eventObj.keyCode || eventObj.which;
         if ((keyCode >= 48 && keyCode <= 57) || keyCode == 8)
             return true;
         else
             return false;
     }).focus(function () {
     //禁用输入法
         this.style.imeMode = 'disabled';
     }).bind("paste", function () {
     //获取剪切板的内容
         var clipboard = window.clipboardData.getData("Text");
         if (/^\d+$/.test(clipboard))
             return true;
         else
             return false;
     });
 };
 
  /** <summary>
  * 限制只能输入字母
  * </summary>
  */
  $.fn.onlyAlpha = function () {
      $(this).keypress(function (event) {
          var eventObj = event || e;
          var keyCode = eventObj.keyCode || eventObj.which;
         if ((keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122))
             return true;
         else
             return false;
     }).focus(function () {
         this.style.imeMode = 'disabled';
     }).bind("paste", function () {
         var clipboard = window.clipboardData.getData("Text");
         if (/^[a-zA-Z]+$/.test(clipboard))
             return true;
         else
             return false;
     });
 };
 
 /* <summary>
 * 限制只能输入数字和字母
 * </summary>
 */
 $.fn.onlyNumAlpha = function () {
     $(this).keypress(function (event) {
         var eventObj = event || e;
         var keyCode = eventObj.keyCode || eventObj.which;
         if ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122))
             return true;
         else
             return false;
     }).focus(function () {
         this.style.imeMode = 'disabled';
     }).bind("paste", function () {
         var clipboard = window.clipboardData.getData("Text");
         if (/^(\d|[a-zA-Z])+$/.test(clipboard))
             return true;
         else
             return false;
     });
 };
 
 $.fn.inputLimit=function(min,max){
     $(this).blur(function (event) {
    	 var inputValue = $(this).val();
         if (parseInt(inputValue) >= min && parseInt(inputValue) <= max)
        	 return true;
         else
             $(this).val("");
             $(this).focus();
            // $.alert("请输入"+min+"~"+max+"范围间的正整数！","输入限制提示","warn");
        	Message.alert(
        		{
 	   		    	msg: "请输入"+min+"~"+max+"范围间的正整数！",
 	 		    	title:"输入限制提示",
 	 		    	btnok: '确定',
 	 		    	btncl:'取消'
 	            },"warn","small");
             return false;
     }).focus(function () {   	 
         this.style.imeMode = 'disabled';
     });
 }

 /**
  * 封装公共alert、confirm消息
  * author:liguangqiang
  * alert使用方式：
  *     四个选项都是可选参数
  *	    Message.alert(
  *			  {
  *			    msg: '内容',
  *			    title: '标题',
  *			    btnok: '确定',
  *			    btncl:'取消'
  *		      },
  *		      'success'
  *	    );
  * 
  * confirm使用方式：
  *   如需增加回调函数，后面直接加 .on( function(e){} );
  *	  点击"确定" e: true
  *	  点击"取消" e: false
  *	  Message.confirm(
  *		  {
  *		    msg: "是否删除角色？"
  *		  })
  *		 .on( function (e) {
  *		    alert("返回结果：" + e);
  *	   }); 
  */
 window.Message = function (){
    var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
    var alr = $("#alert_dialog");
    var ahtml = alr.html();
    /**
     * alert消息封装
     * options:提醒消息对象依次包含提醒标题(title)、提醒内容(content)、ok按钮名称、cancel按钮名称(alert消息时不显示)
     * type:提醒消息类型
     */
    var _alert = function (options,type,WindowResizer) {
	      alr.html(ahtml);	// 复原
	      $("#dialog_ok_button").removeClass('btn-primary').addClass('btn-primary');
	      $("#dialog_cancel_button").hide();
	      //设置窗口样式
/*	      if(type == null || type == '' || type == undefined || type == 'success'){
	    	  $("#dialog_header").removeClass("dialog_warn,dialog_error").addClass("dialog_success");
	      }else if(type == 'warn'){
	    	  $("#dialog_header").removeClass("dialog_success,dialog_error").addClass("dialog_warn");
	      }else if(type == 'error'){
	    	  $("#dialog_header").removeClass("dialog_warn,dialog_success").addClass("dialog_error");
	      }else{
	    	  $("#dialog_header").removeClass("dialog_warn,dialog_error").addClass("dialog_success");
	      }*/
	      //设置窗口大小
	      if(WindowResizer == null || WindowResizer == '' || WindowResizer == undefined ){
	    	  $('#alert_dialog').removeClass("bs-example-modal-lg").addClass("bs-example-modal-sm");
	    	  $('#modal_dialog').removeClass("modal-lg").addClass("modal-sm");
	      }else if(WindowResizer == 'big'){
	    	  $('#alert_dialog').removeClass("bs-example-modal-sm").addClass("bs-example-modal-lg");
	    	  $('#modal_dialog').removeClass("modal-sm").addClass("modal-lg");	    	  
	      }else if(WindowResizer == 'general'){
	    	  $('#alert_dialog').removeClass("bs-example-modal-sm,bs-example-modal-lg");
	    	  $('#modal_dialog').removeClass("modal-sm,modal-lg");	    	  	    	  
	      }else{
	    	  $('#alert_dialog').removeClass("bs-example-modal-lg").addClass("bs-example-modal-sm");
	    	  $('#modal_dialog').removeClass("modal-lg").addClass("modal-sm");
	      }
	      _dialog(options);
	      return {
		        on: function (callback) {
			          if (callback && callback instanceof Function) {
			        	  $("#dialog_ok_button").click(function () { callback(true) });
			          }
		        }
	      };
    };

    var _confirm = function (options) {
      alr.html(ahtml); // 复原
      $("#dialog_ok_button").removeClass('btn-primary').addClass('btn-primary');
      $("#dialog_cancel_button").show();
      _dialog(options);

      return {
        on: function (callback) {
          if (callback && callback instanceof Function) {
        	  $("#dialog_ok_button").click(function () { callback(true) });
        	  $("#dialog_cancel_button").click(function () { callback(false) });
          }
        }
      };
    };

    var _dialog = function (options) {
	      var ops = {
	        msg: "提示内容",
	        title: "操作提示",
	        btnok: "确定",
	        btncl: "取消"
	      };
	
	      $.extend(ops, options);
	      var html = alr.html().replace(reg, function (node, key) {
	         return {
		          Title: ops.title,
		          Message: ops.msg,
		          BtnOk: ops.btnok,
		          BtnCancel: ops.btncl
	         }[key];
	      });
	      
	      alr.html(html);
	      alr.modal({
	        width: 500,
	        backdrop: 'static'
	      });
	    }
	    return {
	      alert: _alert,
	      confirm: _confirm
	    }
  }();

  /**
   * 判断数组中是否包含元素
   */
  Array.prototype.contains = function(e)  
  {  
	  for(i=0;i<this.length;i++)  
	  {  
		  if(this[i] == e)  
		  return true;  
	  }  
	  return false;  
  }  
  
  /**
   * 添加toggle方法。jquery 1.9 以上（含）去除了toggle方法
   */
  $.fn.toggle = function( fn ) {
    var args = arguments,
            guid = fn.guid || jQuery.guid++,
            i = 0,
            toggler = function( event ) {
                var lastToggle = ( jQuery._data( this, "lastToggle" + fn.guid ) || 0 ) % i;
                jQuery._data( this, "lastToggle" + fn.guid, lastToggle + 1 );
                event.preventDefault();
                return args[ lastToggle ].apply( this, arguments ) || false;
            };

    toggler.guid = guid;
    while ( i < args.length ) {
        args[ i++ ].guid = guid;
    }
    return this.click( toggler );
}


// 验证是否是6-15位字符
var value_string_is_right=function(validated_value){
    return !(validated_value==undefined||validated_value.length>15||validated_value.length<4);
};
// 验证是否是5-12数字
var value_num_is_right=function(validated_value){
    return !(validated_value==undefined||validated_value.length>12||validated_value.length<5||isNaN(validated_value));
};
var value_num_eleven_is_right=function(validated_value){
    return !(validated_value==undefined||validated_value.length!=11||isNaN(validated_value));
};

/**
 *清空数据,页面所有input和textarea框数据，但不包含readonly中的数据 
 */
function resetData(){
	$("input[readonly!='readonly']").each(function() {
		$(this).val("");
	});
	$("textarea").val("");
}

function clearCookiesAndLogout(){
    deleteAllCookies();
    $("#logoutForm").submit();
}

function deleteAllCookies() {
	var cookies = document.cookie.split(";");
	for (var i = 0; i < cookies.length; i++) {
		var cookie = cookies[i];
		var eqPos = cookie.indexOf("=");
		var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
		document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
	}
}

Date.prototype.format = function(format)  
{  
/* 
* format="yyyy-MM-dd hh:mm:ss"; 
*/  
var o = {  
"M+" : this.getMonth() + 1,  
"d+" : this.getDate(),  
"H+" : this.getHours(),  
"m+" : this.getMinutes(),  
"s+" : this.getSeconds(),  
"q+" : Math.floor((this.getMonth() + 3) / 3),  
"S" : this.getMilliseconds()  
}  
  
if (/(y+)/.test(format))  
{  
format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
- RegExp.$1.length));  
}  
  
for (var k in o)  
{  
if (new RegExp("(" + k + ")").test(format))  
{  
format = format.replace(RegExp.$1, RegExp.$1.length == 1  
? o[k]  
: ("00" + o[k]).substr(("" + o[k]).length));  
}  
}  
return format;  
}
