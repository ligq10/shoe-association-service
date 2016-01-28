/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var primaryAuditFeedbackListControllers=angular.module('primaryAuditFeedbackListControllers',['primaryAuditFeedbackListServices']);

/**
 * 人员列表
 */
primaryAuditFeedbackListControllers.controller('primaryAuditFeedbackListCtrl',['$scope','loginSession','primaryAuditFeedbackListFactory',
    function($scope,loginSession,primaryAuditFeedbackListFactory){
	//var loginUser = loginSession.loginUser().userInfo;
       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
       $scope.pageSize=PAGESIZE_DEFAULT;
       var search_keyword="";
       //分页
       var refreshPrimaryAuditFeedbackList=function(){
	    	
	    	primaryAuditFeedbackListFactory.queryList({keyword:search_keyword,auditStatus:0,page:$scope.currentPage,size:$scope.pageSize,sort:'createTime,desc'},function(response){
    		   if(response._embedded==undefined && $scope.currentPage>0){
    		       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
    		       $scope.pageSize=PAGESIZE_DEFAULT;
    		       search_keyword="";
    		       $scope.numPages = 0;
    		       $scope.primaryAuditFeedbacks = [];
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
       refreshPrimaryAuditFeedbackList();
       // 搜索
       $scope.search=function() {
           $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
           $scope.pageSize=PAGESIZE_DEFAULT;
	       if($scope.search_keyword != undefined){
	    		search_keyword = $scope.search_keyword;
	       }           
    	   refreshPrimaryAuditFeedbackList();
       }
        // 点击下一页，上一页，首页，尾页按钮
       $scope.pageChanged=function(){
    	   refreshPrimaryAuditFeedbackList();
       };
                  
       //封装数据
       var makeEntry=function(response){
           $scope.page=response.page;
           if(response._embedded==undefined){
               $scope.pagingHidden=true;
           }else{
               $scope.pagingHidden=false;
               $scope.page = response.page;
               $scope.primaryAuditFeedbacks = response._embedded.feedbackResponses;
               $scope.page = response.page;
           }
       };
    
	}
]);