function add() {
    var url = window.location.protocol + "//" + window.location.host;
    var loginUserName = $('input[name="loginUserName"]').val();
    var loginUserPassword = $('input[name="loginUserPassword"]').val();
    var userEmail = $('input[name="userEmail"]').val();
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
    if (!validUserEmail(userEmail)) {
        swal("请输入符合规范的邮箱地址！", {
            icon: "error",
        });
        return false;
    }
    var data = {
        "loginUserName": loginUserName,
        "loginUserPassword": loginUserPassword,
        "userEmail": userEmail
    };
    $.ajax({
        type: 'POST',
        url: '/admin/register',
        datatype: 'json',
        data: data,
        success: function (data) {
            var mes = data.message;
            mes = mes.replace(/^\s+|\s+$/g, "");
            if (mes === "exist") {
                swal("该用户已存在！", {
                    icon: "error",
                });
            } else if (mes === "failed") {
                swal("发生了我也不知道的错误", {
                    icon: "error",
                });
            } else if (mes === "success") {
                swal("添加成功，请在下个页面补全个人信息", {
                    icon: "success",
                }).then(function () {
                    window.location.replace(url + "/admin/profile");
                });
            } else {
                swal(mes, {
                    icon: "error",
                });
            }
        }
    });
}

var timeout = 100;
var tipMes = "";
var i = 0;
var mDiv;

function forgetPassword() {
    var loginUserName = $('input[name="loginUserName"]').val();
    var userEmail = $('input[name="userEmail"]').val();
    if (isNull(loginUserName) || loginUserName.trim().length < 1) {
        swal("请输入登陆名称！", {
            icon: "error",
        });
        return false;
    }
    if (!validUserEmail(userEmail)) {
        swal("请输入符合规范的邮箱地址！", {
            icon: "error",
        });
        return false;
    }
    var data = {
        "loginUserName": loginUserName,
        "userEmail": userEmail
    };
    $.ajax({
        type: 'POST',
        url: '/user/findUser',
        datatype: 'json',
        data: data,
        success: function (mes) {
            var mes1 = mes.message;
            if (mes1 === "empty") {
                swal("用户不存在", {
                    icon: "error",
                });
            } else if (mes1 === "Incorrect email") {
                swal("邮箱不一致！", {
                    icon: "error",
                });
            } else if (mes1 === "success") {
                swal("已证实你是我们的用户，点击ok发送邮件", {
                    icon: "success",
                }).then(function () {
                    $('#but').attr("disabled", true);
                    $('.swal-button.swal-button--confirm').attr("data-target", "#myModal");
                    $('.swal-button.swal-button--confirm').attr("data-toggle", "modal");
                    // 延时加载进度条
                    mDiv = window.setInterval(fn, timeout);
                    $.ajax({
                        type: 'POST',
                        url: '/user/forgetPassword',
                        datatype: 'json',
                        data: data,
                        success: function (data) {
                            var mes = data.message;
                            if (mes === "Mail sent successfully") {
                                i = 100;
                                mDiv = window.setInterval(fn, timeout);
                                clearInterval(mDiv);
                                tipMes = "邮件发送成功！邮件收取可能会有延迟，请耐心等待！";
                            } else {
                                swal(mes, {
                                    icon: "error",
                                });
                                i = 100;
                                $('.tip').css("color", "red");
                                tipMes = "邮件发送失败了，请稍后再试！";
                            }
                        }
                    });
                });
            }
        }
    });
}

function fn() {
    console.log("i---->" + i);
    var myDiv = document.getElementById("myDiv");
    myDiv.style.width = i + "%";
    myDiv.innerText = i + "%";
    if (i === 98) {
        clearInterval(mDiv);
        return false;
    }
    if (i === 100) {
        clearInterval(mDiv);
        $('.tip').html(tipMes);
        return false;
    }
    i++;
}

function closeModal() {
    var url = window.location.protocol + "//" + window.location.host;
    window.location.replace(url);
}