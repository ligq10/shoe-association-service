/**
 * Created by liguangqiang@changhongit.com 2015-05-15.
 */
/* Services */
'use strict';
var middleAuditFeedbackService=angular.module('middleAuditFeedbackServices',['ngResource']);
var middleAuditFeedbackUrl = '';
middleAuditFeedbackService.factory('middleAuditFeedbackFactory',function($resource){
   var middleAuditFeedbackFactory;
   middleAuditFeedbackFactory=$resource(middleAuditFeedbackUrl,{},{
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
    return middleAuditFeedbackFactory;
});
