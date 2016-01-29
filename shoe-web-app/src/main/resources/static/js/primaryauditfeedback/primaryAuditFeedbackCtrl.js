/**
 * Created by liguangqiang@changhongit.com 2015-05-15.
 */
'use strict';
var primaryAuditFeedbackControllers=angular.module('primaryAuditFeedbackControllers',['primaryAuditFeedbackServices']);

/**
 * 人员列表
 */
primaryAuditFeedbackControllers.controller('primaryAuditFeedbackCtrl',['$scope','$stateParams','$state','loginSession','primaryAuditFeedbackFactory',
    function($scope,$stateParams,$state,loginSession,primaryAuditFeedbackFactory){
	//var loginUser = loginSession.loginUser().userInfo;
	$scope.feedbackId = $stateParams.uuid;
	$scope.proofImageUrlListIndex=[];

	// 轮播图数据初始化
	$scope.slides =[];
    $scope.slides =[
                    "http://img02.tooopen.com/images/20160125/tooopen_sy_155386511951.jpg",
                    "http://img02.tooopen.com/images/20151231/tooopen_sy_153270994272.jpg"
                             
                    ];
	primaryAuditFeedbackFactory.getFeedbackDetailById({uuid:$scope.feedbackId},function(response){    	
    	if(response.$resolved){
    		$scope.feedback = response;
    		
    		if(response.proofImageUrls != null && response.proofImageUrls.length >0){
    			//$scope.slides = response.proofImageUrls;
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
	//$state.go('shoeList');
}]);