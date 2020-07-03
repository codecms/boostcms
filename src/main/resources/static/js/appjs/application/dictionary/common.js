

function typeChange(){
	var a=$("#listtype").val();
	$("#type").val(a);
}
function  typeList(){

	 $.ajax({
		cache : true,
		type : "get",
		url : "/application/dictionary/DictionaryTypeEnum",
		dataType : "json",
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			for(var i=0;i<data.length;i++){
				$("#listtype").append("<option value='"+data[i].name+"'>"+data[i].value+"</option>");
			}
			
		}
	});
	
	var statusValue2 = $("#type").val();
	$("#listtype").find("option[value='"+statusValue2+"']").attr("selected",true);
}




function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
	    ignore : [],
		rules : {
		 name : {
				  required : true
			},
		 type : {
				  required : true
			},
		},
		messages : {		
			name : {
				required : icon+"请输入：中文名 "
			},
			type : {
				required : icon+"请输入：字典值类型 "
			},

		}
	})
}