/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var personnelUpdateControllers=angular.module('personnelUpdateControllers',['personnelUpdateServices']);

/**
 * 人员修改
 */
personnelUpdateControllers.controller('personnelUpdateCtrl',['$scope','$timeout','loginSession','$stateParams','personnelUpdateFactory',function($scope,$timeout,loginSession,$stateParams,personnelUpdateFactory){
	$scope.addSuccess="";
	$scope.uuid=$stateParams.uuid;
	//用户管理员角色
	var checkedPermissions=[];
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
        }	
    });
	
	//权限菜单
	personnelUpdateFactory.searchAllRoles(function(responseRole){
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
	
			
	/**
	 * 修改时清空数据
	 */
	$scope.resetPersonnelUpdateData=function(){
		$scope.personnelAddFrom.nickName.$dirty=false;
		$scope.personnelAddFrom.role.$dirty=false;
		$scope.defoutPermissionsPrompt=false;
		$scope.personnelAddFrom.groupId.$dirty=false;
		$scope.personnelAddFrom.tel.$dirty=false;
		$scope.personnelAddFrom.email.$dirty=false;
		$scope.personnelAddFrom.address.$dirty=false;
		$scope.personnelAddFrom.remarks.$dirty=false;
		
		$scope.nickName=null;
		$scope.email=null;
		$scope.tel=null;
		$scope.address=null;
		$scope.remarks=null;
		$scope.permissions=null;
		$scope.selectRole=null;
		$scope.group_divide_radio_name=null;
		$scope.firstAddressCode=null;
		$scope.secondAddressCode=null;
		$scope.thirdAddressCode=null;
		$("input[name!='loginName']").each(function() {
			$(this).val("");
		});
		$("textarea").val("");
		
		$scope.firstAddressCode="";
		$scope.secondAddressCode="";
		$scope.thirdAddressCode="";
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
		user.userId=$scope.userId;
		user.nickName=$scope.nickName;
		user.email=$scope.email;
		user.tel=$scope.tel;
		user.address=$scope.address;
		user.gender=$("input[name='gender']:checked").val();
		var birthDay=$("input[name='birthDay']").val();
		user.birthDay = birthDay;
		user.remarks=$scope.remarks;
		
		//角色
		user.roles=[];
		user.roles.push($scope.selectRole);
		//权限
		user.permissions = $scope.permissions;
		
		//分组
		var group = [];
		var groupVal = $scope.groups;
		group.push(groupVal);
		user.groups = group;
		
		//三级地址
		user.cityCode = $scope.thirdAddressCode;
		personnelFactory.updateUser(user,function(response){
			if(response.$resolved){
				//保存成功清空
                $("input").each(function() {
        			$(this).val("");
        		});
        		$("textarea").val("");
        		
        		$scope.selectRole="";
        		$scope.firstAddressCode="";
        		$scope.secondAddressCode="";
        		$scope.thirdAddressCode="";
        		
                $scope.addSuccess="保存成功,正在跳转..."
                	
            	$timeout(function() {
            		 window.location.href='#/personnelList';
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






