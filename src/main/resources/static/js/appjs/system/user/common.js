

function statusChange(){
	var a=$("#liststatus").val();
	$("#status").val(a);
}
function  statusList(){

	 $.ajax({
		cache : true,
		type : "get",
		url : "/system/user/UserStatusEnum",
		dataType : "json",
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			for(var i=0;i<data.length;i++){
				$("#liststatus").append("<option value='"+data[i].name+"'>"+data[i].value+"</option>");
			}
			
		}
	});
	
	var statusValue2 = $("#status").val();
	$("#liststatus").find("option[value='"+statusValue2+"']").attr("selected",true);
}




function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
	    ignore : [],
		rules : {
		},
		messages : {		

		}
	})
}