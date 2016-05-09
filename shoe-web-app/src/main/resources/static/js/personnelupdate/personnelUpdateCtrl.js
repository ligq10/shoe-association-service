/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var personnelUpdateControllers=angular.module('personnelUpdateControllers',['personnelUpdateServices']);

/**
 * 人员修改
 */
personnelUpdateControllers.controller('personnelUpdateCtrl',['$scope','$state','$timeout','loginSession','$stateParams','personnelUpdateFactory',
  function($scope,$state,$timeout,loginSession,$stateParams,personnelUpdateFactory){
	$scope.addSuccess="";
	$scope.uuid=$stateParams.uuid;
	$scope.role = "";
	$scope.roles = [];
	//用户管理员角色
	var selectUserRole=[];
	
	/**
	 * 判断角色权限是否已选中
	 */
	var isSelectUserRole = function(role){
		if(selectUserRole != null && selectUserRole!= undefined && selectUserRole.length>0){
			for(var i=0;i<selectUserRole.length;i++){
				if(role == selectUserRole[i]){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 初始化权限菜单
	 */
	var initAuthority = function(){
		//权限菜单
		personnelUpdateFactory.searchAllRoles(function(responseRole){
			$scope.roles = [];
			if(responseRole._embedded==undefined){
				$scope.roles = [];
			}else{
				for(var i=0; i<responseRole._embedded.roleResponses.length; i++) {
					
					if(selectUserRole != null && selectUserRole!= undefined && selectUserRole.length>0){
						var isSelectUserRoleFlag = isSelectUserRole(responseRole._embedded.roleResponses[i].code);
						if(isSelectUserRoleFlag){
							$scope.roles.push({
								uuid:responseRole._embedded.roleResponses[i].uuid,
								roleCode:responseRole._embedded.roleResponses[i].code,
								roleDesc:responseRole._embedded.roleResponses[i].name,
								checked:true
							});
							
						}else{
							$scope.roles.push({
								uuid:responseRole._embedded.roleResponses[i].uuid,
								roleCode:responseRole._embedded.roleResponses[i].code,
								roleDesc:responseRole._embedded.roleResponses[i].name,
								checked:false
							});
						}
					}else{
						$scope.roles.push({
							uuid:responseRole._embedded.roleResponses[i].uuid,
							roleCode:responseRole._embedded.roleResponses[i].code,
							roleDesc:responseRole._embedded.roleResponses[i].name,
							checked:false
						});
					}				
				}
				
			}
	   });
	}
	
	//查询某个用户全部信息
	personnelUpdateFactory.personnelDetail({uuid:$scope.uuid},function(response){
		$scope.personnel={};
        if(response==undefined){
        }else{
        	$scope.userId=response.uuid;
        	$scope.loginName=response.loginName;
        	$scope.email=response.email;
        	$scope.tel=response.tel;
        	$scope.homeAddress = response.homeAddress;
        	$scope.selectRole = response.roles;
        	$scope.name=response.name;
        	$scope.gender = response.gender;
        	$scope.birthday = response.birthday;
        	$scope.password = response.password;
        	if(response.roles != null && response.roles.length >0){
        		for(var i=0;i<response.roles.length;i++){
        			$scope.role = $scope.role +response.roles[i].name+",";
        			selectUserRole.push(response.roles[i].code);
        		}
        		$scope.role = $scope.role.substring(0,$scope.role.length-1);
        	}
        	initAuthority();
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
	 * 检测登录账号是否存在
	 */
	$scope.id_error_ishidden=true;
	$scope.checkLoginName=function(){
        var loginName=$scope.loginName;
        if(loginName!=undefined){
            personnelUpdateFactory.checkLoginName({loginname:loginName},function(response){
                $scope.id_error_ishidden=response.$resolved;
            },function(error){
                // 接口请求异常处理方法
                if(error.status==400){
                    $scope.id_error_ishidden=false;
                }
            });
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
	 * 修改人员保存
	 */
	$scope.personnelUpdate=function(){
		var user={};
		user.loginName = $scope.loginName;
		user.name=$scope.name;
		user.email=$scope.email;
		user.tel=$scope.tel;
		user.homeAddress=$scope.homeAddress;
		//user.gender=$("input[name='gender']:checked").val();
		user.birthday=$("input[name='birthday']").val();
		user.gender = $scope.gender;
		//user.birthday = $scope.birthday;
		user.remarks=$scope.remarks;
		user.password = $scope.password;
		//角色
		user.roleCodes=[];
		user.roleCodes=selectUserRole;
		
		personnelUpdateFactory.updateUser({uuid:$scope.uuid},user,function(response){
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
            	//$scope.addSuccess="保存失败！"
            }
        }/*,function(error){
            // 接口请求异常处理方法
            dealAbnormalResponse(error);
        }*/
        );
    };
	
}]);