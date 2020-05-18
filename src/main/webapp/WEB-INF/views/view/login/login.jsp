<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="description" content="在线服务平台">
    <link href="../../assets/images/favicon.ico" rel="icon"/>
    <title>科研设施与仪器开放共享在线服务平台license加密系统|用户登录</title>
    <script src="${rootPath }/static/js/ext/common/util.js"></script>
    <link rel="stylesheet" href="${rootPath }/static/css/ext/bootstrap/bootstrap.min.css"/>
    <link rel="stylesheet" href="${rootPath }/static/css/ext/swiper/css/swiper.min.css"/>
    <link rel="stylesheet" href="${rootPath }/static/css/reset.css"/>
    <link rel="stylesheet" href="${rootPath }/static/css/common.css"/>
    <link rel="stylesheet" href="${rootPath }/static/css/register.css">
    <style type="text/css">
        span#info {
            position: relative;
            left: 251px;
            top: -27px;
            color: red;
        }

        #login-wrap {
            position: relative;
            width: 100%;
            height: 100%;
            background-image: url(${rootPath }/static/img/login-bg.jpg);
            background-size: 100%;
        }


    </style>
</head>
<body id="login-wrap">
<div class="main-container main">
    <div class="title-box">
        <h3 class="title" style="font-size: 40px;margin-top: 50px">license加密系统</h3>
    </div>
    <div class="form-box">
        <form class="form-horizontal login-form" id="loginForm" onsubmit="return checkForm();"
              action="${rootPath }/login" method="post">
            <%--表单攻击--%>
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <div class="form-group">
                <label for="loginName" class="control-label pull-left">登录名：</label>
                <div class="input-panel pull-left" style="height: 32px;">
                    <input type="text" name="loginName" class="form-control" id="loginName" maxlength="255"
                           placeholder="请输入您的登录名" value="${nickname }" autocomplete="off">

                </div>
            </div>
            <div class="form-group">
                <label class="control-label pull-left">密码：</label>
                <div class="input-panel pull-left" style="height: 32px;">
                    <input type="password" name="pwd" class="form-control" id="userPwd"
                           placeholder="请输入您的密码" autocomplete="off" maxlength="10">
                </div>
                <%-- <span hidden="hidden" id="showCode">${codeError }</span> --%>
                <span hidden="hidden" id="randFiledCount">${randFiledCount }</span>
            </div>
            <%-- <c:if test="${sessionScope.randFiledCount >= codeError}"> --%>
            <div class="form-group">
                <label for="code" class="control-label pull-left">验证码：</label>
                <div class="input-panel pull-left" style="width: 400px;height: 32px;">
                    <div class="row">
                        <div class="col-md-4" style="paddingt-left:0!important;">
                            <input type="text" class="form-control1 form-control" id="code" name="code"
                                   placeholder="验证码" style="width: 110px;" maxlength="4">
                        </div>
                        <div class="col-md-4" style="padding-left: 0;margin-left: -5px;">
								<span class="code" id="basic-addon-code">
									<a href="" onclick="return refreshImg('randImg')">
										<img id="randImg" src="${rootPath }/base/randImg" alt="点击更换"
                                             title="点击更换" width="110" height="32" class="m">
									</a>
								</span>
                        </div>
                    </div>
                </div>
            </div>
            <%-- </c:if> --%>
            <span class="error_info"
                  style="margin-left: 71px;padding-left: 0; font-family: MicrosoftYaHei;color: red;font-size: 14px">${info }</span>
            <div class="form-group">
                <button type="submit" class="btn btn-default submit-btn login-submit">提交</button>
                <div class="other-login-box clearfix">
                    <!--  <a class="other-login" href="">使用大型科学仪器国家网络管理平台账号登录</a> -->
                    <a href="${rootPath }user/forgetPass.ins" class="forget-password pull-left" id="forget-password">忘记密码？</a>
                    <span id="login-span">（本系统仅供内部人员使用）</span>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
<script src="${rootPath }/static/js/ext/jquery.js" type="text/javascript"></script>
<script src="${rootPath }/static/js/ext/jquery.form.js" type="text/javascript"></script>
<script src="${rootPath }/static/js/ext/bootstrap/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript">
    function checkForm() {
        var loginName = $.trim($("#loginName").val());
        var userPwd = $.trim($("#userPwd").val());
        var code = $.trim($("#code").val());
        /* var showCode = $("#showCode").html(); */
        var randFiledCount = $("#randFiledCount").html();
        if (loginName.length === 0) {
            layer.msg('登录名不能为空', {icon: 5});
            return false;
        }
        if (userPwd.length === 0) {
            layer.msg('密码不能为空', {icon: 5});
            return false;
        }
        /* if(showCode<=randFiledCount){ */
        if (code.length === 0) {
            layer.msg('验证码不能为空', {icon: 5});
            return false;
        }
        /* } */
        return true;
    }

    $("#loginName").blur(function () {
        var loginName = $.trim($("#loginName").val());
        if (loginName.length === 0) {
            layer.msg('登录名不能为空', {icon: 5});
        }
    });
    $("#userPwd").blur(function () {
        var userPwd = $.trim($("#userPwd").val());
        if (userPwd.length === 0) {
            layer.msg('密码不能为空', {icon: 5});
        }
    });
    $("#code").blur(function () {
        var code = $.trim($("#code").val());
        if (code.length === 0) {
            layer.msg('验证码不能为空', {icon: 5});
        }
    });
    $('.form-control').focus(function () {
        $(this).next().text('');
    });

</script>
<script src="${rootPath }/static/js/ext/layer/layer/layer.js" type="text/javascript"></script>
</html>