/**
 * Created by liguangqiang@changhongit.com 2015-05-15.
 */
/* Services */
'use strict';
var updateFeedbackService=angular.module('updateFeedbackServices',['ngResource']);
var updateFeedbackUrl = '';
updateFeedbackService.factory('updateFeedbackFactory',function($resource){
   var updateFeedbackFactory;
   updateFeedbackFactory=$resource(updateFeedbackUrl,{},{
        // 无条件查询终端列表
        queryList:{
        	url:'/feedbacks/audit',
            method:'GET',
            headers:{
                Accept:'application/hal+json'
            }
        },
        getFeedbackDetailById:{
            url:'/feedbacks/:uuid',
            method:'GET',
            headers:{
            	Accept:'application/hal+json'
            }
       },
       saveAudit:{
        	url:'/audits',
            method:'POST',
            headers:{
                Accept:'application/hal+json'
            }
        },
        update:{
            url:'/terminalusers/:uuid',
            method:'PATCH',
            headers:{
                'Content-Type':'application/json'
            }
        },
        delete:{
            url:'/users/:uuid',
            method:'DELETE'
        },
        resetPassword:{
        	url:'/security/users/password.reset',
            method:'POST',
            headers:{
                'Content-Type':'application/vnd.jiahua.commands.UpdateUserPassword.v1+json'
            }
        },
        getTerminalUserByImeiAndCheckcode:{
        	url:'/terminalusers/search/findByTerminalImeiAndTerminalCheckCode',
        	params:{imei:"imei",checkcode:"checkcode"},
        	method:'GET'
        },
        searchAllRoles:{
        	url: '/roles',
        	method: 'GET',
        	headers: {
        		'Content-Type':'application/json'
        	}
        },
       checkLoginName:{
           url:'/security/users/checkloginname',
           params:{loginname:"loginname"},
           method:'GET'
       },
       getFirstAddressData:{
    	   url:'/groups/100000/childrens',
    	   method:'GET'
       },
       getNextAddressData:{
    	   url:'/groups/:code/childrens',
    	   method:'GET'
       },
       personnelDetail:{
    	   url:'/users/:uuid/view/details',
    	   method:'GET'
       },
       getOneUser:{
    	   url:'/users/search',
           params:{loginname:'loginname'},
    	   method:'GET'
       },
       updateUser:{
    	   url:'/users/command',
    	   method:'POST',
    	   headers:{
    		   'Content-Type':'application/vnd.jiahua.commands.updateAdminUser.v1+json'
    	   }
       },
       updateNormalUser:{
           url:'/users/:userId/passwordupdate',
           method:'POST',
           headers:{
               'Content-Type':'application/vnd.jiahua.commands.updateNormalUser.v1+json'
           }
       },
       searchPermissionsByRoles:{
       	url: '/roles/:roleId/permissions',
       	method: 'GET',
       	headers: {
       		'Content-Type':'application/json'
       	}
       },

    });
    return updateFeedbackFactory;
});
