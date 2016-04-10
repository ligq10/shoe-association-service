/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var shoecompanyUpdateControllers=angular.module('shoecompanyUpdateControllers',['shoecompanyUpdateServices']);

shoecompanyUpdateControllers.controller('shoecompanyUpdateCtrl',['$scope','$stateParams','loginSession','$timeout','$state','$upload','shoecompanyUpdateFactory',
    function($scope,$stateParams,loginSession,$timeout,$state,$upload,shoecompanyUpdateFactory) {
	$scope.shoeCompanyId = $stateParams.uuid;
	$scope.shoeComapny ={};
	var loginUser = loginSession.loginUser().userInfo;
	$scope.tempLogoImageId = "";
	$scope.tempPermitImageId = "";

	$scope.result = "pass";	
	shoecompanyUpdateFactory.getShoeComapnyDetailById({uuid:$scope.shoeCompanyId},function(response){
    	
    	if(response.$resolved){
    		$scope.shoeComapny = response;
    		$scope.logoImageId = response.permitImageId;
    		$scope.permitImageId = response.permitImageId;
    	}
	});
	
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
	
	
	$scope.saveShoeCompany = function(){

		if($scope.tempLogoImageId != "" && $scope.tempLogoImageId != null){
			$scope.logoImageId =$scope.tempLogoImageId;
		}
		
		if($scope.tempPermitImageId != "" && $scope.tempPermitImageId != null){
			$scope.permitImageId =$scope.tempPermitImageId;
		}
		var postEntity={};
		postEntity = $scope.shoeComapny;
		postEntity.logoImageId = $scope.logoImageId;
		postEntity.permitImageId = $scope.permitImageId;
		shoecompanyUpdateFactory.updateShoeCompanyById({uuid:$scope.shoeCompanyId},postEntity,function(response){				
			if(response.$resolved){
				$state.go('shoecompanylist');
				Message.alert({
					msg : "保存成功!",
					title : "提示:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
				
			}else{
				Message.alert({
					msg : "保存失败!",
					title : "警告:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
			}
	 	 },function(error){	
				Message.alert({
					msg : "保存失败!",
					title : "警告:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
	 	 });
	}
	
	 $scope.onFileSelect = function(type,$files) {
		 
		//$files: an array of files selected, each file has name, size, and type.
		var $file = $files[0];	
		if($file == undefined || $file == null){
			return false;
		}		
		$scope.upload=$upload.upload({
	        url:'/images',
	        method:'POST',
	        header:{
	            "Content-Type":"multipart/form-data"
	        },
	        //data:postEntity
	        file:$file
	    }).success(function(response, status, headers, config){
	            if(status==201){	            	
	            	if(type == "logoimage"){
	            		$scope.tempLogoImageId = response.uuid;
	            		$scope.shoeComapny.logoImageUrl = "/images/show/"+$scope.tempLogoImageId;
	            	}else{
		            	$scope.tempPermitImageId = response.uuid;
		            	$scope.shoeComapny.permitImageUrl = "/images/show/"+$scope.tempPermitImageId;
	            	}
	            }
	    });
	 }
}]);