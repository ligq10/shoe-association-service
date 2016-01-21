/**
 * Created by liguangqiang@changhongit.com 2015-02-02.
 */
'use strict';
var addShoeCompanyAuditControllers=angular.module('addShoeCompanyAuditControllers',['addShoeCompanyAuditServices']);

addShoeCompanyAuditControllers.controller('addShoeCompanyAuditCtrl',['$scope','$timeout','$state','$stateParams','$upload','addShoeCompanyAuditFactory',
    function($scope,$timeout,$state,$stateParams,$upload,addShoeCompanyAuditFactory) {
		$scope.shoeCompanyId = $stateParams.uuid;
			
		addShoeCompanyAuditFactory.getShoeComapnyDetailById({uuid:$scope.shoeCompanyId},function(response){
	    	
	    	if(response.$resolved){
	    		$scope.shoeComapny = response;
	    	}
		});
	//$state.go('shoeList');
}]);
