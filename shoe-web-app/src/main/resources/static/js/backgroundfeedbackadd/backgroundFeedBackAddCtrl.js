/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var backgroundFeedBackAddControllers=angular.module('backgroundFeedBackAddControllers',['backgroundFeedBackAddServices']);

backgroundFeedBackAddControllers.controller('backgroundFeedBackAddCtrl',['$scope','$timeout','$state','$stateParams','$upload','$rootScope','backgroundFeedBackAddFactory',
    function($scope,$timeout,$state,$stateParams,$upload,$rootScope,backgroundFeedBackAddFactory) {
    $scope.scoreItemList = [
                            {
                            	scoreCode:'creditScore',
                            	scoreDesc:'诚信分'
                            },
                            {
                            	scoreCode:'qualityScore',
                            	scoreDesc:'产品质量分'
                            },{
                            	scoreCode:'serveScore',
                            	scoreDesc:'服务分'
                            }
                            ];	
    $scope.scoreTypeList = [
            {
            	scoreValue:'plus',
            	scoreDesc:'加分'
            },
            {
            	scoreValue:'reduce',
            	scoreDesc:'减分'
            }
    ];
        
	$scope.score = 0;
	
	function checkmobile(string) { 
		if(string == null || string == undefined || string == ''){
			return false;
		}
	    var pattern = /^1[34578]\d{9}$/;  
	    if (pattern.test(string)) {  
	        return true;  
	    }   
	    return false;  
	}; 
	
	
	$scope.plusScore = function(){
		if($scope.score < 20){
			$scope.score = $scope.score+1;
		}
	}
	
	$scope.reduceScore = function(){
		if($scope.score >= 1){
			$scope.score = $scope.score-1;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~搜索查找~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    $scope.currentPageSearch=CURRENTPAGE_INIT;//当前第几页
    $scope.pageSizeSearch=15;//默认每页条数
    $scope.pagingHiddenSearch=true;
    var search_keyword;
    //request data
 	var refreshCompanyListSearch=function(){
    	var search_keyword="";
    	if($scope.search_keyword != undefined){
    		search_keyword=$scope.search_keyword;
    	}
    	var queryEntity = {};
    	queryEntity.keyword = search_keyword;
    	queryEntity.page = $scope.currentPageSearch;
    	queryEntity.size = $scope.pageSizeSearch;
    	queryEntity.auditStatus = 1;
    	backgroundFeedBackAddFactory.searchCompanyList(queryEntity,function(response){
            makeEntrySearch(response);
            $scope.numPagesSearch = function () {
                return $scope.pageSearch.totalPages;
            };
            $scope.currentPageSearch = $scope.pageSearch.number+1;
        });
        
    	
 	}
 	
 	
 	var makeEntrySearch=function(response){
        $scope.entrySearch=[];
        if(response._embedded==undefined){
        	$scope.pagingHiddenSearch=true;
         }else{
            $scope.pagingHiddenSearch=false;
            var allGroup = $scope.allGroup;
            for(var i=0;i<response._embedded.shoeCompanyResponses.length;i++){
    			$scope.entrySearch.push({
    				name:response._embedded.shoeCompanyResponses[i].name,
    				enterpriseLegalPerson:response._embedded.shoeCompanyResponses[i].enterpriseLegalPerson,
    				tel:response._embedded.shoeCompanyResponses[i].tel,
    				uuid:response._embedded.shoeCompanyResponses[i].uuid
    			});
            	
            }
            $scope.pageSearch = response.page;
        }
    };

    // 点击下一页，上一页，首页，尾页按钮
    $scope.pageChangedSearch=function(){
    	refreshCompanyListSearch();
    };
    
    //输入页数
    $scope.clickBlurSearch=function(){
		var pageIndex=$scope.currentPageSearch;
		if(pageIndex >= 1 && pageIndex <= $scope.numPagesSearch()){
			$scope.currentPageSearch=parseInt(pageIndex);
			refreshCompanyListSearch();
		}else if(pageIndex > $scope.numPagesSearch()){
			$scope.currentPageSearch = $scope.numPagesSearch();
			$scope.clickBlurSearch();
		}else if(pageIndex < 1){
			$scope.currentPageSearch = 1;
			$scope.clickBlurSearch();
		}else{
			$scope.currentPageSearch = 1;
			$scope.clickBlurSearch();
		}
	}
    
    //模糊搜索
    $scope.searchCompany=function(){

    	search_keyword = ($scope.search_keyword == undefined)?"":scope.search_keyword;
        $scope.currentPageSearch=CURRENTPAGE_INIT;//当前第几页
    	$scope.pageSizeSearch=15;//默认每页条数
    	$scope.pagingHiddenSearch=true;
    	refreshCompanyListSearch();
    	
    }
	
    $scope.currentCompany = function(item){
    	$scope.shoeCompanyId = item.uuid;
    	$scope.companyName = item.name;
    }
    
	var submit = function(proofFileIds){
		
		var postEntity={};
		postEntity.scoreType=$scope.scoreType;
		postEntity.score=$scope.score;
		postEntity.scoreReason=$scope.scoreReason;
		postEntity.submitPerson=$scope.submitPerson;
		postEntity.submitTel=$scope.submitTel;
		postEntity.proofFileIds = proofFileIds;
		postEntity.scoreItem=$scope.scoreItem;

		backgroundFeedBackAddFactory.saveFeedback({uuid: $scope.shoeCompanyId},postEntity,function(response){				
			if(response.$resolved){
				$state.go('shoeList');
				Message.alert({
					msg : "新增成功!",
					title : "提示:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
				
			}else{
				Message.alert({
					msg : "新增失败!",
					title : "警告:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
			}
	 	 },function(error){	
				Message.alert({
					msg : "新增失败!",
					title : "警告:",
					btnok : '确定',btncl : '取消'
				}, "warn", "small");
	 	 });
	}
	
	var fileUpload = function(files){
		$scope.fileIds = [];
		if(files == null || files == undefined || files.length == 0){
			Message.alert({
				msg : "评论证据为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
			return false;
		}

		$scope.upload=$upload.upload({
	        url:'/images/multipartfile',
	        method:'POST',
	        header:{
	            "Content-Type":"multipart/form-data"
	        },
	        //data:postEntity
	        file:files
	    }).success(function(response, status, headers, config){
	            if(status==201){
            		$scope.fileIds = response;
            		submit($scope.fileIds);
	            }else{
	    	    	Message.alert({
	    			    msg: "评论证据上传失败！",
	    		    	title:"警告提示",
	    		    	btnok: '确定',
	    		    	btncl:'取消'
	    	      	},"warn","small");
	            }
	    }).error(function(data){
	    	Message.alert({
			    msg: "评论证据上传失败！",
		    	title:"警告提示",
		    	btnok: '确定',
		    	btncl:'取消'
	      	},"warn","small");
	    })
	}

	$scope.submitFeedback = function(){
		var proofFiles = $scope.proofFile;
		fileUpload(proofFiles);
	}
	
}]);

backgroundFeedBackAddControllers.directive("callSearchPage", function () {
	var option = {
			restrict: "E",
			templateUrl: "templates/commonTemplate/call-search-page.html"
	};
	return option;
});