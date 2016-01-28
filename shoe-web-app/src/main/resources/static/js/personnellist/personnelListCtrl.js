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
	//var loginUser = loginSession.loginUser().userInfo;
       $scope.currentPage=CURRENTPAGE_INIT;//当前第几页
       $scope.pageSize=PAGESIZE_DEFAULT;
   	   var search_keyword="";

       //分页
       var refreshPersonnelList=function(){

	    	personnelListFactory.queryList({keyword:search_keyword,page:$scope.currentPage,size:$scope.pageSize,sort:'createTime,desc'},function(personnelAll){
    		   if(personnelAll._embedded==undefined && $scope.currentPage>0){
    			   --($scope.currentPage);
    			   refreshPersonnelList();
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
    	   }else{
    		   $('#personnel-del-modal').modal('show');
    		   $scope.personnelName=loginName;
    		   $scope.deleteOk=function(){
    			   personnelListFactory.delete({uuid:userId},function(response){
    				   refreshPersonnelList();
    			   });
    		   }
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
           }
       };   
}]);


/**
 * 重置密码dialog
 */
personnelListControllers.directive("resetpassword", function (){
    var option={
        restrict:"AEC",
        transclude:true,
        replace:true,
        templateUrl:"templates/commonTemplate/resetPassword.html"
    };
    return option;
});

/**
 * 删除人员dialog
 */
personnelListControllers.directive("deletepersonneldialog", function (){
    var option={
        restrict:"AEC",
        transclude:true,
        replace:true,
        templateUrl:"templates/commonTemplate/delete-personnel-dialog.html"
    };
    return option;
});


