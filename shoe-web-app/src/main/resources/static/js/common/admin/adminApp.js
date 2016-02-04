/**
 * Created by Administrator on 15-1-28.
 */
'use strict';
var adminApp=angular.module('adminApp',['ui.router','ui.tree','angularFileUpload','personnelAddControllers','primaryAuditFeedbackListControllers','shoecompanyListControllers','shoecompanyAddControllers','shoecompanyUpdateControllers',
                  'primaryAuditFeedbackControllers','middleAuditFeedbackControllers',,'middleAuditFeedbackListControllers','personnelListControllers','personnelUpdateControllers','auditShoeCompanyListControllers','addShoeCompanyAuditControllers']);
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
            permission:'COMPANY_AUDIT_LIST',
            father:'COMPANY_AUDIT_MANAGER'            
        })
        .state('shoecompanylist',{
            url:"/shoecompanylist",
            templateUrl:'templates/shoecompanymanager/sho-company-list.html',
            controller:'shoecompanyListCtrl',
            permission:'COMPANY_AUDIT_LIST',
            father:'COMPANY_AUDIT_MANAGER'            
        })
        .state('shoecompanyadd',{
            url:"/shoecompanyadd",
            templateUrl:'templates/shoecompanymanager/shoe-company-add.html',
            controller:'shoecompanyAddCtrl',
            permission:'COMPANY_AUDIT_LIST',
            father:'COMPANY_AUDIT_MANAGER'            
        })
        .state('shoecompanyupdate',{
            url:"/shoecompanyupdate/:uuid",
            templateUrl:'templates/shoecompanymanager/shoe-company-update.html',
            controller:'shoecompanyUpdateCtrl',
            permission:'COMPANY_AUDIT_LIST',
            father:'COMPANY_AUDIT_MANAGER'            
        })
        .state('shoecompanyaudit',{
            url:"/shoecompanyaudit/:uuid",
            templateUrl:'templates/shoecompanyaudit/add-shoe-company-audit.html',
            controller:'addShoeCompanyAuditCtrl',
            permission:'COMPANY_AUDIT',
            father:'COMPANY_AUDIT_MANAGER'             
        })
        .state('primaryauditfeedbacklist',{
            url:"/primaryauditfeedbacklist",
            templateUrl:'templates/feedbackaudit/primary_audit_feedback_list.html',
            controller:'primaryAuditFeedbackListCtrl',
            permission:'PRIMARY_FEEDBACK_AUDIT_LIST',
            father:'FEEDBACK_AUDIT_MANAGER'             
        })
        .state('primaryauditfeedback',{
            url:"/primaryauditfeedback/:uuid",
            templateUrl:'templates/feedbackaudit/primary_audit_feedback.html',
            controller:'primaryAuditFeedbackCtrl',
            permission:'PRIMARY_FEEDBACK_AUDIT',
            father:'FEEDBACK_AUDIT_MANAGER'             
        })
        .state('middleauditfeedbacklist',{
            url:"/middleauditfeedbacklist",
            templateUrl:'templates/feedbackaudit/middle_audit_feedback_list.html',
            controller:'middleAuditFeedbackListCtrl',
            permission:'MIDDLE_FEEDBACK_AUDIT_LIST',
            father:'FEEDBACK_AUDIT_MANAGER'             
        })
        .state('middleauditfeedback',{
            url:"/middleauditfeedback/:uuid",
            templateUrl:'templates/feedbackaudit/middle_audit_feedback.html',
            controller:'middleAuditFeedbackCtrl',
            permission:'MIDDLE_FEEDBACK_AUDIT',
            father:'FEEDBACK_AUDIT_MANAGER'             
        });
});