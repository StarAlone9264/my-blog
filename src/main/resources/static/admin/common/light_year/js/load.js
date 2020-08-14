$(function () {
    var randNum =  parseInt(Math.random() * 30 + 1);
    var location = window.location;
    var path = location.protocol+"//"+location.host+"/admin/dist/img/login_rand/"+randNum+".jpg";
    $(".lyear-login-left").css("background-image","url(" + path + ")");

    $.ajax({
        type: 'GET',
        url: 'https://v1.hitokoto.cn',
        dataType: 'jsonp',
        jsonp: 'callback',
        jsonpCallback: 'hitokoto',
        success (data) {
            const json = eval("(" + data + ")");
            $('.hitokoto').html(json.hitokoto);
            if (json.from_who==null){
                json.from_who = '';
            }
            $('.hitokoto-from').html('——'+json.from_who+'  '+json.from);
        },
        error (jqXHR, textStatus, errorThrown) {
            // 错误信息处理
            console.error(textStatus, errorThrown)
        }
    });

    $('#forget-password').click(function () {
        swal("密码你都忘，没有找回功能。", {
            icon: "error",
        });
        return false;
    })
});