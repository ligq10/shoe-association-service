/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var creditDetailControllers=angular.module('creditDetailControllers',['creditDetailServices']);

creditDetailControllers.controller('creditDetailCtrl',['$scope','$stateParams','$upload','$rootScope','creditDetailFactory',
    function($scope,$stateParams,$upload,$rootScope,creditDetailFactory) {
    $scope.shoeComapnyId = $stateParams.uuid;
    
    creditDetailFactory.getShoeComapnyDetailById({uuid:$scope.shoeComapnyId},function(response){
    	
    	if(response.$resolved){
    		$scope.shoeComapny = response;
    	}
	});
	
}]);

/*creditDetailControllers.directive("deletegroupdialog",
    function (){
        var option={
            restrict:"AEC",
            transclude:true,
            replace:true,
            templateUrl:"templates/commonTemplate/delete-group-dialog.html"
        };
        return option;
    });*/