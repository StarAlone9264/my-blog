$(function () {
    //修改个人信息
    $('#updateUserNameButton').click(function () {
        $("#updateUserNameButton").attr("disabled",true);
        var userName = $('#loginUserName').val();
        var nickName = $('#nickName').val();
        var userPhone = $('#userPhone').val();
        var userEmail = $('#userEmail').val();
        var userAddress = $('#userAddress').val();
        var profilePictureUrl = $('#profilePictureUrl').val();
        if (validUserNameForUpdate(userName, nickName,userPhone,userEmail,userAddress)) {
            //ajax提交数据
            var params = $("#userNameForm").serialize();
            $.ajax({
                type: "POST",
                url: "/admin/profile/modifyInformation",
                data: params,
                success: function (r) {
                    if (r == 'success') {
                        swal("保存成功", {
                            icon: "success",
                        }).then(function () {
                            window.location.href = '/admin/login';
                        });
                    } else {
                        swal("修改失败", {
                            icon: "error",
                        }).then(function () {
                            $("#updateUserNameButton").prop("disabled",false);
                        });
                    }
                }
            });
        } else {
            $("#updateUserNameButton").prop("disabled",false);
        }
    });

    // 图片上传
    new AjaxUpload('#profilePicture', {
        action: '/admin/upload/uploadAvatar',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r != null && r.resultCode == 200) {
                $('#profilePictureUrl').val(r.data);
                return false;
            } else {
                alert("error");
            }
        }
    });

    //修改密码
    $('#updatePasswordButton').click(function () {
        $("#updatePasswordButton").attr("disabled",true);
        var originalPassword = $('#originalPassword').val();
        var newPassword = $('#newPassword').val();
        if (validPasswordForUpdate(originalPassword, newPassword)) {
            var params = $("#userPasswordForm").serialize();
            $.ajax({
                type: "POST",
                url: "/admin/profile/changePassword",
                data: params,
                success: function (r) {
                    console.log(r);
                    if (r == 'success') {
                        alert('密码变更成功！');
                        window.location.href = '/admin/login';
                    } else {
                        alert(r);
                        $("#updatePasswordButton").attr("disabled",false);
                    }
                }
            });
        } else {
            $("#updatePasswordButton").attr("disabled",false);
        }
    });
})

/**
 * 名称验证
 */
function validUserNameForUpdate(userName, nickName,userPhone,userEmail,userAddress) {
    if (isNull(userName) || userName.trim().length < 1) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入登陆名称！");
        return false;
    }
    if (isNull(nickName) || nickName.trim().length < 1) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("昵称不能为空！");
        return false;
    }
    if (isNull(userPhone) || userPhone.trim().length < 1) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("联系电话不能为空！");
        return false;
    }
    if (isNull(userEmail) || userEmail.trim().length < 1) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("联系电话不能为空！");
        return false;
    }
    if (isNull(userAddress) || userAddress.trim().length < 1) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("联系地址不能为空！");
        return false;
    }
    if (!validUserName(userName)) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入符合规范的登录名！");
        return false;
    }
    if (!validCN_ENString2_18(nickName)) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入符合规范的昵称！");
        return false;
    }
    if (!validUserPhone(userPhone)) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入符合规范的联系电话！");
        return false;
    }
    if (!validUserEmail(userEmail)) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入符合规范的邮箱地址！");
        return false;
    }
    return true;
}

/**
 * 密码验证
 */
function validPasswordForUpdate(originalPassword, newPassword) {
    if (isNull(originalPassword) || originalPassword.trim().length < 1) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("请输入原密码！");
        return false;
    }
    if (isNull(newPassword) || newPassword.trim().length < 1) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("新密码不能为空！");
        return false;
    }
    if (!validPassword(newPassword)) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("请输入符合规范的密码！");
        return false;
    }
    return true;
}
