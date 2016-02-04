/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
/* Services */
'use strict';
var shoecompanyListService=angular.module('shoecompanyListServices',['ngResource']);
var shoecompanyUrl = '';
shoecompanyListService.factory('shoecompanyListFactory',function($resource){
   var shoecompanyListFactory;
   shoecompanyListFactory=$resource(shoecompanyUrl,{},{
        // 无条件查询终端列表
        queryList:{
        	url:'/shoecompanies/withoutaudit',
            method:'GET',
            headers:{
                Accept:'application/hal+json'
            }
        },
        deleteShoeCompany:{
            url:'/shoecompanies/:uuid',
            method:'DELETE'
        },
       checkLoginName:{
           url:'/security/users/checkloginname',
           params:{loginname:"loginname"},
           method:'GET'
       }
    });
    return shoecompanyListFactory;
});
