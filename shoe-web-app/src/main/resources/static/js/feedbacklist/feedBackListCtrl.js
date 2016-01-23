/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var feedBackListControllers=angular.module('feedBackListControllers',['feedBackListServices']);

feedBackListControllers.controller('feedBackListCtrl',['$scope','$stateParams','$upload','$rootScope','feedBackListFactory',
    function($scope,$stateParams,$upload,$rootScope,feedBackListFactory) {
    $scope.shoeComapnyId = $stateParams.uuid;
	
	$scope.feedbackList = [];
    $scope.pageSize=PAGESIZE_DEFAULT;
	$scope.currentPage=CURRENTPAGE_INIT;
	
	var initFeedbackList = function(){
		var queryEntity = {
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshFeedbackList(queryEntity);
	}
	
	/**
	 * 刷新订单列表
	 */
	var refreshFeedbackList = function(queryEntity){
		
		feedBackListFactory.queryFeedbackByShoeId({uuid:$scope.shoeComapnyId},queryEntity,function(response){
			if(response.$resolved){
	            if(response._embedded==undefined){
	            	$scope.feedbackList = [];
	            	$scope.currentPage = 0;
	            	$scope.numPages = 0;
	            	$scope.pageSize = 0;
	            	return false;
	            }else{
	            	$scope.feedbackList=[];
	            	$scope.page=response.page;
	                $scope.numPages = function () {
	                    return response.page.totalPages;
	                };
	                for(var i=0;i< response._embedded.feedbackResponses.length;i++){
	                	$scope.feedbackList[i]= response._embedded.feedbackResponses[i];
	                }
	            }

			}else{
	        	$scope.feedbackList = [];
	        	$scope.currentPage = 0;
	        	$scope.numPages = 0;
	        	$scope.pageSize = 0;
			}

		});
	}					

	initFeedbackList();
	
    //shoe翻页 点击下一页，上一页，首页，尾页按钮
    $scope.pageChanged=function(){
		var queryEntity = {
				page:$scope.currentPage,
				size:$scope.pageSize
		}
		refreshFeedbackList(queryEntity);
    }
}]);

/*feedBackAddControllers.directive("deletegroupdialog",
    function (){
        var option={
            restrict:"AEC",
            transclude:true,
            replace:true,
            templateUrl:"templates/commonTemplate/delete-group-dialog.html"
        };
        return option;
    });*/