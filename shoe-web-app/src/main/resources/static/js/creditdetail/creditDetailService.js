/**
 * Created by ligq on 2015-12-02.
 */
/* Services */
'use strict';
var creditDetailService=angular.module('creditDetailServices',['ngResource']);
var creditDetailUrl = '';
creditDetailService.factory('creditDetailFactory',function($resource){
    var creditDetailFactory;
    creditDetailFactory=$resource(creditDetailUrl,{},{
    	getShoeComapnyDetailById:{
            url:'/shoecompanies/:uuid',
            method:'GET',
            headers:{
            	Accept:'application/hal+json'
            }
        }
        // 查询树的根节点
/*        getTreeOfParent:{
            url:'/groups/guanhutong',
            method:"GET",
            headers:{
                Accept:'application/json'
            }
        },
        // 查询子节点
        getChildren:{
            url:'/groups/:uuid/childrens',
            method:"GET",
            headers:{
                Accept:'application/json'
            }
        },
        // 无条件查询终端列表
        queryList:{
            method:'GET',
            headers:{
                Accept:'application/json'
            }
        },
        create:{
            method:'POST',
            headers:{
                Accept:'application/json'
            }
        },
        delete:{
            url:'/groups/:uuid',
            method:'DELETE'
        },
        update:{
            url:'/groups/:uuid',
            method:'PATCH',
            headers:{
                Accept:'application/json'
            }
        }
*/    });
    return creditDetailFactory;
});
