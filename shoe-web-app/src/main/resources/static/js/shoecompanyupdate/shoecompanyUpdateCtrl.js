/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var shoecompanyUpdateControllers=angular.module('shoecompanyUpdateControllers',['shoecompanyUpdateServices']);

shoecompanyUpdateControllers.controller('shoeAddCtrl',['$scope','$timeout','$state','$upload','shoecompanyUpdateFactory',
    function($scope,$timeout,$state,$upload,shoecompanyUpdateFactory) {
	
	$scope.logoImageId = "";
	$scope.permitImageId = "";
	
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
		
	var submit = function(){
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