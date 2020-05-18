// 用common.js必须加上Feng.addCtx("${ctxPath}");
nstr_module.info = function (info) {
    top.layer.msg(info, {icon: 6});
};
nstr_module.success = function (info) {
    top.layer.msg(info, {icon: 1});
};
nstr_module.error = function (info) {
    top.layer.msg(info, {icon: 2});
};
nstr_module.confirm = function (tip, ensure) {
    top.layer.confirm(tip, {
        skin: 'layui-layer-admin'
    }, function () {
        ensure();
    });
};
nstr_module.currentDate = function () {
    // 获取当前日期
    var date = new Date();

    // 获取当前月份
    var nowMonth = date.getMonth() + 1;

    // 获取当前是几号
    var strDate = date.getDate();

    // 添加分隔符“-”
    var seperator = "-";

    // 对月份进行处理，1-9月在前面添加一个“0”
    if (nowMonth >= 1 && nowMonth <= 9) {
        nowMonth = "0" + nowMonth;
    }

    // 对月份进行处理，1-9号在前面添加一个“0”
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }

    // 最后拼接字符串，得到一个格式为(yyyy-MM-dd)的日期
    return date.getFullYear() + seperator + nowMonth + seperator + strDate;
};
nstr_module.getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    } else {
        return null;
    }
};
nstr_module.infoDetail = function (title, info) {
    var display = "";
    if (typeof info === "string") {
        display = info;
    } else {
        if (info instanceof Array) {
            for (var x in info) {
                display = display + info[x] + "<br/>";
            }
        } else {
            display = info;
        }
    }
    top.layer.open({
        title: title,
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['950px', '600px'], //宽高
        content: '<div style="padding: 20px;">' + display + '</div>'
    });
};
nstr_module.zTreeCheckedNodes = function (zTreeId) {
    var zTree = $.fn.zTree.getZTreeObj(zTreeId);
    var nodes = zTree.getCheckedNodes();
    var ids = "";
    for (var i = 0, l = nodes.length; i < l; i++) {
        ids += "," + nodes[i].id;
    }
    return ids.substring(1);
};
nstr_module.closeAllLoading = function () {
    layer.closeAll('loading');
};
nstr_module.layerOpen = function (obj) {
    layer.open({
        type: obj.type || 2,
        title: obj.title,
        scrollbar: obj.scrollbar || false,
        closeBtn: obj.closeBtn, //关闭按钮是否显示
        shade: obj.shade || 0.3,
        shadeClose: obj.shadeClose || true,
        skin: obj.skin || 'layui-layer-demo', //加上边框,
        area: obj.area || ['600px', '400px'],
        content: obj.content,
        btn: obj.btn,
        success: function (layero, index) {
            if (obj.successCB) {
                obj.successCB();
            }
            currentIndex = index;
        },
        yes: function (index, layero) {
            if (obj.btn1Callback) {
                obj.btn1Callback()
            }
//按钮【按钮一】的回调
        },
        btn2: function (index, layero) {
            if (obj.btn2Callback) {
                obj.btn2Callback()
            }
//按钮【按钮二】的回调
//return false 开启该代码可禁止点击该按钮关闭
        }
    })
};
nstr_module.reload = function () {
    window.location.reload();
};
nstr_module.layerConfirm = function (obj) {
    layer.confirm(obj.title, {
        icon: obj.icon || 5,
        btn: [obj.leftBtn, obj.rightBtn] //按钮
    }, function () {
        // layer.msg('的确很重要', {icon: 1});
        if (obj.leftCallback) {
            obj.leftCallback();
        }
    }, function () {
        if (obj.rightCallback) {
            obj.rightCallback();
        }
    });
};
nstr_module.layerAlert = function (str, icon) {
    layer.alert(str, {icon: icon});
};
nstr_module.returnBack = function () {
    $('#LAY_app').find('.layui-this').find('.layui-tab-close').trigger('click');
};
nstr_module.closeLayer = function () {
    layer.close(layer.index);
};
nstr_module.giveValue = function giveValue(name, iframeId, val) {
    $('.layui-layout-body').find('iframe[lay-id="' + iframeId + '"]').contents().find('input[name="' + name + '"]').val(val);
    $('.layui-layout-body').find('iframe[lay-id="' + iframeId + '"]').contents().find('input[name="' + name + '"]').attr("title", val);
}
nstr_module.initAdmins = function initAdmins(class_, iframeId, val) {//只适用于一个地方， 课题组管理-》选择管理员或者成员
    if (class_ === '.admin-container') {
        $('.layui-layout-body').find('iframe[lay-id="' + iframeId + '"]').contents().find('.admin-container').html(val);
    } else if (class_ === '.member-container') {
        $('.layui-layout-body').find('iframe[lay-id="' + iframeId + '"]').contents().find('.member-container').html(val);
    }
}
//获取路径
var pathName = window.document.location.pathname;
//截取，得到项目名称
var rootPath = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
// 以下代码是配置layui扩展模块的目录，每个页面都需要引入
layui.config({
    base: nstr_module.ctxPath + rootPath + '/assets/common/module/'
}).use(['admin'], function () {
    var $ = layui.$;
    var admin = layui.admin;

    // 单标签模式需要根据子页面的地址联动侧边栏的选中，用于适配浏览器前进后退按钮
    if (window != top && top.layui && top.layui.index && !top.layui.index.pageTabs) {
        top.layui.admin.activeNav(location.href.substring(nstr_module.ctxPath.length));
    }

    // 移除loading动画
    setTimeout(function () {
        admin.removeLoading();
    }, window == top ? 300 : 150);

    //注册session超时的操作
    $.ajaxSetup({
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        complete: function (XMLHttpRequest, textStatus) {

            //通过XMLHttpRequest取得响应头，sessionstatus，
            var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
            if (sessionstatus === "timeout") {

                //如果超时就处理 ，指定要跳转的页面
                window.location = nstr_module.ctxPath + "/global/sessionError";
            }
        }
    });

});

//iframe子页面之间相互跳转
function iframeJump(url) {
    _url = "a[lay-href='" + url + "']";
    parent.$(_url).click();
}