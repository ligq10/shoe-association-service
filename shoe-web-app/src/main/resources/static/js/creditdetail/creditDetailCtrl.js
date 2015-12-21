/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var creditDetailControllers=angular.module('creditDetailControllers',['creditDetailServices']);

creditDetailControllers.controller('creditDetailCtrl',['$scope','creditDetailFactory',
    function($scope,creditDetailFactory) {
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