/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var personnelListControllers=angular.module('personnelListControllers',['personnelListServices']);

/**
 * 人员列表
 */
personnelListControllers.controller('personnelListCtrl',['$scope','loginSession','personnelListFactory',
    function($scope,loginSession,personnelListFactory){
	   var loginUser = loginSession.loginUser().userInfo;
       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
       $scope.pageSize=PAGESIZE_DEFAULT;
       $scope.personnels = [];
   	   var search_keyword="";

       //分页
       var refreshPersonnelList=function(){
    	    $scope.personnels = [];
	    	personnelListFactory.queryList({keyword:search_keyword,page:$scope.currentPage,size:$scope.pageSize,sort:'createTime,desc'},function(personnelAll){
    		   if(personnelAll._embedded==undefined && $scope.currentPage>0){
    			   $scope.personnels = [];
    		       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
    		       $scope.pageSize=PAGESIZE_DEFAULT;
    		       search_keyword="";
    		   }else{
    			   makeEntry(personnelAll);
				   $scope.numPages = function () {
					   if(personnelAll._embedded){
						   return $scope.page.totalPages;//总页数
					   }
				   };
    		   }
    	   });
       };
       
       //人员list
       refreshPersonnelList();
       // 搜索
       $scope.search=function() {
           $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
           $scope.pageSize=PAGESIZE_DEFAULT;
	       if($scope.search_keyword != undefined){
	    		search_keyword = $scope.search_keyword;
	       }  
    	   refreshPersonnelList();
       }
        // 点击下一页，上一页，首页，尾页按钮
       $scope.pageChanged=function(){
           refreshPersonnelList();
       };
            
       //删除
       $scope.deletePersonnel=function(loginName,userId){
    	   if(loginUser.uuid == userId){
    		   Message.alert({
	   		    	msg: "不能删除自己！",
	 		    	title:"失败提示",
	 		    	btnok: '确定',
	 		    	btncl:'取消'
	            	},"error","small");
    		   return false;
    	   }else if(loginUser.loginName == loginName){
    		   Message.alert({
	   		    	msg: "不能删除系统超级管理员！",
	 		    	title:"失败提示",
	 		    	btnok: '确定',
	 		    	btncl:'取消'
	            	},"error","small");
    		   return false;
    	   }else{
    		   Message.confirm(
		   		  {
		   		    msg: "确定要删除该人员？",
		   		    title:"提示",
		   		  })
		   		 .on( function (e) {
		   		    if(e){	    		   
	    			   personnelListFactory.deleteUser({uuid:userId},function(response){
	    		    	   if(response.$resolved){
	    		    		   
	    		    		   Message.alert({
	    			   		    	msg: "删除成功！",
	    			 		    	title:"成功提示",
	    			 		    	btnok: '确定',
	    			 		    	btncl:'取消'
	    			            	},"success","small");
	    		    		   refreshPersonnelList(); 
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

    	   }
       };
       
       //封装数据
       var makeEntry=function(response){
           $scope.page=response.page;
           if(response._embedded==undefined){
               $scope.pagingHidden=true;
           }else{
               $scope.pagingHidden=false;
               $scope.personnels = response._embedded.employeeResponses;
               for(var i=0;i<$scope.personnels.length;i++){
            	   var roles = $scope.personnels[i].roles;
            	   
            	   var roleName = "";
            	   for(var j=0;j<roles.length;j++){
            		   roleName = roleName + roles[j].name+",";
            	   }
            	   $scope.personnels[i].roleName = roleName.substring(0,roleName.length - 1);
               }
           }
       };   
}]);



