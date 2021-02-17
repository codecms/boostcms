
var prefix = "/application/dictonarytype"
$(function() {
	load();

	
});





function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
					//	showRefresh : true,
					//	showToggle : true,
					//	showColumns : true,
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
						//search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
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
						onPageChange: function(number, size) {	

                             document.querySelector("#vieworg").scrollIntoView(true);
                        },
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								sort: params.sort=='idext'?'id':params.sort,
								order: params.order,
								id:$('#id').val()==''?undefined:$('#id').val(),
								name:$('#name').val()==''?undefined:$('#name').val(),
					           // name:$('#searchName').val(),
					           // username:$('#searchName').val()
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : [
								{
									checkbox : true
								},
								{
								    field : 'idext', 
									title : '',
									sortable: true,
									formatter : function(value, row, index) {
										return row.id;
										//paraMap_js[id];
									}
								},
								{
								    field : 'name', 
									title : '中文名称',
									sortable: true,
									formatter : function(value, row, index) {
										return row.name;
										//paraMap_js[id];
									}
								},
								{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.id
												+ '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.id
												+ '\')"><i class="fa fa-remove"></i></a> ';

										return e + d ;
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
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		area: ['auto', '500px'],
		offset: '30px',
		shadeClose : false, // 点击遮罩关闭层
		content : prefix + '/add' // iframe的url
	});
}
function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		area: ['auto', '500px'],
		offset: '30px',
		shadeClose : false, // 点击遮罩关闭层
		content : prefix + '/edit/' + id // iframe的url
	});
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					searching();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}


function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					searching();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}