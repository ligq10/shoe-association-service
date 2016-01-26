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
       //分页
       var refreshPersonnelList=function(){
	    	var search_keyword="";
	    	if($scope.search_keyword != undefined){
	    		search_keyword = $scope.search_keyword;
	    	}
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
    	   $scope.currentPage=CURRENTPAGE_INIT;
    	   refreshPersonnelList();
       }
        // 点击下一页，上一页，首页，尾页按钮
       $scope.pageChanged=function(){
           refreshPersonnelList();
       };
     $scope.passwordChange=function(){
    	 $scope.error="";
     }
    /**
   	 * 确认密码
   	 */
/*   	$scope.confirmPasswordWrong=false;
   	$scope.checkPassword=function(){
   		if($("#confirmPassword").val().length > 0 ){
   			if($("#firstPassword").val().length == 0){
   				$scope.confirmPasswordWrong = false;
   				return;
   			}
   			if($("#firstPassword").val() != $("#confirmPassword").val()){
   				//$scope.confirmPassword="";
   				//$("#confirmPassword").focus();
   				$scope.confirmPasswordWrong = true;
   			}else{
   				$scope.confirmPasswordWrong = false;
   			}
   		}else{
   			if($("#firstPassword").val().length == 0){
   				$scope.confirmPasswordWrong = false;
   			}else{
   				$scope.confirmPasswordWrong = true;
   			}
   		}
   	};*/
   	
/*   	//重置密码modal
     $scope.resetPasswordModal=function(loginName){
    	 $scope.restPasswordFrom.password.$dirty=false;
    	 $scope.confirmPasswordWrong = false;
    	 $scope.confirmPassword = "";
    	 $scope.password = "";
    	 $scope.error="";
    	 
    	 $scope.loginName=loginName;
    	 $('.personnel_reset_password').modal('show');
     }*/  
       
       //重置密码
/*      $scope.error="";
       $scope.restPassword=function(){
    	   var reset = {};
    	   reset.loginName=$scope.loginName;
    	   reset.password=$scope.password;
    	   personnelListFactory.resetPassword(reset,function(response){
    		   //$scope.error="修改成功！"
			   $('.personnel_reset_password').modal('hide');
    	   },function(error){
               // 接口请求异常处理方法
               if(error.status==400){
    			   if(error.data.code == 'password.empty'){
    				   $scope.error="密码不能为空！"
    			   }
    			   if(error.data.code == 'password.same'){
    				   $scope.error="新密码不能与旧密码相同！"
    			   }
               }
           });
       };*/
       
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
       
//       //取消按钮
       $scope.cancelOk=function(){
    	   $scope.password="";
    	   $scope.newPassword="";
    	   $scope.error="";
       };
       //封装数据
       var makeEntry=function(response){
           $scope.entry=[];
           $scope.links={};
           $scope.page=response.page;
           if(response._embedded==undefined){
               $scope.pagingHidden=true;
           }else{
               $scope.pagingHidden=false;
               var personnelData = response._embedded.employeeResponses;
               var uuid;
               var loginName;
               var email;
               var tel;
               var name;
               var createTime;
               var roleName;
               for(var i=0; i<personnelData.length; i++){
                	   uuid=personnelData[i].uuid;
                	   loginName=personnelData[i].loginName;//登录账号
                	   email=personnelData[i].email;//联系邮箱
                	   tel=personnelData[i].tel;//联系电话
                	   name=personnelData[i].name;//用户名
                	   createTime=personnelData[i].createTime;//创建时间
                	   roleName="";//角色
                	   if(personnelData[i].roles != null && personnelData[i].roles != undefined && personnelData[i].roles.length >0){
                		   for(var j=0; j<personnelData[i].roles.length; j++){
                			   roleName = roleName + personnelData[i].roles[j].name+",";
                		   }
                		   roleName = roleName.substring(0,roleName.length-1);
                	   }
                	   
                   
                   $scope.entry.push({
                	   uuid:uuid,
                       loginName:loginName,
                       email:email,
                       tel:tel,
                       name:name,
                       createTime:createTime,
                       roleName:roleName,                	   
                   });
               }
               $scope.page = response.page;
           }
           $scope.personnels=$scope.entry;
       };
    
	}
]);


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


