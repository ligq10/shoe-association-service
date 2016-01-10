/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var auditShoeCompanyListControllers=angular.module('auditShoeCompanyListControllers',['auditShoeCompanyListServices']);

/**
 * 人员列表
 */
auditShoeCompanyListControllers.controller('auditShoeCompanyListCtrl',['$scope','auditShoeCompanyListFactory',
    function($scope,auditShoeCompanyListFactory){
	//var loginUser = loginSession.loginUser().userInfo;
       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
       $scope.pageSize=PAGESIZE_DEFAULT;
       //分页
       var refreshAuditShoeCompanyListList=function(){
	    	var search_keyword="";
	    	if($scope.search_keyword != undefined){
	    		search_keyword = $scope.search_keyword;
	    	}
	    	auditShoeCompanyListFactory.queryList({keyword:search_keyword,page:$scope.currentPage,size:$scope.pageSize,sort:'createDate,desc'},function(response){
    		   if(response._embedded==undefined && $scope.currentPage>0){
    			   --($scope.currentPage);
    			   refreshPersonnelList();
    		   }else{
    			   //makeEntry(response);
				   $scope.numPages = function () {
					   if(response._embedded){
						   return $scope.page.totalPages;//总页数
					   }
				   };
    		   }
    	   });
       };
       
       //人员list
       auditShoeCompanyListFactory();
       // 搜索
       $scope.search=function() {
    	   $scope.currentPage=CURRENTPAGE_INIT;
    	   auditShoeCompanyListFactory();
       }
        // 点击下一页，上一页，首页，尾页按钮
       $scope.pageChanged=function(){
    	   auditShoeCompanyListFactory();
       };
 
}]);



