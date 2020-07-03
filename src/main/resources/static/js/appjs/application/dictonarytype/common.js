





function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
	    ignore : [],
		rules : {
		 name : {
				  required : true
			},
		},
		messages : {		
			name : {
				required : icon+"请输入：中文名称 "
			},

		}
	})
}