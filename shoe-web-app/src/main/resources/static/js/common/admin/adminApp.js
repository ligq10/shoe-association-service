/**
 * Created by Administrator on 15-1-28.
 */
'use strict';
var adminApp=angular.module('adminApp',['ui.router','ui.tree','angularFileUpload','personnelAddControllers',
                  'personnelListControllers','personnelUpdateControllers','auditShoeCompanyListControllers','addShoeCompanyAuditControllers']);
adminApp.config(function($stateProvider, $urlRouterProvider, $httpProvider,$rootScopeProvider){
    $httpProvider.defaults.headers.common['X-Token'] = $.cookie('X-Token');

// For any unmatched url, redirect to /index
    $urlRouterProvider.otherwise("/index");
    // Now set up the states
    $stateProvider.
        state('index', {
            url: "/index",
            template:"欢迎使用中国鞋材供应网",
            permission: 'PASS'
        })
        .state('unauthorized', {
            url: "/unauthorized",//无权限
            template: "<div class='alert alert alert-danger'>您没有权限访问此页面</div>",
            permission: 'PASS'
        })
        .state('personnellist',{
            url:"/personnellist",
            templateUrl:'templates/personnel/personnel-list.html',
            controller:'personnelListCtrl',
            permission:'PERSONNEL_LIST',
            father:'PERSONNEL_MANAGER'
        })
        .state('personneladd',{
            url:"/personneladd",
            templateUrl:'templates/personnel/personnel-add.html',
            controller:'personnelAddCtrl',
            permission:'PERSONNEL_ADD',
            father:'PERSONNEL_MANAGER'            
        })
        .state('personnelupdate',{
            url:"/personnelupdate/:uuid",
            templateUrl:'templates/personnel/personnel-update.html',
            controller:'personnelUpdateCtrl',
            permission:'PERSONNEL_UPDATE',
            father:'PERSONNEL_MANAGER'
        })
        .state('auditshoecompanylist',{
            url:"/auditshoecompanylist",
            templateUrl:'templates/shoecompanymanager/audit_shoe_company_list.html',
            controller:'auditShoeCompanyListCtrl',
            permission:'SHOE_AUDIT_LIST',
            father:'AUDIT_MANAGER'            
        })
        .state('shoecompanyaudit',{
            url:"/shoecompanyaudit/:uuid",
            templateUrl:'templates/shoecompanyaudit/add-shoe-company-audit.html',
            controller:'addShoeCompanyAuditCtrl',
            permission:'SHOE_AUDIT',
            father:'AUDIT_MANAGER'             
        });
});