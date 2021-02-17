$().ready(function() {
	validateRule();
	statusList();
});

$.validator.setDefaults({
	submitHandler : function() {
		save2();
	}
});


$("#imgs").on('change', function () {
	  if (typeof (FileReader) != "undefined") {
	    var image_holder = $("#image-holder");
	    image_holder.empty();
	    var reader = new FileReader();
	    reader.onload = function (e) {
	      $("<img   />", {
	      "src": e.target.result,
	      "class": "thumb-image",
	      "height":"120px",
	      "width":"160px",
	      }).appendTo(image_holder);
	    }
	    image_holder.show();
	    reader.readAsDataURL($(this)[0].files[0]);
	  } else {
	    alert("This browser does not support FileReader.");
	  }
	});


function getCheckedRoles() {
	var adIds = "";
	$("input:checkbox[name=role]:checked").each(function(i) {
		if (0 == i) {
			adIds = $(this).val();
		} else {
			adIds += ("," + $(this).val());
		}
	});
	return adIds;
}
function save() {
	$("#roleIds").val(getCheckedRoles());
	$.ajax({
		cache : true,
		type : "POST",
		url : "/system/user/save",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}



function save2() {
	$("#roleIds").val(getCheckedRoles());
　　　　var option = {
      　　 url : '/system/user/save',
     　　  type : 'post',
      　　 dataType : 'json',
      　　 headers : {"ClientCallMode" : "ajax"}, //添加请求头部
     　　  success : function(data) {
        　if (data.code == 0) {
			parent.layer.msg("操作成功");
			parent.reLoad();
			var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
			parent.layer.close(index);

		} else {
			parent.layer.alert(data.msg)
		}
       },
       error: function(data) {
           alert("企业用户升级失败,请联系管理员！");
       }
    };
   $("#signupForm").ajaxSubmit(option);
   return false; //最好返回false，因为如果按钮类型是submit,则表单自己又会提交一次;返回false阻止表单再次提交
}

function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			},
			username : {
				required : true,
				minlength : 2,
				remote : {
					url : "/system/user/exit", // 后台处理程序
					type : "post", // 数据发送方式
					dataType : "json", // 接受数据格式
					data : { // 要传递的数据
						username : function() {
							return $("#username").val();
						}
					}
				}
			},
			password : {
				required : true,
				minlength : 6
			},
			confirm_password : {
				required : true,
				minlength : 6,
				equalTo : "#password"
			},
			email : {
				required : true,
				email : true
			},
			topic : {
				required : "#newsletter:checked",
				minlength : 2
			},
			deptName:"required",
			deptId:"required",
			status:"required",
			role:"required",
			agree : "required"
		},
		messages : {

			name : {
				required : icon + "请输入姓名"
			},
			username : {
				required : icon + "请输入您的用户名",
				minlength : icon + "用户名必须两个字符以上",
				remote : icon + "用户名已经存在"
			},
			password : {
				required : icon + "请输入您的密码",
				minlength : icon + "密码必须6个字符以上"
			},
			confirm_password : {
				required : icon + "请再次输入密码",
				minlength : icon + "密码必须6个字符以上",
				equalTo : icon + "两次输入的密码不一致"
			},
			deptName:icon + "请选部门",
			deptId: icon + "请选部门",
			status: icon +"选状态",
			role: icon +"请选角色",
			email : icon + "请输入您的E-mail",
		}
	})
}

var openDept = function(){
	layer.open({
		type:2,
		title:"选择部门",
		area : [ '300px', '450px' ],
		content:"/system/sysDept/treeView"
	})
}
function loadDept( deptId,deptName){
	$("#deptId").val(deptId);
	$("#deptName").val(deptName);
}