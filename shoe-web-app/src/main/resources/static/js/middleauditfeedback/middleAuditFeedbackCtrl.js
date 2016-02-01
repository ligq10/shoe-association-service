/**
 * Created by liguangqiang@changhongit.com 2015-05-15.
 */
'use strict';
var middleAuditFeedbackControllers=angular.module('middleAuditFeedbackControllers',['middleAuditFeedbackServices']);

/**
 * 人员列表
 */
middleAuditFeedbackControllers.controller('middleAuditFeedbackCtrl',['$scope','$stateParams','$state','loginSession','middleAuditFeedbackFactory',
    function($scope,$stateParams,$state,loginSession,middleAuditFeedbackFactory){
	var loginUser = loginSession.loginUser().userInfo;
	$scope.result = "pass";
	$scope.feedbackId = $stateParams.uuid;
	$scope.proofImageUrlListIndex=[];
	$scope.auditscore = 0;//审核评分

	$scope.primaryAudit = {
			result:"",
			scoreItem : "",
			scoreType : "",
			score:""			
	};
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
    $scope.scoreItemList = [
                            {
                            	scoreCode:'creditScore',
                            	scoreDesc:'诚信分'
                            },
                            {
                            	scoreCode:'qualityScore',
                            	scoreDesc:'产品质量分'
                            },{
                            	scoreCode:'serveScore',
                            	scoreDesc:'服务分'
                            }
                            ];
	// 轮播图数据初始化
	$scope.slides =[];
/*    $scope.slides =[
                    "http://img02.tooopen.com/images/20160125/tooopen_sy_155386511951.jpg",
                    "http://img02.tooopen.com/images/20151231/tooopen_sy_153270994272.jpg"
                             
                    ];*/
	middleAuditFeedbackFactory.getFeedbackDetailById({uuid:$scope.feedbackId},function(response){    	
    	if(response.$resolved){
    		$scope.feedback = response;
    		
    		if(response.proofImageUrls != null && response.proofImageUrls.length >0){
    			$scope.slides = response.proofImageUrls;
    		}
    	}
	});

	middleAuditFeedbackFactory.getAuditMessagesByFeedbackId({uuid:$scope.feedbackId},function(response){    	
    	if(response.$resolved && response._embedded != undefined){
    		for(var i=0 ; i< response._embedded.auditMessageResponses.length;i++){
    			if(response._embedded.auditMessageResponses[i].auditStatusValue  == 1){
    				$scope.primaryAudit = response._embedded.auditMessageResponses[i];
    				$scope.primaryAudit.auditResult =response._embedded.auditMessageResponses[i].auditResult == "pass"?"通过":"不通过";
    				if(response._embedded.auditMessageResponses[i].scoreItem == "creditScore"){
    					$scope.primaryAudit.scoreItem = "诚信分";
    				}else if(response._embedded.auditMessageResponses[i].scoreItem == "qualityScore"){
    					$scope.primaryAudit.scoreItem = "产品质量分";
    				}else if(response._embedded.auditMessageResponses[i].scoreItem == "serveScore"){
    					$scope.primaryAudit.scoreItem = "服务分";
    				}
    				$scope.primaryAudit.scoreType = response._embedded.auditMessageResponses[i].scoreType == "plus"?"加分":"减分";

					$scope.scoreItem = response._embedded.auditMessageResponses[i].scoreItem;
    				//$scope.result = response._embedded.auditMessageResponses[i].auditResult;
    				$scope.scoreType = response._embedded.auditMessageResponses[i].scoreType;
    				$scope.auditscore = response._embedded.auditMessageResponses[i].score;
    				$scope.message = response._embedded.auditMessageResponses[i].auditRemark;
    			}
    		}
    	}
	});

	
	/**
	 * 轮播图看上一页
	 */
	$scope.prev = function(){
		//$("#myCarousel").carousel('prev');
		angular.element("#myCarousel").carousel('prev');
	}
	/**
	 * 轮播图看下一页
	 */
	$scope.next = function(){
        //$("#myCarousel").carousel('next');
		angular.element("#myCarousel").carousel('next');

        //播放指定图片
        //$("#myCarousel").carousel(0);
        //$("#myCarousel").carousel(1);
        //停止播放
       // $("#myCarousel").carousel('pause');

	}
	
	$scope.plusScore = function(){
		if($scope.auditscore < 20){
			$scope.auditscore = $scope.auditscore+1;
		}
	}
	
	$scope.reduceScore = function(){
		if($scope.auditscore >= 1){
			$scope.auditscore = $scope.auditscore-1;
		}
	}
	
	$scope.submitAudit = function(){
		var postEntity={
			businessId:$scope.feedbackId,
			businessType:"feedback",
			auditor:loginUser.name,
			auditorId:loginUser.uuid,
			auditResult:$scope.result,
			auditRemark:$scope.message,
			scoreType:$scope.scoreType,
			scoreItem:$scope.scoreItem,
			score:$scope.auditscore
		};
		middleAuditFeedbackFactory.saveAudit(postEntity,function(response){				
			if(response.$resolved){
				$state.go('middleauditfeedbacklist');
				Message.alert({
					msg : "保存成功!",
					title : "成功提示",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
				
			}else{
				Message.alert({
					msg : "保存失败!",
					title : "错误提示",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
			}
	 	 },function(error){	
				Message.alert({
					msg : "保存失败!",
					title : "错误提示",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
	 	 });
	}
	//$state.go('shoeList');
}]);