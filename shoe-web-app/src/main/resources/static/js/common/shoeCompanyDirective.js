/**
 * 监听路由
 * 
 */
/*shoeApp.run(['$rootScope','$location','loginSession', '$state', '$q', function ($rootScope,$location, loginSession, $state, $q) {
    // 用户昵称显示
    var loginUser = loginSession.loginUser().userInfo;
    $rootScope.loginUser=loginUser;
    
    // 路由发生改变时 权限判断
    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
    	// 路由更改时 获取参数
        var searchObject = $location.search();
       
        if(searchObject&&searchObject.imei){
        	
        	$rootScope.customer_imei=searchObject.imei;
        	
        	var initCurrentTerminal = {"ownerName":"","imei":"","sim":""};
        	
        	var currentTerminal={};
            $.ajax({
                type: "GET",
                async: false,
                url: "/cp150s/search/findByImei?imei=" + searchObject.imei,
                headers: {
                    'X-Token': $.cookie("X-Token")
                },
                success: function (data) {                   
                	currentTerminal = data._embedded.cp150s[0];
                },
                error: function () {
                	currentTerminal={"ownerName":"","imei":"","sim":""};
                }
            });
            
            $.extend(initCurrentTerminal,currentTerminal);//扩展合并
            
            $rootScope.currentTerminal=initCurrentTerminal;
        }else{
        	if(!$rootScope.currentTerminal){
        		$rootScope.currentTerminal=initCurrentTerminal;
        	}       	
        }
        
        if (loginUser.permissions != undefined) {
            var permissions = loginUser.permissions;
            if (toState.permission == 'PASS') {

            } else {
                if (toState.permission == 'FORBIDDEN') {
                    event.preventDefault();
                    $state.go('unauthorized');
                } else {
                    if (!permissions.contains(toState.permission)) {
                        event.preventDefault();
                        $state.go('unauthorized');
                    }

                }
            }
        }
    });
*/
    // 菜单添加 active 样式
/*    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
        var active = toState.permission;
        var grandpa = toState.grandpa;
        var father = toState.father;
        if (father == undefined) {
            $('*[has-permission=' + active + ']').click();
        } else {
            if (grandpa == undefined) {
                // $('*[has-permission='+parent+']').collapse('show');
                if ($('*[has-permission=' + father + ']').hasClass("no_child")) {
                    $(".left_side_nav_active").removeClass("left_side_nav_active");
                    $('*[has-permission=' + father + ']').addClass('left_side_nav_active');
                } else {
                    $('*[has-permission=' + father + ']').children().children().removeClass("glyphicon-chevron-down");
                    $('*[has-permission=' + father + ']').children().children().addClass("glyphicon-chevron-up");
                    $('*[has-permission=' + active + ']').parent().parent().addClass('in');
                    $(".left_side_nav_active").removeClass("left_side_nav_active");
                    $('*[has-permission=' + active + ']').addClass('left_side_nav_active');
                }
            } else {
                $('*[has-permission=' + grandpa + ']').children().children().removeClass("glyphicon-chevron-down");
                $('*[has-permission=' + grandpa + ']').children().children().addClass("glyphicon-chevron-up");
                $('*[has-permission=' + father + ']').parent().parent().addClass('in');
                $(".left_side_nav_active").removeClass("left_side_nav_active");
                $('*[has-permission=' + father + ']').addClass('left_side_nav_active');
            }

        }
    });

}]);
*/
/**
 * 权限控制菜单显示
 */
/*shoeApp.directive('hasPermission', function (loginSession) {
    var loginUser = loginSession.loginUser().userInfo;
    return {
        link: function (scope, element, attrs) {

            var value = attrs.hasPermission.trim();// has-permisson="GROUP_MANAGER"
													// value=GROUP_MANAGER
            if (loginUser.permissions != undefined) {
                var permissions = loginUser.permissions
                if (permissions.contains(value)) {
                    element.show();
                } else {
                    element.hide();
                    element.addClass("no_permission");
                }
            }

            element.bind('click', function () {
                var $this = element;
                if (!$this.hasClass('btn')) {
                    if (!$this.hasClass('left_side_nav_active')) {
                        $(".left_side_nav_active").removeClass("left_side_nav_active");
                        $this.addClass('left_side_nav_active');
                    }
                    if ($this.next(".collapse").hasClass('in')) {
                        $this.children().children().addClass("glyphicon-chevron-down");
                        $this.children().children().removeClass("glyphicon-chevron-up");
                    } else {
                        $this.children().children().removeClass("glyphicon-chevron-down");
                        $this.children().children().addClass("glyphicon-chevron-up");
                    }
                }
            });
        }
    };
});
*/
/**
 * 处理ng-repeat渲染完毕后执行
 * 
 */

shoeApp.directive('onFinishRenderFilters', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    };
});

/**
 * validata blur 验证：当失去焦点时就验证（$dirty = true）
 */
shoeApp.directive('ngModel', function () {
    return {
        require: 'ngModel',
        link: function (scope, elem, attr, ngModel) {
            elem.on('blur', function () {
                ngModel.$dirty = true;
                scope.$apply();
            });

            ngModel.$viewChangeListeners.push(function () {
                ngModel.$dirty = false;
            });

            scope.$on('$destroy', function () {
                elem.off('blur');
            });
        }
    }
});

/**
 * 电子围栏列表中截取经纬度小数数据显示精确度（显示3位小数）
 */

/*
 * ordermanApp.filter('decimalPrecision', function () { var
 * decimalPrecisionFilter = function (input) { var words = input.split('/'); for
 * (var i = 0; i < words.length; i++) { words[i] =
 * parseFloat(words[i]).toFixed(3); } return words.join('/'); }; return
 * decimalPrecisionFilter; });
 */

/**
 * 获取登录用户数据 uuid,loginName,nickName,role,group
 */
/*shoeApp.factory('loginSession', function () {
    var obj = {userInfo: null};
    obj.loginUser = function () {
        var loginName = $.cookie("userName");
        if (loginName == undefined || loginName == '') {
            window.location.href = "/orderman/loading";
        } else {
        	var formatData = {};
            $.ajax({
                type: "GET",
                async: false,
                url: "/users/search?loginname=" + loginName,
                headers: {
                    'X-Token': $.cookie("X-Token")
                },
                success: function (data) {                   
                    formatData.uuid = data.uuid;
                    formatData.loginName = data.loginName;
                    formatData.nickName = data.nickName;
                    formatData.role = data.roles[0].name;
                    formatData.group = data.groups[0].uuid;
                    var permissions = [];
                    if (data.permissions != undefined) {
                        for (var i = 0; i < data.permissions.length; i++) {
                            permissions.push(data.permissions[i].name);
                        }
                        if (permissions.contains('SOS_SETTING') || permissions.contains('SERVICE_MANAGER') || permissions.contains('CALLCENTER_MANAGER')) {
                            permissions.push('MANAGELIST');
                        }
                        if (permissions.contains('TERMINAL_MANAGER')) {// 终端管理
                            permissions.push('TERMINAL_MANAGER_UPDATE');
                        }
                        if (data.groups[0].uuid == 'guanhutong') {// 只有最顶层管理员可以添加终端
                            permissions.push('TERMINAL_MANAGER_ADD');
                        }
                        if (permissions.contains('VOICE_REMINDER')) {// 语音提醒
                            permissions.push('VOICE_REMINDER_ADD');
                            permissions.push('VOICE_REMINDER_UPDATE');
                        }
                        if (permissions.contains('USER_MANAGER')) {// 人员管理
                            permissions.push('USER_MANAGER_ADD');
                            permissions.push('USER_MANAGER_UPDATE');
                        }
                        if (permissions.contains('GROUP_MANAGER')) {// 终端分组
                            permissions.push('GROUP_MANAGER_DIS');
                        }
                        if (permissions.contains('TERMINAL_SETTING')// 终端设置
                            || permissions.contains('VOICE_REMINDER')// 语音提醒
                            || permissions.contains('PROTECTED_CIRCLE')// 电子围栏
                            || permissions.contains('TRACK_PLAYBACK')// 轨迹回放
                            || permissions.contains('DIALING_CALL ')// 代打电话
                            || permissions.contains('SHORT_MESSAGE')// 代发短信
                        ) {
                            permissions.push('TERMINALUSE');
                        }
                        if (permissions.contains('SERVICER_ORDER')) {// 订单管理
                                permissions.push('SERVICER_ORDER_ADD');//添加订单
                                permissions.push('SERVICER_ORDER_QUERY');//查询订单
                                permissions.push('SERVICER_ORDER_MESSAGE');//订单消息
                        }
                    }
                    formatData.permissions = permissions;
                    obj.userInfo = formatData;
                },
                error: function () {
                	$.cookie("X-Token",null,{expires:-1});
    				$.cookie("Authorities",null,{expires:-1});
    				$.cookie("Token-Exp",null,{expires:-1});
    				$.cookie("userName",null,{expires:-1});
    				window.location.replace("/orderman/expire?info="+data.responseText);
                }
            });
*/          // 获取第三方插件信息（联通/铁通）
/*    		$.ajax({
    			type: "GET",
    			async: false,
    			url: "/groups/"+formatData.group+"/callCenter",
    			headers:{
    				'X-Token':$.cookie("X-Token")
    			},
    			success: function(data){
    				if(data.name == 'TIETONG'){
    					formatData.toolBar = 'TIETONG';
    				}else if(data.name == 'BEIJING_UNICOM'){
    					formatData.toolBar = 'BEIJING_UNICOM';
    				}else{
    					formatData.toolBar = null;
    				}
    			},
    			error:function(data){
    				formatData.toolBar = null;
    			}
    		});
    		obj.userInfo = formatData;
        }
        return obj;
    };
    return obj;
});
*/
/**
 * 搜索input清空
 * 
 */
shoeApp.directive('clearSearch', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            element.bind('click', function () {
                scope.search_keyword = '';
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
/*shoeApp.directive('leftnav', function () {
    return {
        restrict: "E",
        transclude: true,
        replace: true,
        templateUrl: "templates/commonTemplate/call-left-nav.html"
    };
});
shoeApp.directive('searchServicePage', function () {
    return {
        restrict: "AEC",
        replace: true,
        templateUrl: "templates/commonTemplate/page.html"
    };
});
shoeApp.directive('servicePage', function () {
    return {
        restrict: "AEC",
        replace: true,
        templateUrl: "templates/commonTemplate/map-search-page.html"
    };
});
shoeApp.directive('orderCompanyPage', function () {
    return {
        restrict: "AEC",
        replace: true,
        templateUrl: "templates/commonTemplate/order-company-page.html"
    };
});
*/
shoeApp.directive('companyListPage', function () {
    return {
        restrict: "AEC",
        replace: true,
        templateUrl: "templates/commonTemplate/company-list-page.html"
    };
});

/*shoeApp.directive('orderWaiterLocation', function () {
    return {
        restrict: "AEC",
        replace: true,
        templateUrl: "templates/orderList/order_waiter_location.html"
    };
});
*/
/**
 * 时间控件model
 */
shoeApp.directive('dateTimeModel', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ngModel) {
            elem.on('blur', function () {
                ngModel.$setViewValue(elem.val());
            });
        }
    }
});
shoeApp.factory('poiSession', function () {
    var service = {};
    service.poi = null;
    return service;
});

/**
 * 铁通插件
 */
/*shoeApp.directive('tietongToolbar', function () {
	return {
		restrict:"E",
    	transclude:true,
    	replace:true,
    	templateUrl:"templates/commonTemplate/tietong-toolbar.html"
	}
});*/
/**
 * 联通插件
 */
/*shoeApp.directive('unicomToolbar', function () {
	return {
		restrict:"E",
		transclude:true,
		replace:true,
		templateUrl:"templates/commonTemplate/unicom-toolbar.html"
	}
});*/
