/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var middleAuditFeedbackListControllers=angular.module('middleAuditFeedbackListControllers',['middleAuditFeedbackListServices']);

/**
 * 人员列表
 */
middleAuditFeedbackListControllers.controller('middleAuditFeedbackListCtrl',['$scope','loginSession','middleAuditFeedbackListFactory',
    function($scope,loginSession,middleAuditFeedbackListFactory){
	//var loginUser = loginSession.loginUser().userInfo;
       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
       $scope.pageSize=PAGESIZE_DEFAULT;
       var search_keyword="";
       //分页
       var refreshMiddleAuditFeedbackList=function(){
	       $scope.middleAuditFeedbacks = [];
    	   middleAuditFeedbackListFactory.queryList({keyword:search_keyword,auditStatus:1,page:$scope.currentPage,size:$scope.pageSize,sort:'createTime,desc'},function(response){
    		   if(response._embedded==undefined && $scope.currentPage>0){
    		       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
    		       $scope.pageSize=PAGESIZE_DEFAULT;
    		       search_keyword="";
    		       $scope.numPages = 0;
    		       $scope.middleAuditFeedbacks = [];
    		   }else{
    			   makeEntry(response);
				   $scope.numPages = function () {
					   if(response._embedded){
						   return $scope.page.totalPages;//总页数
					   }
				   };
    		   }
    	   });
       };
       
       //人员list
       refreshMiddleAuditFeedbackList();
       // 搜索
       $scope.search=function() {
           $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
           $scope.pageSize=PAGESIZE_DEFAULT;
	       if($scope.search_keyword != undefined){
	    		search_keyword = $scope.search_keyword;
	       }           
	       refreshMiddleAuditFeedbackList();
       }
        // 点击下一页，上一页，首页，尾页按钮
       $scope.pageChanged=function(){
    	   refreshMiddleAuditFeedbackList();
       };
                  
       //封装数据
       var makeEntry=function(response){
           $scope.page=response.page;
           if(response._embedded==undefined){
               $scope.pagingHidden=true;
           }else{
               $scope.pagingHidden=false;
               $scope.page = response.page;
               $scope.middleAuditFeedbacks = response._embedded.feedbackResponses;
               $scope.page = response.page;
           }
       };
    
}]);