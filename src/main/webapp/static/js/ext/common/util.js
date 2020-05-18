/**
 * 定义一个全局的rootPath参数,存储的是项目名
 * 和${rootPath}jsp中的效果一样
 */
//获取路径
//var pathName=window.document.location.pathname;
//截取，得到项目名称
//var rootPath=pathName.substring(0,pathName.substr(1).indexOf('/')+1)+'/';
//var curWwwPath = window.document.location.href;
// 获取网络协议
var protocol = window.location.protocol;
// 获取主机名+端口号
var domainPort = window.location.host;
// 获取发布项目的名称
// 获取路径
var url = window.location.pathname;
var webApp = url.split('/')[1];
// http://127.0.0.1/demo
var urlPrefix = protocol + "//" + domainPort + "/";


/**
 * 刷新验证码
 * @returns {Boolean}
 */
function refreshImg(imgId) {
    /* 取到验证码对应的img标签
        为验证码的img标签中的src属性赋值
    */
    //var num = Math.floor(Math.random()*10+1);
    $("#" + imgId).attr("src", urlPrefix + "base/randImg?now=" + new Date().getTime());
    return false;
}