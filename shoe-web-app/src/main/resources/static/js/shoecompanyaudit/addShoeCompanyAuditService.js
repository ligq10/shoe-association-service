/**
 * Created by  on 2015-12-02.
 */
/* Services */
'use strict';
var addShoeCompanyAuditService=angular.module('addShoeCompanyAuditServices',['ngResource']);
var addShoeCompanyAuditUrl = '';
addShoeCompanyAuditService.factory('addShoeCompanyAuditFactory',function($resource){
    var addShoeCompanyAuditFactory;
    addShoeCompanyAuditFactory=$resource(addShoeCompanyAuditUrl,{},{
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
    return addShoeCompanyAuditFactory;
});
