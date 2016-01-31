/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var shoeAddControllers=angular.module('shoeAddControllers',['shoeAddServices']);

shoeAddControllers.controller('shoeAddCtrl',['$scope','$timeout','$state','$upload','shoeAddFactory',
    function($scope,$timeout,$state,$upload,shoeAddFactory) {
	
	$scope.letterList=['A','B','C','D','E','F','G','H','I','J','K','L',
	               'M','N','O','P','Q','R','S','T','U','V','W','X',
	               'Y','Z'];
	$scope.creditList = [
	                     {level:'0',desc:"优"},
	                     {level:'1',desc:"良"},
	                     {level:'2',desc:"中"},
	                     {level:'3',desc:"差"}
	                     ];
	$scope.sortList = [
	                   {value:"asc",desc:"升序"},
	                   {value:"desc",desc:"降序"}
	                   ];
	
	$scope.checkCodeTitle = "获取验证码";
	$scope.isValidCheckCodeButton = true;
	
	$scope.logoImageId = "";
	$scope.permitImageId = "";
	var i =60; //验证码重新获取
	var checkmobile = function (string) { 
		if(string == null || string == undefined || string == ''){
			return false;
		}
	    var pattern = /^1[34578]\d{9}$/;  
	    if (pattern.test(string)) {  
	        return true;  
	    }   
	    return false;  
	}; 
	
	
	var updateTime = function(){
		
		i--;
		$scope.checkCodeTitle = "距离下次获取还有" + i + "秒";
		if(i>0){
			$timeout(function(){
				updateTime();
			},1000);
		}else{
			$scope.isValidCheckCodeButton = false;
			$scope.checkCodeTitle = "获取验证码";
			i =60;
		}
	}
	
	var submit = function(){
  		if($scope.name == null || $scope.name == undefined || $scope.name == ''){
			Message.alert({
				msg : "企业名称不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		if($scope.enterpriseLegalPerson == null || $scope.enterpriseLegalPerson == undefined || $scope.enterpriseLegalPerson == ''){
			Message.alert({
				msg : "企业法人不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		var postEntity={};
		postEntity.name=$scope.name;
		postEntity.address=$scope.address;
		postEntity.enterpriseLegalPerson=$scope.enterpriseLegalPerson;
		postEntity.submitPerson=$scope.submitPerson;
		postEntity.tel=$scope.tel;
		postEntity.logoImageId = $scope.logoImageId;
		postEntity.permitImageId = $scope.permitImageId;
		postEntity.checkCode = $scope.checkCode;
		shoeAddFactory.saveShoeCompany(postEntity,function(response){				
			if(response.$resolved){
				$state.go('shoeList');
				Message.alert({
					msg : "新增成功!",
					title : "提示:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
				
			}else{
				Message.alert({
					msg : "新增失败!",
					title : "警告:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
			}
	 	 },function(error){	
				Message.alert({
					msg : "新增失败!",
					title : "警告:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
	 	 });
	}
	
	var logoFileUpload = function(logoimage){
		if(logoimage == null || logoimage == undefined){
			Message.alert({
				msg : "企业形象照为空!",
				title : "警告:",
				btnok : '确定',
				btncl : '取消'
			}, "warn", "small");
			return false;
		}

		$scope.upload=$upload.upload({
	        url:'/images',
	        method:'POST',
	        header:{
	            "Content-Type":"multipart/form-data"
	        },
	        //data:postEntity
	        file:logoimage
	    }).success(function(response, status, headers, config){
	            if(status==201){
            		$scope.logoImageId  = response.uuid;
            		submit();
	            }else{
	    	    	Message.alert({
	    			    msg: "图片上传失败！",
	    		    	title:"警告提示",
	    		    	btnok: '确定',
	    		    	btncl:'取消'
	    	      	},"warn","small");
	            }
	    }).error(function(data){
	    	Message.alert({
			    msg: "图片上传失败！",
		    	title:"警告提示",
		    	btnok: '确定',
		    	btncl:'取消'
	      	},"warn","small");
	    })
		
	}
	
	var fileUpload = function(permitimage,logoimage){
		
		if(permitimage == null || permitimage == undefined){
			Message.alert({
				msg : "营业执照为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
			return false;
		}

		$scope.upload=$upload.upload({
	        url:'/images',
	        method:'POST',
	        header:{
	            "Content-Type":"multipart/form-data"
	        },
	        //data:postEntity
	        file:permitimage
	    }).success(function(response, status, headers, config){
	            if(status==201){
            		$scope.permitImageId = response.uuid;
            		logoFileUpload(logoimage);
	            }else{
	    	    	Message.alert({
	    			    msg: "图片上传失败！",
	    		    	title:"警告提示",
	    		    	btnok: '确定',
	    		    	btncl:'取消'
	    	      	},"warn","small");
	            }
	    }).error(function(data){
	    	Message.alert({
			    msg: "图片上传失败！",
		    	title:"警告提示",
		    	btnok: '确定',
		    	btncl:'取消'
	      	},"warn","small");
	    })
	}
	
	$scope.checkTel = function(){
		var result = checkmobile($scope.tel);
		if(result){
			$scope.isValidCheckCodeButton = false;
		}else{
			$scope.isValidCheckCodeButton = true;
		}
	}
	
	$scope.getCheckCode = function(){
		$scope.isValidCheckCodeButton = true;
		var postEntity = {
				mobile:$scope.tel,
				type:"1"
		}
		shoeAddFactory.getCheckCode(postEntity,function(response){
			
			if(response.$resolved){
		        // 1秒后显示  
		        updateTime();
			}else{
				Message.alert({
					msg : "获取验证码失败!",
					title : "警告:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
			}
		},function(error){	
			Message.alert({
				msg : "获取验证码失败!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
 	    });       
	}	
	
	
	$scope.submitShoeCompany = function(){
		var permitimage = $scope.permitimage[0];
		var logoimage = $scope.logoimage[0];
		fileUpload(permitimage,logoimage);
	}
		
}]);