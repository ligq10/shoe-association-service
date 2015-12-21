/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var shoeAddControllers=angular.module('shoeAddControllers',['shoeAddServices']);

shoeAddControllers.controller('shoeAddCtrl',['$scope','shoeAddFactory',
    function($scope,shoeAddFactory) {
	$scope.letterList=['A','B','C','D','E','F','G','H','I','J','K','L',
	               'M','N','O','P','Q','R','S','T','U','V','W','X',
	               'Y','Z'];
	$scope.creditList = [
	                     {level:'0',desc:"优"},
	                     {level:'1',desc:"良"},
	                     {level:'2',desc:"中"},
	                     {level:'3',desc:"差"}
	                     ];
	$scope.sortList = [
	                   {value:"asc",desc:"升序"},
	                   {value:"desc",desc:"降序"}
	                   ];
	
	$scope.submitShoeCompany = function(){
		if($scope.name == null || $scope.name == undefined || $scope.name == ''){
			Message.alert({
				msg : "企业名称不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		if($scope.enterpriseLegalPerson == null || $scope.enterpriseLegalPerson == undefined || $scope.enterpriseLegalPerson == ''){
			Message.alert({
				msg : "企业法人不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		var postEntity={
				name:$scope.name,
				address:$scope.address,
				enterpriseLegalPerson:$scope.enterpriseLegalPerson,
				submitPerson:$scope.submitPerson,
				tel:$scope.tel
		}
		
		
	}
}]);

shoeListControllers.directive("deletegroupdialog",
    function (){
        var option={
            restrict:"AEC",
            transclude:true,
            replace:true,
            templateUrl:"templates/commonTemplate/delete-group-dialog.html"
        };
        return option;
    });