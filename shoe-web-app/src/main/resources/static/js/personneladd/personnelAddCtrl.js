/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var personnelAddControllers=angular.module('personnelAddControllers',['personnelAddServices']);

/**
 * 人员新增
 */
personnelAddControllers.controller('personnelAddCtrl',['$scope','$state','$timeout','loginSession','personnelAddFactory',function($scope,$state,$timeout,loginSession,personnelAddFactory){
	$scope.addSuccess="";
	$scope.roleAdmins = [];
	$scope.gender = "male";
	/**
	 * 检测登录账号是否存在
	 */
	$scope.id_error_ishidden=true;
	$scope.checkLoginName=function(){
        var loginName=$scope.loginName;
        if(loginName!=undefined){
            personnelAddFactory.checkLoginName({loginname:loginName},function(response){
                $scope.id_error_ishidden=response.$resolved;
            },function(error){
                // 接口请求异常处理方法
                if(error.status==400){
                    $scope.id_error_ishidden=false;
                }
            });
        }
    };
	

	//权限菜单
    personnelAddFactory.searchAllRoles(function(responseRole){
		$scope.roleAdmins = [];
		if(responseRole._embedded==undefined){
			$scope.roleAdmins = [];
		}else{
			for(var i=0; i<responseRole._embedded.dataDictResponses.length; i++) {
				$scope.roleAdmins.push({
					roleId:responseRole._embedded.dataDictResponses[i].uuid,
					roleCode:responseRole._embedded.dataDictResponses[i].dictCode,
					roleDesc:responseRole._embedded.dataDictResponses[i].dictDesc
				});
			}
			
		}
   });
	
	
	/**
	 * 确认密码
	 */
	$scope.confirmPasswordWrong=false;
	$scope.confirmPasswordOk = false;
	$scope.checkPassword=function(){
		if($("#confirmPassword").val()!=$("#firstPassword").val()){
			$scope.confirmPasswordWrong = true;
			$scope.confirmPasswordOk = false;
			return ;
			
		}else{
			$scope.confirmPasswordWrong = false;
			$scope.confirmPasswordOk = true;
		}
		
	};
	//验证邮箱
	$scope.mailInVaild=false;
	$scope.mailVaild=false;
	$scope.validEMail=function(){
		if($("#email").val().length!=0){
			var reg=/^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
			var mailreg=$("#email").val().match(reg);
			if(mailreg==null){
				$scope.mailInVaild=true;
				$scope.mailVaild=false;
			}else{
				$scope.mailInVaild=false;
				$scope.mailVaild=true;
			}
			
		}
		
	};
	
	
	/**
	 * 保存权限分配
	 */
	$scope.saveAuthorityDistribution=function(){
		var checkedRoles = [];
		$("input:checked[rank='children']").each(function(){
			if($(this).val().length > 0){
				checkedRoles.push($(this).val());
			}
		});
		$("#authority_distribution_checked_permissions").val(checkedRoles);
		
		$scope.permissions=checkedRoles;
		
		$("#authority_distribution").modal('hide');
		$scope.defoutPermissionsPrompt = false;
		
	};
	

	

	/**
	 * 新增时清空数据
	 */
	$scope.resetPersonnelAddData=function(){
		$scope.personnelAddFrom.loginName.$dirty=false;
		$scope.personnelAddFrom.nickName.$dirty=false;
		$scope.personnelAddFrom.password.$dirty=false;
		$scope.confirmPasswordWrong=false;
		$scope.personnelAddFrom.role.$dirty=false;
		$scope.defoutPermissionsPrompt=false;
		//$scope.personnelAddFrom.groupId.$dirty=false;
		$scope.personnelAddFrom.tel.$dirty=false;
		$scope.personnelAddFrom.email.$dirty=false;
		$scope.personnelAddFrom.address.$dirty=false;
		$scope.personnelAddFrom.remarks.$dirty=false;
		
		$scope.loginName=null;
		$scope.password=null;
		$scope.nickName=null;
		$scope.email=null;
		$scope.tel=null;
		$scope.address=null;
		$scope.remarks=null;
		$scope.permissions=null;
		$scope.selectRole=null;
		
	};	
	
	/**
	 * 新增人员保存
	 */
	$scope.personnelAdd=function(){
		var user={};
		user.loginName=$scope.loginName;
		user.password=$scope.password;
		user.name=$scope.nickName;
		user.email=$scope.email;
		user.tel=$scope.tel;
		user.homeAddress=$scope.address;
		user.remarks=$scope.remarks;
		
		user.gender=$scope.gender;
		user.birthDay=$("input[name='birthday']").val();
		//角色
		user.roleCodes=[];
		user.roleCodes=$scope.selectRole;
		
		personnelAddFactory.create(user,function(response){
            if(response.$resolved){
               
                //保存成功清空
                $("input").each(function() {
        			$(this).val("");
        		});
        		$("textarea").val("");
        		
        		$scope.selectRole="";
        		
                $scope.addSuccess="保存成功,正在跳转..."
                	
            	$timeout(function() {
    				$state.go('personnellist');
    		    }, 1000);
        		
            }else{
            	$scope.addSuccess="保存失败！"
            }
        },function(error){
        	$scope.addSuccess="保存失败！"
        }
        );
    };
	
	
}]);

/**
 * 权限分配dialog
 */
personnelAddControllers.directive("authoritydistribute", function (){
	var option={
			restrict:"AEC",
			transclude:true,
			replace:true,
			templateUrl:"templates/commonTemplate/authority-distribution.html"
	};
	return option;
});
