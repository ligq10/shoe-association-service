/**
 * Created by Administrator on 15-1-28.
 */
'use strict';
var adminApp=angular.module('adminApp',['ui.router','ui.tree','angularFileUpload','personnelAddControllers',
                  'personnelListControllers','personnelUpdateControllers','auditShoeCompanyListControllers']);
adminApp.config(function($stateProvider,$urlRouterProvider){
// For any unmatched url, redirect to /index
    $urlRouterProvider.otherwise("/index");
    // Now set up the states
    $stateProvider.
        state('index', {
            url: "/index",
            template:"欢迎使用中国鞋材供应网"
        }).
        state('personnellist',{
            url:"/personnellist",
            templateUrl:'templates/personnel/personnel-list.html',
            controller:'personnelListCtrl'
        }).
        state('personneladd',{
            url:"/personneladd",
            templateUrl:'templates/personnel/personnel-add.html',
            controller:'personnelAddCtrl'
        }).
        state('personnelupdate',{
            url:"/personnelupdate",
            templateUrl:'templates/personnel/personnel-update.html',
            controller:'personnelUpdateCtrl'
        }).
        state('auditshoecompanylist',{
            url:"/auditshoecompanylist",
            templateUrl:'templates/shoecompanymanager/audit_shoe_company_list.html',
            controller:'auditShoeCompanyListCtrl'
        });
});