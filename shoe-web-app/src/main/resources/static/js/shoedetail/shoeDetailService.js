/**
 * Created by  on 2015-12-02.
 */
/* Services */
'use strict';
var shoeDetailService=angular.module('shoeDetailServices',['ngResource']);
var shoeDetailUrl = '';
shoeDetailService.factory('shoeDetailFactory',function($resource){
    var shoeDetailFactory;
    shoeDetailFactory=$resource(shoeDetailUrl,{},{
    	saveShoeCompany:{
            method:'POST',
            url:'/shoecompanies',
            headers:{
                Accept:'application/hal+json'
            }
    	},
    	getShoeComapnyDetailById:{
            url:'/shoecompanies/:uuid',
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
        }
    });
    return shoeDetailFactory;
});
