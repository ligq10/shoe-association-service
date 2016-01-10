/**
 * Created by lixiang@changhongit.com 2015-05-15.
 */
/* Services */
'use strict';
var auditShoeCompanyListServices=angular.module('auditShoeCompanyListServices',['ngResource']);
var auditShoeCompanyListUrl = '';
auditShoeCompanyListServices.factory('auditShoeCompanyListFactory',function($resource){
   var auditShoeCompanyListFactory;
   auditShoeCompanyListFactory=$resource(auditShoeCompanyListUrl,{},{
        // 无条件查询终端列表
        queryList:{
        	url:'/users/search/byKeyword',
            method:'GET',
            headers:{
                Accept:'application/hal+json'
            }
        } 
    });
    return auditShoeCompanyListFactory;
});
