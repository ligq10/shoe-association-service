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
	$scope.proofImageUrls =[
	                        "http://img02.tooopen.com/images/20160125/tooopen_sy_155386511951.jpg",
	                        "http://img02.tooopen.com/images/20151231/tooopen_sy_153270994272.jpg",
	                        "http://img05.tooopen.com/images/20151102/tooopen_sy_147374363674.jpg"	                        
	                        ];
	primaryAuditFeedbackFactory.getFeedbackDetailById({uuid:$scope.feedbackId},function(response){    	
    	if(response.$resolved){
    		$scope.feedback = response;
    		
    		if(response.proofImageUrls != null && response.proofImageUrls.length >0){
    			$scope.proofImageUrlListIndex=[];
    			for(var i=0;i<response.proofImageUrls.length;i++){
    				$scope.proofImageUrlListIndex.push(i);
    			}
    		}
    	}
	});
	
	//$state.go('shoeList');
}]);