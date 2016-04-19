/**
 * Created by liguangqiang@changhongit.com 2015-02-02.
 */
'use strict';
var shoeDetailControllers=angular.module('shoeDetailControllers',['shoeDetailServices']);

shoeDetailControllers.controller('shoeDetailCtrl',['$scope','$timeout','$state','$stateParams','$upload','shoeDetailFactory',
    function($scope,$timeout,$state,$stateParams,$upload,shoeDetailFactory) {
		$scope.shoeCompanyId = $stateParams.uuid;

		shoeDetailFactory.getShoeComapnyDetailById({uuid:$scope.shoeCompanyId},function(response){
	    	
	    	if(response.$resolved){
	    		$scope.shoeComapny = response;
	    	}
		});
	//$state.go('shoeList');
}]);
