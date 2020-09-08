function add(){
    var url = window.location.protocol+"//"+window.location.host;
    var loginUserName = $('input[name="loginUserName"]').val();
    var loginUserPassword = $('input[name="loginUserPassword"]').val();
    var key = $('input[name="key"]').val();

    if (isNull(loginUserName) || loginUserName.trim().length < 1) {
        swal("请输入登陆名称！", {
            icon: "error",
        });
        return false;
    }
    if (isNull(loginUserPassword) || loginUserPassword.trim().length < 1) {
        swal("请输入密码！", {
            icon: "error",
        });
        return false;
    }
    var data = {
        "loginUserName": loginUserName,
        "loginUserPassword": loginUserPassword
    };
    $.ajax({
        type: 'POST',
        url: '/admin/register',
        datatype: 'json',
        data: data,
        success: function (data){
            var mes = data.message;
            mes = mes.replace( /^\s+|\s+$/g, "" );
            if (mes === "exist"){
                swal("该用户已存在！", {
                    icon: "error",
                });
            }else if (mes === "failed"){
                swal("发生了我也不知道的错误", {
                    icon: "error",
                });
            }else if (mes === "success"){
                swal("添加成功，请在下个页面补全个人信息", {
                    icon: "success",
                }).then(function (){
                    window.location.replace(url+"/admin/profile");
                });
            }else {
                swal(mes, {
                    icon: "error",
                });
            }
        }
    });

}