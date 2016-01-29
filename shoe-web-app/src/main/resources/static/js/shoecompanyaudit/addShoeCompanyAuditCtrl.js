/**
 * Created by liguangqiang@changhongit.com 2015-02-02.
 */
'use strict';
var addShoeCompanyAuditControllers=angular.module('addShoeCompanyAuditControllers',['addShoeCompanyAuditServices']);

addShoeCompanyAuditControllers.controller('addShoeCompanyAuditCtrl',['$scope','loginSession','$timeout','$state','$stateParams','$upload','addShoeCompanyAuditFactory',
    function($scope,loginSession,$timeout,$state,$stateParams,$upload,addShoeCompanyAuditFactory) {
		$scope.shoeCompanyId = $stateParams.uuid;
		var loginUser = loginSession.loginUser().userInfo;

		$scope.result = "pass";	
		addShoeCompanyAuditFactory.getShoeComapnyDetailById({uuid:$scope.shoeCompanyId},function(response){
	    	
	    	if(response.$resolved){
	    		$scope.shoeComapny = response;
	    	}
		});
		
		$scope.submitAudit = function(){
			var postEntity={
				businessId:$scope.shoeCompanyId,
				businessType:"register",
				auditor:loginUser.name,
				auditorId:loginUser.uuid,
				auditResult:$scope.result,
				auditRemark:$scope.message
			};
			addShoeCompanyAuditFactory.saveAudit(postEntity,function(response){				
				if(response.$resolved){
					$state.go('auditshoecompanylist');
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
