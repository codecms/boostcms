

(function ($) {
    $(document).ajaxStart(function () {

    });
    $(document).ajaxStop(function () {
    });
    //登录过期，shiro返回登录页面
    $.ajaxSetup({
        complete: function (xhr, status,dataType) {
            if('text/html;charset=UTF-8'==xhr.getResponseHeader('Content-Type')){
                top.location.href = '/login';
            }
        }
    });
})(jQuery);


function serializeNotNull(serStr){
    // return serStr.split("&").filter(function(str){return !str.endsWith("=")}).join("&");
    return serStr.split("&").filter(str => !str.endsWith("=")).join("&");
}


Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "H+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//$(function(){
//	var curTime = new Date().getTime();
//	var startDate = curTime - (7 * 3600 * 24 * 1000);
//	$("#startTime").val(new Date(startDate).Format("yyyy-MM-dd"));
//	$("#endTime").val(new Date().Format("yyyy-MM-dd"));
//	
//});




var notDataException = '<i onclick="failLoad()" id="xuanzhun" class=\' failLoad\' style=\'position: relative;right: 0.01%;color: #676a6c;cursor: pointer;font-size: 17px;\'><i class="fa fa-spinner" aria-hidden="true"></i></span>';
function failLoad() {
    layer.confirm('<h3>数据加载异常,请刷新一下页面!</h3><p style="font-size: 12px">(若刷新无效请立即联系管理员)</p>', {
        offset: 't',
        area: '300px;',
        btn: ['好的','稍后'] //按钮
    }, function() {
        window.location.reload()
        layer.closeAll();
    })
}
