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
	$scope.role = "";
	$scope.roles = [];
	$scope.gender = "male";
	//用户管理员角色
	var selectUserRole=[];
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
		$scope.roles = [];
		if(responseRole._embedded==undefined){
			$scope.roles = [];
		}else{
			for(var i=0; i<responseRole._embedded.dataDictResponses.length; i++) {
				$scope.roles.push({
					roleId:responseRole._embedded.dataDictResponses[i].uuid,
					roleCode:responseRole._embedded.dataDictResponses[i].dictCode,
					roleDesc:responseRole._embedded.dataDictResponses[i].dictDesc,
					checked:false
				});
			}
			
		}
   });
	
	
	$scope.selectRole = [];
    $scope.checkedRole = function(scope){
		if($.inArray(scope.uuid,$scope.selectRole) != -1){
			$scope.selectRole.splice($.inArray(scope.code,$scope.selectRole),1);
    	}else{
    		$scope.selectRole.push(scope.code);
    	}
		
    }
    
    $scope.chkAll = false;
    $scope.checkedAll = function(scope,chkAll){
    	
		angular.forEach($scope.roles, function(value, key){
	        value.checked = !chkAll;
	    });
    	
		//$scope.selectRole = [];
    	$scope.chkAll=!$scope.chkAll;    	
    }
	
    $scope.saveAuthorityDistribution = function(){    	
    	$scope.role = "";
    	selectUserRole = [];
		angular.forEach($scope.roles, function(value, key){
	        if(value.checked){
	        	$scope.role = $scope.role + value.roleDesc+",";
	        	selectUserRole.push(value.roleCode);
	        }
	    });
		$scope.role = $scope.role.substring(0,$scope.role.length-1);
		$("#authority_distribution_dialog").modal('hide');		
    }
    
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
		user.homeAddress=$scope.homeAddress;
		user.remarks=$scope.remarks;
		
		user.gender=$scope.gender;
		user.birthday=$scope.birthday;
		//角色
		user.roleCodes=[];
		user.roleCodes=selectUserRole;
		
		personnelAddFactory.create(user,function(response){
            if(response.$resolved){
               
                //保存成功清空
                $("input").each(function() {
        			$(this).val("");
        		});
        		$("textarea").val("");
        		$scope.role = "";
        		$scope.selectRole = [];
        		selectUserRole = [];
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

