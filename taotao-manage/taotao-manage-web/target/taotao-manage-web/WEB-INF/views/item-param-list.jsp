<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemParamList" title="规格参数列表"
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/rest/item/param/list',method:'get',pageSize:10,toolbar:itemParamListToolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">ID</th>
        	<th data-options="field:'itemCatId',width:80">商品类目ID</th>
        	<th data-options="field:'itemCatName',width:100">商品类目</th>
            <th data-options="field:'paramData',width:300,formatter:formatItemParamData">规格(只显示分组名称)</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="编辑规格参数" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/item-param-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>

	function formatItemParamData(value , index){
		var json = JSON.parse(value);
		var array = [];
		$.each(json,function(i,e){
			array.push(e.group);
		});
		return array.join(",");
	}

    function getSelectionsIds(){
    	var itemList = $("#itemParamList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var itemParamListToolbar = [{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
        	TAOTAO.createWindow({
        		url : "/rest/page/item-param-add",
        	});
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
            var ids = getSelectionsIds();
            if(ids.length == 0){
                $.messager.alert('提示','必须选择一条规格参数才能编辑!');
                return ;
            }
            if(ids.indexOf(',') > 0){
                $.messager.alert('提示','只能选择一条规格参数!');
                return ;
            }

            $("#itemEditWindow").window({
                onLoad :function(){
                    //回显数据
                    var data = $("#itemList").datagrid("getSelections")[0];
                    data.priceView = TAOTAO.formatPrice(data.price);
                    $("#itemeEditForm").form("load",data);

                    //加载商品规格
                    //提交到后台的RESTful
                    $.ajax({
                        type: "GET",
                        url: "/rest/item/param/item/" + data.id,
                        statusCode : {
                            200 : function(_data){
                                $("#itemeEditForm .params").show();
                                $("#itemeEditForm [name=itemParams]").val(_data.paramData);
                                $("#itemeEditForm [name=itemParamId]").val(_data.id);

                                //回显商品规格
                                var paramData = JSON.parse(_data.paramData);

                                var html = "<ul>";
                                for(var i in paramData){
                                    var pd = paramData[i];
                                    html+="<li><table>";
                                    html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";

                                    for(var j in pd.params){
                                        var ps = pd.params[j];
                                        html+="<tr><td class=\"param\"><span>"+ps.k+"</span>: </td><td><input autocomplete=\"off\" type=\"text\" value='"+ps.v+"'/></td></tr>";
                                    }

                                    html+="</li></table>";
                                }
                                html+= "</ul>";
                                $("#itemeEditForm .params td").eq(1).html(html);

                            },
                            404 : function(){

                            },
                            500 : function(){
                                alert("error");
                            }
                        }
                    });

                    TAOTAO.init({
                        "pics" : data.image,
                        "cid" : data.cid,
                        fun:function(node){
                            TAOTAO.changeItemParam(node, "itemeEditForm");
                        }
                    });
                }
            }).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品规格!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的商品规格吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/param/delete",params, function(data){
            			if(data.status == 200){
            				$.messager.alert('提示','删除商品规格成功!',undefined,function(){
            					$("#itemParamList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>