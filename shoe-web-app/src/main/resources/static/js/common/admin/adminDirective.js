/**
 * 监听路由
 * 
 */
adminApp.run(['$rootScope', 'loginSession', '$state', '$q',function ($rootScope,loginSession,$state, $q) {
	//用户昵称显示
	var loginUser = loginSession.loginUser().userInfo;
	$rootScope.loginUser_name=loginUser.name;
	
	//路由发生改变时 权限判断
	$rootScope.$on('$stateChangeStart',function (event, toState, toParams, fromState, fromParams) {
		if(loginUser.permissions != undefined) {
			var permissions = loginUser.permissions;
			if(toState.permission == 'PASS'){
				
			}else{
				if(toState.permission == 'FORBIDDEN'){
					event.preventDefault();
					$state.go('unauthorized');
				}else{
					if(!permissions.contains(toState.permission)){
						event.preventDefault();
						$state.go('unauthorized');
					}
					
				}
			}
		}
	});
	
	//菜单添加 active 样式
	$rootScope.$on('$stateChangeSuccess',function (event, toState, toParams, fromState, fromParams) {
		var active = toState.permission;
		var grandpa = toState.grandpa;
		var father = toState.father;
		if(father == undefined) {
			$('*[has-permission='+active+']').click();
		} else{
			if(grandpa == undefined) {
				//$('*[has-permission='+parent+']').collapse('show');
				if($('*[has-permission='+father+']').hasClass("no_child")) {
					$(".left_side_nav_active").removeClass("left_side_nav_active");
					$('*[has-permission='+father+']').addClass('left_side_nav_active');
				}else{
					$('*[has-permission='+father+']').children().children().removeClass("glyphicon-chevron-down");
					$('*[has-permission='+father+']').children().children().addClass("glyphicon-chevron-up");
					$('*[has-permission='+active+']').parent().parent().addClass('in');
					$(".left_side_nav_active").removeClass("left_side_nav_active");
					$('*[has-permission='+active+']').addClass('left_side_nav_active');
				}
			}else{
				$('*[has-permission='+grandpa+']').children().children().removeClass("glyphicon-chevron-down");
				$('*[has-permission='+grandpa+']').children().children().addClass("glyphicon-chevron-up");
				$('*[has-permission='+father+']').parent().parent().addClass('in');
				$(".left_side_nav_active").removeClass("left_side_nav_active");
				$('*[has-permission='+father+']').addClass('left_side_nav_active');
			}
				
		}
	});
	
}]); 

/**
 * 权限控制菜单显示
 */
adminApp.directive('hasPermission', function(loginSession) {
	var loginUser = loginSession.loginUser().userInfo;
	return {
		link: function(scope, element, attrs) {
			
			var value = attrs.hasPermission.trim();//has-permisson="GROUP_MANAGER"  value=GROUP_MANAGER
			if(loginUser.permissions != undefined) {
				var permissions = loginUser.permissions
				if(permissions.contains(value)){
					element.show();
				}else{
					element.hide();
					element.addClass("no_permission");
				}
			}
			
			element.bind('click', function() {
				var $this = element;
				if(!$this.hasClass('btn')){
					if (!$this.hasClass('left_side_nav_active')) {
						$(".left_side_nav_active").removeClass("left_side_nav_active");
						$this.addClass('left_side_nav_active');
					}
					if ($this.next(".collapse").hasClass('in') ){
						$this.children().children().addClass("glyphicon-chevron-down");
						$this.children().children().removeClass("glyphicon-chevron-up");
					}else {
						$this.children().children().removeClass("glyphicon-chevron-down");
						$this.children().children().addClass("glyphicon-chevron-up");
					}
				}
            });
    }
  };
});

/**
 * 处理ng-repeat渲染完毕后执行
 * 
 */

adminApp.directive('onFinishRenderFilters', function ($timeout) {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function() {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});

/**
 * validata blur
 * 验证：当失去焦点时就验证（$dirty = true）
 */
adminApp.directive('ngModel', function() {
    return {
        require: 'ngModel',
        link: function(scope, elem, attr, ngModel) {
            elem.on('blur', function() {
                ngModel.$dirty = true;
                scope.$apply();
            });

            ngModel.$viewChangeListeners.push(function() {
                ngModel.$dirty = false;
            });

            scope.$on('$destroy', function() {
                elem.off('blur');
            });
        }
    }
});

/**
 * 电子围栏列表中截取经纬度小数数据显示精确度（显示3位小数）
 */

adminApp.filter('decimalPrecision', function() {
    var decimalPrecisionFilter = function(input) {
        var words = input.split('/');
        for (var i = 0; i < words.length; i++) {
            words[i] = parseFloat(words[i]).toFixed(3);
        }
        return words.join('/');
    };
    return decimalPrecisionFilter;
});

/**
 * 获取登录用户数据 uuid,loginName,nickName,role,group
 */
adminApp.factory('loginSession', function() { 
    var obj = {userInfo:null};
    obj.loginUser = function (){
    	var loginName = $.cookie("userName");
    	if(loginName == undefined || loginName == ''){
    		window.location.href = "reputation/loading";
    	} else {
    		$.ajax({
    			type: "GET",
    			async: false,
    			url: "/employees/byloginid/"+loginName,
    			headers:{
    				'X-Token':$.cookie("X-Token")
    			},
    			success: function(data){
    				
    				var formatData = {};
    				formatData.uuid=data.uuid;
    				formatData.loginName=data.loginName;
    				formatData.name=data.name;
    				var permissions = [];
    				if(data.roles != undefined && data.roles != null) {
    					for(var i = 0; i < data.roles.length; i++){
    						permissions.push(data.roles[i].code);
    					}
    					if(permissions.contains('admin')){
							permissions.push('PERSONNEL_MANAGER');
							permissions.push('PERSONNEL_LIST');
							permissions.push('PERSONNEL_ADD');
							permissions.push('PERSONNEL_UPDATE');
							permissions.push('PERSONNEL_ADD');
							permissions.push('AUDIT_MANAGER');
							permissions.push('SHOE_AUDIT_LIST');
							permissions.push('SHOE_AUDIT');
						}
    					if(permissions.contains('primaryAuditor')){//终端管理
							permissions.push('AUDIT_MANAGER');
							permissions.push('SHOE_AUDIT_LIST');
							permissions.push('SHOE_AUDIT');
						}
    					if(permissions.contains ('middleAuditor')){//只有最顶层管理员可以添加终端
							permissions.push('AUDIT_MANAGER');
							permissions.push('SHOE_AUDIT_LIST');
							permissions.push('SHOE_AUDIT');

    					}
/*    					if(permissions.contains('VOICE_REMINDER')){//语音提醒
    						permissions.push('VOICE_REMINDER_ADD');
    						permissions.push('VOICE_REMINDER_UPDATE');
    					}
    					if(permissions.contains('USER_MANAGER')){//人员管理
    						permissions.push('USER_MANAGER_ADD');
    						permissions.push('USER_MANAGER_UPDATE');
    					}
    					if(permissions.contains('GROUP_MANAGER')){//终端分组
    						permissions.push('GROUP_MANAGER_DIS');
    						permissions.push('GROUP_MANAGER_DES');
    					}
						if(permissions.contains('TERMINAL_SETTING')//终端设置
							|| permissions.contains('VOICE_REMINDER')//语音提醒
							|| permissions.contains('PROTECTED_CIRCLE')//电子围栏
							|| permissions.contains('TRACK_PLAYBACK')//轨迹回放
							|| permissions.contains('DIALING_CALL ')//代打电话
							|| permissions.contains('SHORT_MESSAGE')//代发短信
						){
							permissions.push('TERMINALUSE');
						}
						if(permissions.contains('SERVICER_MANAGER')){//服务商订单
    						permissions.push('SERVICER_MANAGER_QUERY'); 
    						permissions.push('SERVICER_MANAGER_ADD');
    						permissions.push('SERVICER_MANAGER_TYPE');
    					}*/
    				}
    				formatData.permissions=permissions;
    				obj.userInfo = formatData;
    			},
    			error:function(data) {
    			 	$.cookie("X-Token",null,{expires:-1});
    				$.cookie("Authorities",null,{expires:-1});
    				$.cookie("Token-Exp",null,{expires:-1});
    				$.cookie("userName",null,{expires:-1});
    				window.location.replace("/reputation/expire?info="+data.responseText);
    			}
    		});
    	}
    	return obj;
    }
    return obj;    
});

/**
 * 搜索input清空
 * 
 */
adminApp.directive('clearSearch', function () {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
        	element.bind('click', function() {
        		scope.search_keyword='';
    			$("#search_input").val(null);
    			$("#search_input").blur();
    			$("#search_input").focus();
            });
        }
    };
});

/**
 * 侧边导航
 * 
 */
adminApp.directive('leftnav', function () {
    return {
    	restrict:"E",
    	transclude:true,
    	replace:true,
    	templateUrl:"templates/commonTemplate/admin-left-nav.html"
    };
});

/**
 * 权限分配dialog
 */
adminApp.directive("authoritydistributedialog", function (){
	var option={
			restrict:"AEC",
			transclude:true,
			replace:true,
			templateUrl:"templates/commonTemplate/authority-distribution-dialog.html"
	};
	return option;
});