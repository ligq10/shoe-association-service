/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var backgroundFeedBackAddControllers=angular.module('backgroundFeedBackAddControllers',['backgroundFeedBackAddServices']);

backgroundFeedBackAddControllers.controller('backgroundFeedBackAddCtrl',['$scope','$timeout','$state','$stateParams','$upload','$rootScope','backgroundFeedBackAddFactory',
    function($scope,$timeout,$state,$stateParams,$upload,$rootScope,backgroundFeedBackAddFactory) {
    	
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
        
	$scope.score = 0;
	
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
		backgroundFeedBackAddFactory.saveFeedback({uuid: $scope.shoeCompanyId},postEntity,function(response){				
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
	
}]);
