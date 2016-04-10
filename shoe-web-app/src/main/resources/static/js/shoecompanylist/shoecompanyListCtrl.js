/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var shoecompanyListControllers=angular.module('shoecompanyListControllers',['shoecompanyListServices']);

/**
 * 人员列表
 */
shoecompanyListControllers.controller('shoecompanyListCtrl',['$scope','loginSession','shoecompanyListFactory',
    function($scope,loginSession,shoecompanyListFactory){
	   var loginUser = loginSession.loginUser().userInfo;
       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
       $scope.pageSize=PAGESIZE_DEFAULT;
       $scope.shoecompanies = [];
   	   var search_keyword="";

       //分页
       var refreshShoecompanyList=function(){
    	    $scope.shoecompanies = [];
    	    shoecompanyListFactory.queryList({keyword:search_keyword,page:$scope.currentPage,size:$scope.pageSize,sort:'createTime,desc'},function(response){
    		   if(response._embedded==undefined){
    			   $scope.shoecompanies = [];
    		       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
    		       $scope.pageSize=PAGESIZE_DEFAULT;
    		       search_keyword="";
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
       refreshShoecompanyList();
       // 搜索
       $scope.search=function() {
           $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
           $scope.pageSize=PAGESIZE_DEFAULT;
	       if($scope.search_keyword != undefined){
	    		search_keyword = $scope.search_keyword;
	       }  
	       refreshShoecompanyList();
       }
        // 点击下一页，上一页，首页，尾页按钮
       $scope.pageChanged=function(){
    	   refreshShoecompanyList();
       };
            
       //删除
       $scope.deleteshoecompany=function(uuid){

    		   Message.confirm(
		   		  {
		   		    msg: "确定要删除该条目？",
		   		    title:"提示",
		   		  })
		   		 .on( function (e) {
		   		    if(e){	    		   
		   		    	shoecompanyListFactory.deleteShoeCompany({uuid:uuid},function(response){
	    		    	   if(response.$resolved){
	    		    		   
	    		    		   Message.alert({
	    			   		    	msg: "删除成功！",
	    			 		    	title:"成功提示",
	    			 		    	btnok: '确定',
	    			 		    	btncl:'取消'
	    			            	},"success","small");
	    		    		   refreshShoecompanyList(); 
	    		    	   }else{
	    		    		   Message.alert({
	    			   		    	msg: "删除失败！",
	    			 		    	title:"错误提示",
	    			 		    	btnok: '确定',
	    			 		    	btncl:'取消'
	    			            	},"error","small");
	    		    	   }
	    				   
	    			   });		    		   
		   		    }
		   	   }); 
   	   
       };
       
       //封装数据
       var makeEntry=function(response){
           $scope.page=response.page;
           if(response._embedded==undefined){
               $scope.pagingHidden=true;
           }else{
               $scope.pagingHidden=false;
               $scope.shoecompanies = response._embedded.shoeCompanyResponses;
           }
       };   
}]);



