/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var feedBackAddControllers=angular.module('feedBackAddControllers',['feedBackAddServices']);

feedBackAddControllers.controller('feedBackAddCtrl',['$scope','$timeout','$state','$stateParams','$upload','$rootScope','feedBackAddFactory',
    function($scope,$timeout,$state,$stateParams,$upload,$rootScope,feedBackAddFactory) {
    $scope.shoeComapnyId = $stateParams.uuid;
	
    $scope.scoreTypeList = [
            {
            	scoreValue:'plus',
            	scoreDesc:'加分'
            },
            {
            	scoreValue:'reduce',
            	scoreDesc:'减分'
            }
    ];
    
	$scope.checkCodeTitle = "获取验证码";
	$scope.isValidCheckCodeButton = true;
    
	$scope.score = 0;
	
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
	
	$scope.plusScore = function(){
		if($scope.score < 20){
			$scope.score = $scope.score+1;
		}
	}
	
	$scope.reduceScore = function(){
		if($scope.score >= 1){
			$scope.score = $scope.score-1;
		}
	}
	
	$scope.checkTel = function(){
		var result = checkmobile($scope.submitTel);
		if(result){
			$scope.isValidCheckCodeButton = false;
		}else{
			$scope.isValidCheckCodeButton = true;
		}
	}
	
	var submit = function(proofFileIds){
  		if($scope.scoreType == null || $scope.scoreType == undefined || $scope.scoreType == ''){
			Message.alert({
				msg : "请设置评分类型!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		if($scope.score == null || $scope.score == undefined || $scope.score < 1){
			Message.alert({
				msg : "评分分数必须大于0!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		if($scope.scoreReason == null || $scope.scoreReason == undefined || $scope.scoreReason == ''){
			Message.alert({
				msg : "评分事由不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		if($scope.submitPerson == null || $scope.submitPerson == undefined || $scope.submitPerson == ''){
			Message.alert({
				msg : "提交者姓名不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		if($scope.submitTel == null || $scope.submitTel == undefined || $scope.submitTel == ''){
			Message.alert({
				msg : "提交者手机不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		var postEntity={};
		postEntity.scoreType=$scope.scoreType;
		postEntity.score=$scope.score;
		postEntity.scoreReason=$scope.scoreReason;
		postEntity.submitPerson=$scope.submitPerson;
		postEntity.submitTel=$scope.submitTel;
		postEntity.proofFileIds = proofFileIds;
		postEntity.checkCode = $scope.checkCode;
		feedBackAddFactory.saveFeedback({uuid: $scope.shoeComapnyId},postEntity,function(response){				
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
	
	var fileUpload = function(files){
		$scope.fileIds = [];
		if(files == null || files == undefined || files.length == 0){
			Message.alert({
				msg : "评论证据为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
			return false;
		}

		$scope.upload=$upload.upload({
	        url:'/images/multipartfile',
	        method:'POST',
	        header:{
	            "Content-Type":"multipart/form-data"
	        },
	        //data:postEntity
	        file:files
	    }).success(function(response, status, headers, config){
	            if(status==201){
            		$scope.fileIds = response;
            		submit($scope.fileIds);
	            }else{
	    	    	Message.alert({
	    			    msg: "评论证据上传失败！",
	    		    	title:"警告提示",
	    		    	btnok: '确定',
	    		    	btncl:'取消'
	    	      	},"warn","small");
	            }
	    }).error(function(data){
	    	Message.alert({
			    msg: "评论证据上传失败！",
		    	title:"警告提示",
		    	btnok: '确定',
		    	btncl:'取消'
	      	},"warn","small");
	    })
	}

	$scope.submitFeedback = function(){
		var proofFiles = $scope.proofFile;
		fileUpload(proofFiles);
	}
	
	$scope.getCheckCode = function(){
		$scope.isValidCheckCodeButton = true;
		var postEntity = {
				mobile:$scope.submitTel,
				type:"2"
		}
		feedBackAddFactory.getCheckCode(postEntity,function(response){
			
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

}]);



/*feedBackAddControllers.directive("deletegroupdialog",
    function (){
        var option={
            restrict:"AEC",
            transclude:true,
            replace:true,
            templateUrl:"templates/commonTemplate/delete-group-dialog.html"
        };
        return option;
    });*/