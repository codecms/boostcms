var prefix = "/system/role";
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : false, // 设置为true将禁止多选
						iconSize : 'outline',


						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						showColumns : true, // 是否显示内容下拉框（选择显示的列）
				formatNoMatches: function () {  //没有匹配的结果
					$('#exampleTable').bootstrapTable('removeAll');
					return '无符合条件的记录';
				},
				onLoadError: function (data) {//连接不上错误
					$('#exampleTable').bootstrapTable('removeAll');
					alert("数据异常，请重新登录");
					$('#exampleTable').bootstrapTable('destroy');
					return '请重新登录';
				},
				onLoadSuccess:function(data){//返回数据结构不对的，一般是后台出错了							
							
				},

						columns : [
								{ // 列配置项
									// 数据类型，详细参数配置参见文档http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/
									checkbox : true
								// 列表中显示复选框
								},
								{
									field : 'roleId', // 列字段名
									title : '序号' // 列标题
								},
								{
									field : 'roleName',
									title : '角色名'
								},
								{
									field : 'remark',
									title : '备注'
								},
								{
									field : '',
									title : '权限'
								},
								{
									title : '操作',
									field : 'roleId',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.roleId
												+ '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.roleId
												+ '\')"><i class="fa fa-remove"></i></a> ';
										return e + d;
									}
								} ]
					});
}

function reLoad() {
   	$('#exampleTable').bootstrapTable('refresh');
}

function add() {
	// iframe层
	layer.open({
		type : 2,
		title : '添加角色',
		maxmin : true,
		area: ['auto', '500px'],
		shadeClose : false, // 点击遮罩关闭层
		content : prefix + '/add' // iframe的url
	});
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code === 0) {
					layer.msg("删除成功");
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})

}
function edit(id) {
	layer.open({
		type : 2,
		title : '角色修改',
		maxmin : true,
		area: ['auto', '500px'],
		shadeClose : true, // 点击遮罩关闭层
		content : prefix + '/edit/' + id // iframe的url
	});
}
