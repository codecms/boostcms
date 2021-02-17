// 以下为官方示例
$().ready(function() {
	validateRule();
	statusList();
	// $("#signupForm").validate();
});

$.validator.setDefaults({
	submitHandler : function() {
		save2();
	}
});


function save2() {
	$("#roleIds").val(getCheckedRoles());
　　　　var option = {
      　　 url : '/system/user/update',
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

$("#imgs").on('change', function () {
	  if (typeof (FileReader) != "undefined") {
	    var image_holder = $("#image-holder");
	    image_holder.empty();
	    var reader = new FileReader();
	    reader.onload = function (e) {
	      $("<img />", {
	      "src": e.target.result,
	      "class": "thumb-image",
	      "width":"160px",
	      }).appendTo(image_holder);
	    }
	    image_holder.show();
	    reader.readAsDataURL($(this)[0].files[0]);
	    $("#orgpic").hide();
	  } else {
	    alert("This browser does not support FileReader.");
	  }
	});


function update() {
	$("#roleIds").val(getCheckedRoles());
	$.ajax({
		cache : true,
		type : "POST",
		url : "/system/user/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg(data.msg);
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.msg(data.msg);
			}

		}
	});

}
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
function setCheckedRoles() {
	var roleIds = $("#roleIds").val();
	alert(roleIds);
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
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			},
			username : {
				required : true,
				minlength : 2
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
				minlength : icon + "用户名必须两个字符以上"
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