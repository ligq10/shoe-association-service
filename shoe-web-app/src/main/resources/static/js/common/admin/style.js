/**
 * Created by Administrator on 15-2-5.
 */

/*onload方法*/
//页面加载后根据浏览器设置模块宽高度
var windowHeight = $(window).height();
var windowWidth= $(window).width();
//左侧导航兰宽度
var left_side_nav_width = $("#left_side_nav").width();
$("#firstAccessLoading").css("margin-left",(windowWidth-800)/2);
$("#firstAccessLoading").css("margin-top",50);
$("#firstAccessLoading").css("display","block");
$(function() {
	//左侧导航高度
	//$("#left_side_nav").height(windowHeight-60);
	//内容栏padding-left
	//$("#replace_left_side_nav").css("padding-left",left_side_nav_width);

	
	//轨迹回放：设置终端列表和地图div高度
	//$(".playback_list").height(windowHeight-120);
    //$(".playback_map").height(windowHeight-120);
    
});

/**
 * 设置index页面左边导航栏高度以及收缩样式
 */
	//左侧导航高度
	//$("#setNavHeight").height(windowHeight-90);
	//内容栏padding-left
	//$("#replace_left_side_nav").css("padding-left",left_side_nav_width);


	//点击左侧导航菜单后添加active样式
	$(".nav li a").click(function(e) {
		
		var $this = $(this);
		if (!$this.hasClass('left_side_nav_active')) {
			$(".left_side_nav_active").removeClass("left_side_nav_active");
			$this.addClass('left_side_nav_active');
		}
		e.preventDefault();
	});
	
	$(".firstTitle").click(function(e) {
		var $this = $(this);
		if (!$this.hasClass('left_side_nav_active')) {
			$(".left_side_nav_active").removeClass("left_side_nav_active");
			$this.addClass('left_side_nav_active');
		}
		if ($this.find(".glyphicon-chevron-down").html() != undefined){
			$this.find(".glyphicon-chevron-down").addClass("glyphicon-chevron-up");
			$this.find(".glyphicon-chevron-down").removeClass("glyphicon-chevron-down");
		}
		else {
			$this.find(".glyphicon-chevron-up").addClass("glyphicon-chevron-down");
			$this.find(".glyphicon-chevron-up").removeClass("glyphicon-chevron-up");
		}
		e.preventDefault();
	});
	
	
	
	//隐藏侧边导航栏
	$("#nav_hide").toggle(
			//隐藏
			function() {
				var left_side_nav_width = $("#left_side_nav").width();
				$("#left_side_nav").animate({left:left_side_nav_width*0.80*(-1)});
				
				$("#hide_left_icon span").removeClass("glyphicon-chevron-left");
				$("#hide_left_icon span").addClass("glyphicon-chevron-right");
				
				/*$("#childContent").removeClass("col-md-12");
				$("#childContent").addClass("col-md-12");
				$("#replace_left_side_nav").css("padding-left",left_side_nav_width*0.2);*/
				var residue = left_side_nav_width*0.80
				$("#replace_left_side_nav").css('width',residue);
				$("#childContent").animate({width:$("#childContent").width()+residue});
			},
			//展现
			function() {

				$("#left_side_nav").animate({left:"0px"});
				
				$("#hide_left_icon span").addClass("glyphicon-chevron-left").removeClass("glyphicon-chevron-right");
				$("#hide_left_icon span").addClass("glyphicon-chevron-left");
				
				/*$("#childContent").removeClass("col-md-12");
				$("#childContent").addClass("col-md-12");
				$("#replace_left_side_nav").css("padding-left",left_side_nav_width);*/
				
				$("#childContent").animate({width:'85%'});
				$("#replace_left_side_nav").css('width','14%');
				
			}
	);
	
	//轨迹回放：设置终端列表和地图div高度
	//$(".playback_list").height(windowHeight-120);
    //$(".playback_map").height(windowHeight-120);
    	

/**
 * 【终端分配】：终端树选择时高亮
 */
function change(obj) {
	 imei = $(obj).text();
	 $(obj).parent().addClass("trbg").siblings().removeClass("trbg"); 
}
/**
 * 【轨迹回放】：点击终端数时获取该终端的uuid号,并复制给form表单的hidden input
 *<td uuid="{{e.uuid}}" onclick="change(this);getImei(this);"><img ng-src="" />{{e.imei}}</td> 
 */
function getImei(obj) {
	uuid = $(obj).attr("uuid");
	$("#hidden_uuid").val(uuid);
}
