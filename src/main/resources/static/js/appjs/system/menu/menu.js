var prefix = "/system/menu"
$(document).ready(function () {
    load();
});
var load = function () {
    $('#exampleTable')
        .bootstrapTreeTable(
            {
                id: 'menuId',
                code: 'menuId',
                parentCode: 'parentId',
                type: "GET", // 请求数据的ajax类型
                url: prefix + '/list', // 请求数据的ajax的url
                ajaxParams: {sort:'order_num'}, // 请求数据的ajax的data属性
                expandColumn: '1',// 在哪一列上面显示展开按钮
                striped: true, // 是否各行渐变色
                bordered: true, // 是否显示边框
                expandAll: false, // 是否全部展开
                // toolbar : '#exampleToolbar',
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
                columns: [
                    {
                        title: '编号',
                        field: 'menuId',
                        visible: false,
                        align: 'center',
                        valign: 'center',
                    },
                    {
                        title: '名称',
                        valign: 'center',
                        field: 'name',
  
                    },

                    {
                        title: '图标',
                        field: 'icon',
                        align: 'center',
                        valign: 'center',
                        formatter: function (item, index) {
                            return item.icon == null ? ''
                                : '<i class="' + item.icon
                                + ' fa-lg"></i>';
                        }
                    },
                    {
                        title: '类型',
                        field: 'type',
                        align: 'center',
                        valign: 'center',
                        formatter: function (item, index) {
                            if (item.type === 0) {
                                return '<span class="label label-primary">目录</span>';
                            }
                            if (item.type === 1) {
                                return '<span class="label label-success">菜单</span>';
                            }
                            if (item.type === 2) {
                                return '<span class="label label-warning">按钮</span>';
                            }
                        }
                    },
                    {
                        title: '地址',
                        valign: 'center',
                        field: 'url'
                    },
                    {
                        title: '权限标识',
                        valign: 'center',
                        field: 'perms'
                    },
                    {
                        title: '操作',
                        field: 'id',
                        align: 'center',
                        valign: 'center',
                        formatter: function (item, index) {
                            var e = '<a class="btn btn-primary btn-sm '
                                + s_edit_h
                                + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                                + item.menuId
                                + '\')"><i class="fa fa-edit"></i></a> ';
                            var p = '<a class="btn btn-primary btn-sm '
                                + s_add_h
                                + '" href="#" mce_href="#" title="添加下级" onclick="add(\''
                                + item.menuId
                                + '\')"><i class="fa fa-plus"></i></a> ';
                            var d = '<a class="btn btn-warning btn-sm '
                                + s_remove_h
                                + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                                + item.menuId
                                + '\')"><i class="fa fa-remove"></i></a> ';
                            return e + d + p;
                        }
                    }]
            });
}

function reLoad() {
    load();
}

function add(pId) {
    layer.open({
        type: 2,
        title: '增加菜单',
        maxmin: true,
        area: ['auto', '500px'],
        shadeClose: false, // 点击遮罩关闭层
        content: prefix + '/add/' + pId // iframe的url
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (data) {
                if (data.code == 0) {
                    layer.msg("删除成功");
                    reLoad();
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    })
}

function edit(id) {
    layer.open({
        type: 2,
        title: '菜单修改',
        maxmin: true,
        area: ['auto', '500px'],
        shadeClose: false, // 点击遮罩关闭层
        content: prefix + '/edit/' + id // iframe的url
    });
}

