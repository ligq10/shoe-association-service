/**
 * Created by ligq 2015-02-02.
 */
'use strict';
var shoeListControllers=angular.module('shoeListControllers',['shoeListServices']);

shoeListControllers.controller('shoeListCtrl',['$scope','shoeListFactory',
    function($scope,shoeListFactory) {

	var searchInputTemp = "";
	$scope.phoneticizeTemp = "";
	$scope.creditLevelTemp = "";
	$scope.sortTemp = ""; 
	
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
	
    $scope.pageSize=PAGESIZE_DEFAULT;
	$scope.currentPage=CURRENTPAGE_INIT;
	
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
	
	$scope.companyList = [];
	
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
	
	//initCompanyList($scope.companyList);
	

	/**
	 * 刷新企业列表
	 */
	var refreshShoesList = function(queryEntity){
		var queryEntity = queryEntity;
		$scope.companyList = [];		
		$scope.companyRowList = [];
		if(queryEntity == null || queryEntity == undefined){
		    $scope.pageSize=PAGESIZE_DEFAULT;
			$scope.currentPage=CURRENTPAGE_INIT;
			queryEntity = {

					page:$scope.currentPage,
					size:$scope.pageSize,
					auditStatus:1
			};
			
		}
		shoeListFactory.queryShoesByMultipleConditions(queryEntity,function(response){
			
			if(response.$resolved){
                if(response._embedded==undefined){
                	$scope.companyList = [];              	
                	$scope.companyRowList = [];
                	$scope.currentPage = 0;
                	$scope.numPages = 0;	                	
                	return false;
                }else{
                	$scope.companyList=[];
                	$scope.page=response.page;
                    $scope.numPages = function () {
                        return response.page.totalPages;
                    };
/*                    for(var i=0;i< response._embedded.orderSummaryResponses.length;i++){
                    	$scope.orders[i]= response._embedded.orderSummaryResponses[i];
                    }*/
                    initCompanyList(response._embedded.shoeCompanyResponses);
                }

			}else{
				$scope.companyList = [];				
				$scope.companyRowList = [];
				
            	$scope.currentPage = 0;
            	$scope.numPages = 0;
			}
		});
	}					

	//初始化页面
	refreshShoesList();
	
	/**
	 * 查询输入框条目列表
	 */
	$scope.queryShoes = function(){
		searchInputTemp = $scope.searchInput;
		$scope.phoneticizeTemp = "";
		$scope.creditLevelTemp = "";
		$scope.sortTemp = ""; 
		var queryEntity = {
				name : searchInputTemp,	
				phoneticize : "",
				level : "",
				sort : "",
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshShoesList(queryEntity);
	}
	
	/**
	 * 字母查询
	 */
	$scope.phoneticizeSearch = function(phoneticize){
		$scope.searchInput = "";
		searchInputTemp = $scope.searchInput;
		$scope.phoneticizeTemp = phoneticize;
		$scope.creditLevelTemp = "";
		$scope.sortTemp = ""; 
		var queryEntity = {
				name : searchInputTemp,	
				phoneticize : phoneticize,
				level : $scope.creditLevelTemp,
				sort : $scope.sortTemp,
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshShoesList(queryEntity);
	}
	
	/**
	 * 字母查询
	 */
	$scope.searchAll = function(){
		$scope.currentPage = 0;
		$scope.pageSize = 20;
		$scope.searchInput = "";
		searchInputTemp = $scope.searchInput;
		$scope.phoneticizeTemp = "";
		$scope.creditLevelTemp = "";
		$scope.sortTemp = ""; 
		var queryEntity = {
				name : searchInputTemp,	
				phoneticize : $scope.phoneticizeTemp,
				level : $scope.creditLevelTemp,
				sort : $scope.sortTemp,
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshShoesList(queryEntity);
	}
	
	/**
	 * 信誉等级
	 */
	$scope.searchCredit = function(level){
		$scope.currentPage = 0;
		$scope.pageSize = 20;
		$scope.searchInput = "";
		searchInputTemp = $scope.searchInput;
		$scope.phoneticizeTemp = "";
		$scope.creditLevelTemp = level;
		$scope.sortTemp = "";
		var queryEntity = {
				name : searchInputTemp,	
				phoneticize : $scope.phoneticizeTemp,
				level : level,
				sort : $scope.sortTemp,
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshShoesList(queryEntity);
	}
	
	$scope.searchSort = function(value){
		$scope.searchInput = "";
		$scope.sortTemp = value;
		var queryEntity = {
				name : searchInputTemp,	
				phoneticize : $scope.phoneticizeTemp,
				level : $scope.creditLevelTemp,
				sort : $scope.sortTemp,
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshShoesList(queryEntity);
	}
	
    //shoe翻页 点击下一页，上一页，首页，尾页按钮
    $scope.pageChanged=function(){
		var queryEntity = {
				name : searchInputTemp,	
				phoneticize : $scope.phoneticizeTemp,
				level : $scope.creditLevelTemp,
				sort : $scope.sortTemp,
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshShoesList(queryEntity);
    }
}]);