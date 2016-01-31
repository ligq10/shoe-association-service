/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var auditShoeCompanyListControllers=angular.module('auditShoeCompanyListControllers',['auditShoeCompanyListServices']);

/**
 * 审计条目列表
 */
auditShoeCompanyListControllers.controller('auditShoeCompanyListCtrl',['$scope','loginSession','auditShoeCompanyListFactory',
    function($scope,loginSession,auditShoeCompanyListFactory){
	//var loginUser = loginSession.loginUser().userInfo;
       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
       $scope.pageSize=PAGESIZE_DEFAULT;
       $scope.shoecompanyList = [];
   	   var search_keyword="";
      
       //分页
       var refreshAuditShoeCompanyListList = function(){
    	   
    	    $scope.shoecompanyList = [];

	    	auditShoeCompanyListFactory.queryAuditList({keyword:search_keyword,auditStatus:0,page:$scope.currentPage,size:$scope.pageSize},function(response){
    		   if(response._embedded==undefined && $scope.currentPage>0){   			  
    		       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
    		       $scope.pageSize=PAGESIZE_DEFAULT;
    		       $scope.shoecompanyList = [];
    		   	   search_keyword="";
    		   }else{
    			   $scope.shoecompanyList = response._embedded.shoeCompanyResponses;
				   $scope.numPages = function () {
					   if(response._embedded){
						   return $scope.page.totalPages;//总页数
					   }
				   };
    		   }
    	   });
       };
       
       //人员list
       refreshAuditShoeCompanyListList();
       // 搜索
       $scope.search=function() {
           $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
           $scope.pageSize=PAGESIZE_DEFAULT;
	       if($scope.search_keyword != undefined){
	    		search_keyword = $scope.search_keyword;
	       } 
    	   refreshAuditShoeCompanyListList();
       }
        // 点击下一页，上一页，首页，尾页按钮
       $scope.pageChanged=function(){
    	   refreshAuditShoeCompanyListList();
       };
 
}]);



