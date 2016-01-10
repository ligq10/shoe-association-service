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
	var uuid=$stateParams.uuid;
	//用户管理员角色
	var checkedPermissions=[];
	//查询某个用户全部信息
	personnelUpdateFactory.personnelDetail({uuid:uuid},function(response){
		$scope.personnel={};
        if(response==undefined){
        }else{
        	$scope.userId=response.uuid;
        	$scope.loginName=response.loginName;
        	$scope.email=response.email;
        	$scope.tel=response.tel;
        	
        	$scope.nickName=response.nickName;
        	
        	$scope.selectRole = "/roles/"+response.roles[0].uuid;
        	if($scope.selectRole) {
    			//加载对应权限
    			var roleIds = {};
    			var modules = [];
    			var getrole=$scope.selectRole.split("/");
    			var roleId=getrole[getrole.length-1];
    			var permissionIds={};
    			personnelUpdateFactory.searchPermissionsByRoles({roleId:roleId},function(permissionsAll){
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
        	//权限
        	var permissions=[];
        	for(var j = 0; j<response.permissions.length;j++){
        		permissions.push("/permissions/"+response.permissions[j].uuid);
        		checkedPermissions.push("/permissions/"+response.permissions[j].uuid);
        	}
        	$scope.permissions=permissions;
        	
        	
        	for(var i= 0; i<response.profiles.length;i++) {
        		if(response.profiles[i].pName == "address"){
        			$scope.address=response.profiles[i].pValue;
            	}
        		if(response.profiles[i].pName == "gender"){
        			$scope.gender=response.profiles[i].pValue;
        			if($scope.gender == "male"){
        				$("#male").attr("checked","checked");
        			}
        			if($scope.gender == "female"){
        				$("#female").attr("checked","checked");
        			}
            	}
        		if(response.profiles[i].pName == "cityCode"){
        			$scope.cityCode=response.profiles[i].pValue;//411202
        			
        			$scope.firstAddressCode =$scope.cityCode.substring(0,2)+"0000";//41+0000
        			
        			personnelUpdateFactory.getNextAddressData({code:$scope.firstAddressCode},function(nextAddressData){
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
        			
        			
        			$scope.secondAddressCode =$scope.cityCode.substring(0,4)+"00";//411200
        			personnelUpdateFactory.getNextAddressData({code:$scope.secondAddressCode},function(nextAddressData){
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
        			$scope.thirdAddressCode =$scope.cityCode;//411202
            	}
        		if(response.profiles[i].pName == "birthday"){
        			$scope.birthDay=response.profiles[i].pValue;
            	}
        		if(response.profiles[i].pName == "remarks"){
        			$scope.remarks=response.profiles[i].pValue;
            	}
        	}
        	
        	//分组
        	for(var m = 0; m<response.groups.length;m++) {
        		$scope.groups="/groups/"+response.groups[m].uuid;
        		$scope.group_divide_radio_name=response.groups[m].name;
        	}
        	
        }
 });
	
	//权限菜单
	personnelUpdateFactory.searchAllRoles(function(responseRole){
		$scope.roleAdmins = [];
		var rolesAdminArry =[];
		if(responseRole._embedded==undefined){
			$scope.roleAdmins = [];
		}else{
			for(var i=0; i<responseRole._embedded.roles.length; i++) {
				if(responseRole._embedded.roles[i].description == "运营管理员"){
					var getrole = responseRole._embedded.roles[i]._links.self.href.split("/");
					var roleId = "/roles/" + getrole[getrole.length-1];
					$scope.roleAdmins.push({
						roleAdminId:roleId,roleAdminName:"运营管理员"
					});
				}else if(responseRole._embedded.roles[i].description == "CallCenter管理员"){
					var getrole = responseRole._embedded.roles[i]._links.self.href.split("/");
					var roleId = "/roles/" + getrole[getrole.length-1];
					$scope.roleAdmins.push({
						roleAdminId:roleId,roleAdminName:"CallCenter管理员"
					});
				}else if(responseRole._embedded.roles[i].description == "指挥中心管理员"){
					var getrole = responseRole._embedded.roles[i]._links.self.href.split("/");
					var roleId = "/roles/" + getrole[getrole.length-1];
					$scope.roleAdmins.push({
						roleAdminId:roleId,roleAdminName:"指挥中心管理员"
					});
				}
			}
			
			
		}
   });
	
	/**
	 * 权限树加载完后执行：部分选择
	 */
	$scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
		if(checkedPermissions == null){
			 $("input:checkbox").prop("checked",true);
		}else{
			var allBox = $("input:checkbox[rank='children']");
			for(var m = 0; m < checkedPermissions.length; m++) {
				for(var n = 0; n < allBox.length; n++) {
					if(allBox[n].value == checkedPermissions[m]){
						$(allBox[n]).click();
					}
				}
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
	 * 当改变role清除上次的权限选择并加载对应权限
	 */
	$scope.resetCheckedRoles=function(){
		$scope.defoutPermissionsPrompt = true;
		checkedPermissions = null;
		//清除上次的权限选择
		$("#authority_distribution_checked_permissions").val("");
		
		if($scope.selectRole) {
			//加载对应权限
			var roleIds = {};
			var modules = [];
			var getrole=$scope.selectRole.split("/");
			var roleId=getrole[getrole.length-1];
			var permissionIds={};
			personnelFactory.searchPermissionsByRoles({roleId:roleId},function(permissionsAll){
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
	 * 选择分组dialog
	 */
	$scope.groupDivide=function(){
		//保存按钮初始化disabled
		$("#checkGroup_confirm_btn").attr("disabled","disabled");
		

		var loginUser = loginSession.loginUser().userInfo;
	     /**
	 	 * 登录用户管理的所有分组
	 	 */
	     groupFactory.getTree({groupid:loginUser.group},function(response){
	 		getTree(response);
	 	 });
	     
	    var getTree = function(response) {
	    	$scope.list=[];
	 		formatData(response);
	 		$scope.list.push(response);
	 	}
	    var isTopGroup = true;
	 	var formatData = function(response){
	 		if(isTopGroup == true){
	 			response['editEnabled']=false;
	 	 		response['deleteEnabled']=false;
	 		}else{
	 			response['editEnabled']=true;
	 	 		response['deleteEnabled']=true;
	 		}
	 		response['addGroupInputClass']='hide';
	 		response['editGroupInputClass']='hide';
	 		response['childrensLoaded']=true;
	 		response['addEnabled']=true;
	 		response['items']=response.childrens;
	 		delete response.childrens;
	 		response['title']=response.name;
	 		delete response.name;
	 		response['uuid']=response.id;
	 		delete response.id;
	 			
	 		isTopGroup = false;
			if(response.items != undefined){
				for(var i = 0; i < response.items.length; i++) {
					formatData(response.items[i]);
				}
			}
	 	}

	    $scope.loadChilren = function(scope) {
	     var nodeData = scope.$modelValue;

	     //加载子节点
	     if (!nodeData.childrensLoaded) {
	         groupFactory.getChildren({uuid:nodeData.uuid},function(response){
	             if(response._embedded==undefined){
	             }else{
	                 for(var i=0;i<response._embedded.groups.length;i++){
	                     var childrensLink=response._embedded.groups[i]._links.self.href;
	                     var childrensLinkArr=childrensLink.split("/");
	                     var childrensLinkArrLength=childrensLinkArr.length;
	                     var uuid=childrensLinkArr[childrensLinkArrLength-1];
	                     nodeData.items.push({
	                         "id": nodeData.id * 10 + nodeData.items.length,
	                         "title": response._embedded.groups[i].name,
	                         "childrens" : response._embedded.groups[i]._links.childrens.href,
	                         "uuid": uuid,
	                         "childrensLoaded" : false,
	                         "items":[],
	                     });
	                 }
	             }
	         });
	        nodeData.childrensLoaded = true;
	     }
	     scope.toggle();

	     };
	};//选择分组结束
	
	/**
	 * 未选择分组是保存按钮disabled
	 */
	$scope.checkGroup=function(){
		$("#checkGroup_confirm_btn").removeAttr("disabled");
	};
	
	/**
	 * 保存选择的分组，填充到表单
	 */
	$scope.saveGroupDivide=function() {
		$scope.group_divide_radio_name = $(".group_divide_radio:checked").val();//name
		$scope.groups = "/groups/" + $(".group_divide_radio:checked").attr("groupuuid");//uuid
		$("#group_divide").modal('hide');
		
	};
	
	/**
	 * 省市区三级联动
	 */
	//1.页面渲染时load一级地址
	personnelFactory.getFirstAddressData(function(firstAddressData){
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
	});
	
	//2.一级地址改动时，二级地址加载
	$scope.loadSecondAddressData=function(){
		$scope.thirdAddress = [];//清空原三级地址
		var code = $scope.firstAddressCode;
		personnelFactory.getNextAddressData({code:code},function(nextAddressData){
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
		
	};
	//3.二级地址改动时，三级地址加载
	$scope.loadThirdAddressData=function(){
		var code = $scope.secondAddressCode;
		personnelFactory.getNextAddressData({code:code},function(nextAddressData){
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


/**
 * 权限分配dialog
 */
personnelUpdateControllers.directive("authoritydistribute", function (){
	var option={
			restrict:"AEC",
			transclude:true,
			replace:true,
			templateUrl:"templates/commonTemplate/authority-distribution.html"
	};
	return option;
});





