/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var feedBackAddControllers=angular.module('feedBackAddControllers',['feedBackAddServices']);

feedBackAddControllers.controller('feedBackAddCtrl',['$scope','$stateParams','$upload','$rootScope','feedBackAddFactory',
    function($scope,$stateParams,$upload,$rootScope,feedBackAddFactory) {
    $scope.shoeComapnyId = $stateParams.uuid;
	
	$scope.score = 0;
	
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