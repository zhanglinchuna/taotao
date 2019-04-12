var TT = TAOTAO = {
	checkLogin : function(){
		var _token = $.cookie("TT_TOKEN");
		if(!_token){
			return ;
		}
		$.ajax({
			url : "http://sso.taotao.com/service/user/" + _token,
			dataType : "jsonp",
			type : "GET",
			success : function(_data){
				var html =_data.username+"，欢迎来到淘淘！<a href=\"http://www.taotao.com/user/logout.html\" class=\"link-logout\" onclick='logout()'>[退出]</a>";
				$("#loginbar").html(html);
			}
		});
	}
}

function logout(){
	var html = "您好！欢迎来到淘淘！ <a href=\"javascript:login()\">[登录]</a>&nbsp;<a href=\"javascript:regist()\">[免费注册]</a>";
	$("#loginbar").html(html);
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});