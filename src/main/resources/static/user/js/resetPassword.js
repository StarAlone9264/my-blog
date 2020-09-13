$(function () {
    var url = window.location.protocol+"//"+window.location.host;
    // 为提交按钮添加点击事件
    $('#submit-button').click(function () {
        var verifyId = $('input[name="verifyId"]').val();
        var userId = $('input[name="userId"]').val();
        var password = $('input[name="password"]').val();
        var secondPassword = $('input[name="second-confirmation"]').val();
        if (isNull(verifyId) || verifyId.trim().length < 1) {
            swal("出错了！回邮件重新进入试试", {
                icon: "error",
            });
            return false;
        }
        if (isNull(userId) || userId.trim().length < 1) {
            swal("出错了！回邮件重新进入", {
                icon: "error",
            });
            return false;
        }
        if (isNull(password) || password.trim().length < 1) {
            swal("请输入密码！", {
                icon: "error",
            });
            return false;
        }
        if (isNull(secondPassword) || secondPassword.trim().length < 1) {
            swal("请输入密码！", {
                icon: "error",
            });
            return false;
        }
        if (!validPassword(password)) {
            swal("请输入符合规范的密码！", {
                icon: "error",
            });
            return false;
        }
        if (password !== secondPassword) {
            swal("两次密码输入不一致！", {
                icon: "error",
            });
            return false;
        }
        var data = {
            "verifyId": verifyId,
            "userId": userId,
            "password": password
        };
        $.ajax({
            type: 'POST',
            url: '/user/resetPassword',
            datatype: 'json',
            data: data,
            success: function (data){
                var mes = data.message;
                if (mes === "success"){
                    swal("密码修改成功！", {
                        icon: "success",
                    }).then(function (){
                        window.location.replace(url+"/admin/login");
                    });
                }else if (mes === "verifyId is null!"){
                    swal("出错了！回邮件重新进入试试", {
                        icon: "error",
                    });
                }else if (mes === "userId is null!"){
                    swal("出错了！回邮件重新进入试试", {
                        icon: "error",
                    });
                }else if (mes === "Password cannot be empty!"){
                    swal("出错了！回邮件重新进入试试", {
                        icon: "error",
                    });
                }else {
                    swal(mes, {
                        icon: "error",
                    });
                }
            }
        });
    });
});