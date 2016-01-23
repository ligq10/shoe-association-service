/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
'use strict';
var personnelAddControllers=angular.module('personnelAddControllers',['personnelAddServices']);

/**
 * 人员新增
 */
personnelAddControllers.controller('personnelAddCtrl',['$scope','$timeout','personnelAddFactory',function($scope,$timeout,personnelAddFactory){
	$scope.addSuccess="";
	$scope.roleAdmins = [];
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
	 * 当改变role清除上次的权限选择并加载对应权限
	 */
/*	$scope.resetCheckedRoles=function(){
		$scope.defoutPermissionsPrompt = true;
		
		//清除上次的权限选择
		//$("#authority_distribution_checked_permissions").val("");
		
		if($scope.selectRole) {
			//加载对应权限
			var roleIds = {};
			var modules = [];
			var getrole=$scope.selectRole.split("/");
			var roleId=getrole[getrole.length-1];
			var permissionIds={};
			personnelAddFactory.searchPermissionsByRoles({roleId:roleId},function(permissionsAll){
				if(permissionsAll._embedded==undefined){
					permissionIds = {};
				}else{
					for(var i=0; i<permissionsAll._embedded.permissions.length; i++) {
						var isNotHaveModule=true;   
						for (var j = 0; j < modules.length; j++) {
							if (modules[j].title == permissionsAll._embedded.permissions[i].module) {
								isNotHaveModule= false;
							}
						}
						if (isNotHaveModule) {
							modules.push({
								"roleId": "/roles/"+permissionsAll._embedded.permissions[i].uuid,
								"title": permissionsAll._embedded.permissions[i].module,
								"children":permissionsAll._embedded.permissions[i].uuid,
								"items": []
							});
						}
					}
					
					for(var i=0; i<permissionsAll._embedded.permissions.length; i++) {
						for (var j = 0; j < modules.length; j++) {
							if (modules[j].title == permissionsAll._embedded.permissions[i].module) {
								modules[j].items.push({
									"roleId": "/permissions/"+permissionsAll._embedded.permissions[i].uuid,
									"title": permissionsAll._embedded.permissions[i].description,
									"parent":modules[j].children,
									"items": []
								});
							}
						}
					}
					
					$scope.authorities= modules;
				}
				
			});
		} 
	};*/
	
	/**
	 * 权限树加载完后执行：全选
	 */
/*	$scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
        $("input:checkbox").prop("checked",true);
    });*/

	/**
	 * 保存选择的分组，填充到表单
	 */
/*	$scope.saveGroupDivide=function() {
		$scope.group_divide_radio_name = $(".group_divide_radio:checked").val();//name
		$scope.groups = "/groups/" + $(".group_divide_radio:checked").attr("groupuuid");//uuid
		$("#group_divide").modal('hide');
		
		$scope.group_divide_radio_name
	};
	*/
	/**
	 * 省市区三级联动
	 */
	//1.页面渲染时load一级地址
/*	personnelAddFactory.getFirstAddressData(function(firstAddressData){
		$scope.firstAddress = [];
		 if(firstAddressData._embedded==undefined){
			 $scope.firstAddress = [];
         }else{
             for(var i=0;i<firstAddressData._embedded.groups.length;i++){
                 var firstName = firstAddressData._embedded.groups[i].name;
                 var firstCodeSplit = firstAddressData._embedded.groups[i]._links.self.href.split("/");
                 var firstCode = firstCodeSplit[firstCodeSplit.length-1];
                 
                 $scope.firstAddress.push({
                     "firstName": firstName,
                     "firstCode": firstCode
                 });
             }
         }
	});*/
	
	//2.一级地址改动时，二级地址加载
/*	$scope.loadSecondAddressData=function(){
		$scope.thirdAddress = [];//清空原三级地址
		var code = $scope.firstAddressCode;
		personnelAddFactory.getNextAddressData({code:code},function(nextAddressData){
			$scope.secondAddress = [];
			 if(nextAddressData._embedded==undefined){
				 return;
	         }else{
	             for(var i=0;i<nextAddressData._embedded.groups.length;i++){
	                 var secondName = nextAddressData._embedded.groups[i].name;
	                 var secondCodeSplit = nextAddressData._embedded.groups[i]._links.self.href.split("/");
	                 var secondCode = secondCodeSplit[secondCodeSplit.length-1];
	                 
	                 $scope.secondAddress.push({
	                     "secondName": secondName,
	                     "secondCode": secondCode
	                 });
	             }
	         }
		});
		
	};*/
	//3.二级地址改动时，三级地址加载
/*	$scope.loadThirdAddressData=function(){
		var code = $scope.secondAddressCode;
		personnelAddFactory.getNextAddressData({code:code},function(nextAddressData){
			$scope.thirdAddress = [];
			 if(nextAddressData._embedded==undefined){
				 return;
	         }else{
	             for(var i=0;i<nextAddressData._embedded.groups.length;i++){
	                 var thirdName = nextAddressData._embedded.groups[i].name;
	                 var thirdCodeSplit = nextAddressData._embedded.groups[i]._links.self.href.split("/");
	                 var thirdCode = thirdCodeSplit[thirdCodeSplit.length-1];
	                 
	                 $scope.thirdAddress.push({
	                     "thirdName": thirdName,
	                     "thirdCode": thirdCode
	                 });
	             }
	         }
		});
		
	};*/
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
/*		$scope.group_divide_radio_name=null;
		$scope.firstAddressCode=null;
		$scope.secondAddressCode=null;
		$scope.thirdAddressCode=null;*/
/*		$("input").each(function() {
			$(this).val("");
		});*/
		//$("textarea").val("");
		
/*		$scope.firstAddressCode="";
		$scope.secondAddressCode="";
		$scope.thirdAddressCode="";*/
		
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
		
		user.gender=$("input[name='gender']:checked").val();
		user.birthDay=$("input[name='birthday']").val();
		//角色
		user.roles=[];
		user.roles.push($scope.selectRole);
		//权限
		//user.permissions = $scope.permissions;
		
		//分组
		//var group = [];
		//var groupVal = $("#group_divide_checked_uuid").val();
		//group.push(groupVal);
		//user.groups = group;
		
		//三级地址
		//user.cityCode = $scope.thirdAddressCode;
		
		personnelAddFactory.create(user,function(response){
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
