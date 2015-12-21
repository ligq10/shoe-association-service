/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var shoeListControllers=angular.module('shoeListControllers',['shoeListServices']);

shoeListControllers.controller('shoeListCtrl',['$scope','shoeListFactory',
    function($scope,shoeListFactory) {

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
	$scope.company = {
			name : "雪松",
			address : "成都市高新区",
			enterpriseLegalPerson : "韩寒",
			submitPerson : "韩寒",
			tel : "13409890879",
			createTime : "",
			updateTime : "",
			comprehensiveScore : "100",
			logoUrl : "",
			totalScore : "100",
			creditScore : "30",
			qualityScore : "30",
			serveScore : "40",
			creditLevel : "0"
	};
	
	$scope.companyList = [
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
			$scope.company,
	                      ];
	$scope.companyRowList = [];
	
	var initCompanyList= function(companyList) {

        if(companyList != null && companyList != undefined  && companyList.length > 0){
            var j=1;
            var groupArr = [];
            for(var i=0;i<companyList.length;i++){
                var company={};
                company = companyList[i];
                groupArr.push(company);
                if(i==(j*4-1) || i== (companyList.length-1)){
                    $scope.companyRowList.push(groupArr);
                    groupArr =[];
                    j++;
                }
            }
        }
	}
	
	initCompanyList($scope.companyList);
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