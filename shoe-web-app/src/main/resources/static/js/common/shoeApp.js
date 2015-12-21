/**
 * Created by Administrator on 15-1-28.
 */
'use strict';
var shoeApp=angular.module('shoeApp',['ui.router','ui.tree','angularFileUpload',,'shoeListControllers','shoeAddControllers','creditDetailControllers','feedBackAddControllers']);
shoeApp.config(function($stateProvider,$urlRouterProvider){
// For any unmatched url, redirect to /index
    $urlRouterProvider.otherwise("/index");
    // Now set up the states
    $stateProvider.
        state('index', {
            url: "/index",
            templateUrl:'templates/shoe/shoe_list.html',
            controller:'shoeListCtrl'
        }).
        state('shoeList',{
            url:"/shoeList",
            templateUrl:'templates/shoe/shoe_list.html',
            controller:'shoeListCtrl'
        }).
        state('shoeAdd',{
            url:'/shoeAdd',
            templateUrl:'templates/shoe/shoe_add.html',
            controller:'shoeAddCtrl'
        }).
        state('feedbackAdd',{
            url:'/feedbackAdd',
            templateUrl:'templates/shoe/feedback_add.html',
            controller:'feedBackAddCtrl'
        }).
        state('creditDetail',{
            url:"/creditDetail",
            templateUrl:'templates/shoe/credit_detail.html',
            controller:'creditDetailCtrl'
        });
/*        state('terminalAdd',{
            url:'/terminalAdd',
            templateUrl:'templates/terminal/terminal-add.html',
            controller:'terminalAddCtrl'
        }).
        state('terminalUpdate',{
            url:'/terminalUpdate/:uuid',
            templateUrl:'templates/terminal/terminal-update.html',
            controller:'terminalUpdateCtrl'
        }).
		state('groupList',{
	        url:"/groupList",
	        templateUrl:'templates/group/group-list.html',
	        controller:'groupListCtrl'
	    }).
	    state('manageList',{
	        url:"/manageList",
	        templateUrl:'templates/manage/manage-list.html',
	        controller:'manageListCtrl'
	    }).
	    state('sosSet',{
	        url:"/sosSet",
	        templateUrl:'templates/manage/sosSet.html',
	        controller:'sosSetCtrl'
	    }).
	    state('serviceTelSet',{
	        url:"/serviceTelSet",
	        templateUrl:'templates/manage/serviceTelSet.html',
	        controller:'serviceTelSetCtrl'
	    }).
	    state('terminalSet',{
	        url:"/terminalSet",
	        templateUrl:'templates/apply/terminal-Set.html',
	        controller:'terminalSetCtrl'
	    }).
	    state('voiceReminder',{
	        url:"/voiceReminder",
	        templateUrl:'templates/apply/voice-reminder.html',
	        controller:'voiceReminderCtrl'
	    }).
	    state('voiceReminderAdd',{
	        url:"/voiceReminderAdd",
	        templateUrl:'templates/apply/voice-reminder-add.html',
	        controller:'voiceReminderAddCtrl'
	    }).
	    state('voiceReminderUpdate',{
	        url:"/voiceReminderUpdate/:imei/:reminderTime/:mode/:action/:content/:status",
	        templateUrl:'templates/apply/voice-reminder-update.html',
	        controller:'voiceReminderUpdateCtrl'
	    });*/
});