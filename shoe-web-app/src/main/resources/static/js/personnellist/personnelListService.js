/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
/* Services */
'use strict';
var personnelListService=angular.module('personnelListServices',['ngResource']);
var personnelUrl = '';
personnelListService.factory('personnelListFactory',function($resource){
   var personnelListFactory;
   personnelListFactory=$resource(personnelUrl,{},{
        // 无条件查询终端列表
        queryList:{
        	url:'/employees',
            method:'GET',
            headers:{
                Accept:'application/hal+json'
            }
        },
       registerUser:{
           url:'/users/register',
           method:'POST',
           headers:{
               'Content-Type':'application/vnd.jiahua.commands.registerUser.v1+json'
           }
       },
        deleteUser:{
            url:'/employees/:uuid',
            method:'DELETE'
        },
       checkLoginName:{
           url:'/security/users/checkloginname',
           params:{loginname:"loginname"},
           method:'GET'
       }
    });
    return personnelListFactory;
});
