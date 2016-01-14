/**
 * Created by lixiang on 15-5-7.
 */
$(function(){
	
});
/**
 * 日期控件
 */
$('#birthday').datetimepicker({
	locale: 'zh-CN',
    format: 'YYYY-MM-DD'
});
/**
 * 权限分配级联勾选
 */
function clickTreeCheckbox(obj){
	var $this = $(obj);
	var $thisRank = $this.attr("rank");
	if($thisRank == "parent"){
		var children = $this.attr("children");
		if(obj.checked){
			//全选子节点
			$("input[parent='"+children+"']").prop("checked",true);
		}else{
			//取消全选子节点
			$("input[parent='"+children+"']").prop("checked",false);
		}
	}else{
		var parent = $this.attr("parent");
		var all = $("input[parent='"+parent+"']").size();
		var part = $("input:checked[parent='"+parent+"']").size();
		if(part == 0){
			$("input[children='"+parent+"']").prop("indeterminate",false);
			$("input[children='"+parent+"']").prop("checked",false);
		}else{
			if(all == part){
				$("input[children='"+parent+"']").prop("indeterminate",false);
				$("input[children='"+parent+"']").prop("checked",true);
			}else{
				$("input[children='"+parent+"']").prop("indeterminate",true);
			}
			
		}
	}
	var checkedLen = $("input:checked[rank='children']").length;
	if(checkedLen > 0) {
		$("#authorityDistributeBtn").removeAttr("disabled");
	}
	if(checkedLen <= 0) {
		$("#authorityDistributeBtn").attr("disabled","disabled");
	}
}















