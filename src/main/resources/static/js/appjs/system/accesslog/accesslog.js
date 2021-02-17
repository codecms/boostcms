
var prefix = "/system/accesslog"
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
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								sort: params.sort=='idext'?'id':params.sort,
								order: params.order,
								username:$('#username').val()==''?undefined:$('#username').val(),
								operation:$('#operation').val()==''?undefined:$('#operation').val(),
								time:$('#time').val()==''?undefined:$('#time').val(),
								method:$('#method').val()==''?undefined:$('#method').val(),
								params:$('#params').val()==''?undefined:$('#params').val(),
								ip:$('#ip').val()==''?undefined:$('#ip').val(),
								gmtCreate:$('#gmtCreate').val()==''?undefined:$('#gmtCreate').val(),
								url:$('#url').val()==''?undefined:$('#url').val(),
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
								    field : 'username', 
									title : '用户名',
									sortable: true,
									formatter : function(value, row, index) {
										return row.username;
										//paraMap_js[id];
									}
								},
								{
								    field : 'operation', 
									title : '用户操作',
									sortable: true,
									formatter : function(value, row, index) {
										return row.operation;
										//paraMap_js[id];
									}
								},
								{
								    field : 'ip', 
									title : 'IP地址',
									sortable: true,
									formatter : function(value, row, index) {
										return row.ip;
										//paraMap_js[id];
									}
								},
								{
								    field : 'gmt_create', 
									title : '访问时间',
									sortable: true,
									formatter : function(value, row, index) {
										return row.gmtCreate;
										//paraMap_js[id];
									}
								},
								{
								    field : 'url', 
									title : '',
									sortable: true,
									formatter : function(value, row, index) {
										return row.url;
										//paraMap_js[id];
									}
								}

								 ]
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




