var prefix = "/system/user"
$(function() {
	var deptId = '';
	getTreeData();
	load(deptId);
});

function load(deptId) {
	$('#exampleTable')
		.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : prefix + "/list", // 服务器数据的加载地址
				// showRefresh : true,
				// showToggle : true,
				// showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				// search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者
				formatLoadingMessage: function () {
					return "请稍等，正在加载中...";
				},
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
	                 if(data.hasOwnProperty("total") && data.hasOwnProperty("rows")){                              
                      }else{
                         $('#exampleTable').bootstrapTable('destroy');
                         return 'error';
                       }							
				},
				queryParams : function(params) {
					return {
						// 说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit : params.limit,
						offset : params.offset,
						sort: params.sort=='idext'?'id':params.sort,
						order: params.order,
						name:$('#searchName').val()==''?undefined:$('#searchName').val(),
						deptId : deptId
					};
				},

				columns : [
					{
						checkbox : true
					},
					{
						field : 'user_id', 
						sortable: true,
						title : '序号',
						formatter : function(value, row, index) {
							return row.userId;
							//paraMap_js[id];
						}
						
					},
					{
						field : 'name',
						sortable: true,
						title : '姓名'
					},
					{
						field : 'username',
						title : '用户名'
					},
					{
						field : 'deptId',
						title : '邮箱',
						formatter: function(value, row, index) {
							if(row.deptId==0){
								return '-';
							}else{
								return deptMap_js[row.deptId].name;
							}
						}
					},
					{
						field : 'email',
						title : '邮箱'
					},
					{
						field : 'img', 
						title : '图片' ,
						formatter: function(value, row, index) {

							if(null != row.img) {
								var e = '<img id="pictu"  height="40px" width="40px" style="border-radius:8px;" src="/system/user/getPicture?deviceId=' + row.userId + '&timesta='+Math.random()+'" ></img> ';
							} else {
								var e = "";
							}
							return e;
						}
					},
					{
						field : 'status',
						title : '状态',
						sortable: true,
						align : 'center',
						formatter : function(value, row, index) {
							if (row.status == '禁用') {
								return '<span class="label label-danger">禁用</span>';
							} else if (row.status == '正常') {
								return '<span class="label label-primary">正常</span>';
							}
						}
					},
					{
						title : '操作',
						field : 'id',
						align : 'center',
						formatter : function(value, row, index) {
							var e = '<a  class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
								+ row.userId
								+ '\')"><i class="fa fa-edit "></i></a> ';
							var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
								+ row.userId
								+ '\')"><i class="fa fa-remove"></i></a> ';
							var f = '<a class="btn btn-success btn-sm ' + s_resetPwd_h + '" href="#" title="重置密码"  mce_href="#" onclick="resetPwd(\''
								+ row.userId
								+ '\')"><i class="fa fa-key"></i></a> ';
							var m = '<a class="btn btn-success btn-sm ' + s_edit_pwd + '" href="#" title="修改密码"  mce_href="#" onclick="editPwd(\''
							+ row.userId
							+ '\')"><i class="fa fa-key"></i></a> ';
							if(loginname != 'admin' && loginname !=row.username ){
								m='';
								f='';
								if(logindept == row.deptId){
									e='';
									d='';
								}
							}
							return e + d + f+m;
						}
					} ]
			});
}

function searching(){
	if($('#exampleTable').bootstrapTable('getOptions').pageNumber != 1){
		$('#exampleTable').bootstrapTable('refreshOptions', {
			pageNumber: 1
		});
	}else{
		$('#exampleTable').bootstrapTable('refresh');
	}
//	reLoad();
}

function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

function add() {
	// iframe层
	layer.open({
		type : 2,
		title : '增加用户',
		maxmin : true,
		area: ['auto', '500px'],
		shadeClose : false, // 点击遮罩关闭层
		content : prefix + '/add'
	});
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : "/system/user/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
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
		title : '用户修改',
		maxmin : true,
		area: ['auto', '500px'],
		shadeClose : false,
		content : prefix + '/edit/' + id // iframe的url
	});
}

function editPwd(id) {
	layer.open({
		type : 2,
		title : '修改密码',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		content : prefix + '/editPwd/' + id // iframe的url
	});
}


function resetPwd(id) {
	layer.open({
		type : 2,
		title : '重置密码',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		content : prefix + '/resetPwd/' + id // iframe的url
	});
}

function getTreeData() {
	$.ajax({
		type : "GET",
		url : "/system/sysDept/tree",
		success : function(tree) {
			loadTree(tree);
		}
	});
}
function loadTree(tree) {
	$('#jstree').jstree({
		'core' : {
			'data' : tree
		},
		"plugins" : [ "search" ]
	});
	$('#jstree').jstree().open_all();
}
$('#jstree').on("changed.jstree", function(e, data) {
	if (data.selected == -1) {
		var opt = {
			query : {
				deptId : '',
			}
		}
		$('#exampleTable').bootstrapTable('refresh', opt);
	} else {
		var opt = {
			query : {
				deptId : data.selected[0],
			}
		}
		$('#exampleTable').bootstrapTable('refresh',opt);
	}

});