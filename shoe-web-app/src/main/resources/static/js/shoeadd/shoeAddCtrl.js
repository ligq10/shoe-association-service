/**
 * Created by wangjingxue@changhongit.com 2015-02-02.
 */
'use strict';
var shoeAddControllers=angular.module('shoeAddControllers',['shoeAddServices']);

shoeAddControllers.controller('shoeAddCtrl',['$scope','$state','$upload','shoeAddFactory',
    function($scope,$state,$upload,shoeAddFactory) {
	$scope.letterList=['A','B','C','D','E','F','G','H','I','J','K','L',
	               'M','N','O','P','Q','R','S','T','U','V','W','X',
	               'Y','Z'];
	$scope.creditList = [
	                     {level:'0',desc:"优"},
	                     {level:'1',desc:"良"},
	                     {level:'2',desc:"中"},
	                     {level:'3',desc:"差"}
	                     ];
	$scope.sortList = [
	                   {value:"asc",desc:"升序"},
	                   {value:"desc",desc:"降序"}
	                   ];
	
	$scope.submitShoeCompany = function(){
		var permitimage = $scope.permitimage[0];
		var logoimage = $scope.logoimage[0];
		permitimage.id = "permitimage";
		logoimage.id = "logoimage";
		var files = [permitimage,logoimage];
		
  		if($scope.name == null || $scope.name == undefined || $scope.name == ''){
			Message.alert({
				msg : "企业名称不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		
		if($scope.enterpriseLegalPerson == null || $scope.enterpriseLegalPerson == undefined || $scope.enterpriseLegalPerson == ''){
			Message.alert({
				msg : "企业法人不能为空!",
				title : "警告:",
				btnok : '确定',btncl : '取消'
			}, "warn", "small");
		}
		var postEntity={};
		postEntity.name=$scope.name;
		postEntity.address=$scope.address;
		postEntity.enterpriseLegalPerson=$scope.enterpriseLegalPerson;
		postEntity.submitPerson=$scope.submitPerson;
		postEntity.tel=$scope.tel;
		postEntity.files = files;
/*		$scope.upload=$upload.upload({
            url:'/shoecompanies',
            method:'POST',
            header:{
                "Content-Type":"multipart/form-data"
            },
            data:postEntity
            //file:files
        }).success(function(data,status){
                if(status==200){
                	Message.confirm({
          		  			msg: "新增成功！",
                    		title:"成功提示",
                    		btnok: '确定',
                    		btncl:'取消'
          		  		  })
          		  		 .on( function (flag) {
          		  			$state.go("shoeList",{}, {reload: false}); 				  				 
          		   }); 
                }else{
               	 $scope.errorIsHide=false;
                }
        }).error(function(data){
        	Message.alert({
 		    	msg: "新增失败",
		    	title:"警告提示",
		    	btnok: '确定',
		    	btncl:'取消'
          	},"warn","small");
        })*/
		shoeAddFactory.saveShoeCompany(postEntity,function(response){				
			if(response.$resolved){
				$state.go('shoeList');
				Message.alert({
					msg : "新增成功!",
					title : "警告:",
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
}]);

shoeListControllers.directive("deletegroupdialog",
    function (){
        var option={
            restrict:"AEC",
            transclude:true,
            replace:true,
            templateUrl:"templates/commonTemplate/delete-group-dialog.html"
        };
        return option;
    });