var location_map = (window.location.toString()).split('/');
//var path = location_map[0]+'//'+location_map[2]+'/'+location_map[3];
var path = location_map[0] + '//' + location_map[2];

/**
 * 通过浏览器IP解析地址
 */
function getLocalCity() {
    /*$("input[name=localCity]").attr("value","北京市");
    $("input[name=localCityForShow]").attr("value","北京市");*/
    /* var cityName = '北京市';
     $("input[name=localCity]").attr("value",cityName);*/

    //alert($('#localCity').val());
    function myFun(result) {
        var cityName = result.name;
        // $("input[name=localCity]").attr("value",cityName); //FZQ修改，cityForSearch和cityForShow应该分开
        $("input[name=localCityForShow]").attr("value", cityName);
    }

    var myCity = new BMap.LocalCity();
    myCity.get(myFun);
}


/**
 * 上一页搜索
 * 先获取当前页码，由于是上一页，因此当前页码需-1
 * 填充id为seniorSearchForm的表单，并传到后台
 * 将返回的值传回jsp
 */
function preyPage() {
    var currentPage = $("#currentPage").val(); // 用户输入的页码
    var currentPageInt = parseInt(currentPage);
    var totalPageNum = parseInt($("#lastPageNum").val());
    if (currentPageInt <= 1) {
        alert("已是第一页");
    } else {
        currentPage = currentPageInt - 1;
        $("#currentPage").attr("value", currentPage); // seniorSearchForm表单中要传递的页码参数
        $.ajax({
            type: "get",
            url: path + "/search/tp.do",
            data: $("#filterSearchForm").serialize(),
            dataType: "json",
            success: function (data) {
                $("#instruResult").html(getSearchResultStr(data));
                $("#currentPage").attr("value", currentPage);
                $("#currentPage1").html(currentPage);
                showPageNumber(currentPage, totalPageNum);
                $("#target").val(currentPage);
            },
            error: function () {
                window.location.href = path + "/base/toIndex.do";
            }
        });
    }
}

/**
 * 下一页搜索
 * 具体流程说明参见 @see preyPage()
 */
function nextPage() {
    var currentPage = $("#currentPage").val(); // 用户输入的页码
    var currentPageInt = parseInt(currentPage);

    var totalPageNum = parseInt($("#lastPageNum").val());

    if (currentPageInt >= totalPageNum) {
        alert("已是最后一页");
    } else {
        currentPage = currentPageInt + 1;
        $("#currentPage").attr("value", currentPage); // seniorSearchForm表单中要传递的页码参数
        $.ajax({
            type: "get",
            url: path + "/search/tp.do",
            data: $("#filterSearchForm").serialize(),
            dataType: "json",
            success: function (data) {
                $("#instruResult").html(getSearchResultStr(data));
                $("#currentPage").attr("value", currentPage);
                $("#currentPage1").html(currentPage);
                showPageNumber(currentPage, totalPageNum);
                $("#target").val(currentPage);
            },
            error: function () {
                window.location.href = path + "/base/toIndex.do";
            }
        });
    }
}

/**
 * 当前页搜索
 * 首先判断输入的页码是否在合理范围内；
 * 剩余流程参加上一页搜索 @see preyPage()
 */
function currentPage() {
    var strs = "stes";
    var currentPage = $("#target").val(); // 用户输入的页码
    var numberCheck = /^[0-9]*$/;
    if (currentPage == "") {
        alert("请输入页码");
    } else if (!currentPage.match(numberCheck)) {
        alert("无效页码");
    } else {
        var currentPageInt = parseInt(currentPage);

        var totalPageNum = parseInt($("#lastPageNum").val());

        if (currentPageInt < 1 || currentPageInt > totalPageNum) {
            alert("您选择的页码已超出范围");
        } else {
            $("#currentPage").attr("value", currentPage); // seniorSearchForm表单中要传递的页码参数
            $.ajax({
                type: "get",
                url: path + "/search/searType.ins",
                data: {strs: strs},
                dataType: "json",
                success: function (data) {
                    $("#instruResult").html(getSearchResultStr(data));
                    $("#currentPage1").html(currentPage);
                    showPageNumber(currentPage, totalPageNum);
                },
                error: function () {
                    window.location.href = path + "/base/toIndex.do";
                }
            });
        }
    }
}

/**
 * 选页
 * 和跳页(当前页搜索)其实是差不多的
 */
function selectPage(page) {
    $("#target").val(page);
    currentPage();
}

/**
 * 首页或者尾页搜索
 * @param pageNum 首页或者尾页的页码
 */
/*function firstOrLastPage(pageNum) {

	if (pageNum != 1) {
		pageNum = $("#lastPageNum").val();
	}
	$("#currentPage").attr("value",pageNum); // seniorSearchForm表单中要传递的页码参数
	$.ajax({
		type : "get",
		url : path+"/search/tp.do",
		data : $("#filterSearchForm").serialize(),
		dataType : "json",
		success: function(data){
			$("#instruResult").html(getSearchResultStr(data));
			$("#current").attr("value",pageNum); // 页面中显示的当前页码
		},
		error: function(){
			window.location.href=path+"/base/toIndex.do";
		}
	});
}*/


/**
 * 城市切换
 * @param city 切换的城市名称，本地城市或者全国，其中全国用*表示
 */

/*function exchangeCity(city) {

	$("#localCity").attr("value", city);
    $("#province").attr("value", "*");
	$.ajax({
		type : "get",
		url : path+"/search/fs.do",
		data : $("#filterSearchForm").serialize(),
		dataType : "json",
		success: function(data){
            if (data[1].baseInstruList != null && data[1].baseInstruList != "") {
                $("#instruResult").html(getSearchResultStr(data[1].baseInstruList));
                $("#instruResult").show();
                $("#error").hide();
            } else {
                $("#instruResult").hide();
                $("#error").show();
            }
			/!**
			 * 给页面参数赋值
			 *!/
			if (city=="*") {
				$("#localCityForShow").html("全国");
			} else {
				$("#localCityForShow").html(city);
			}
            $("#facetSearchResult").html(getFacetResultStr(data[0]));
            $("#totalPageNum").html(data[1].page.totalPageNum + "页"); // 页面中显示的当前页码
            $("#lastPageNum").attr("value", data[1].page.totalPageNum);
            $("#current").attr("value", 1);
			$("#resultNumForShow").html(data[1].page.totalNum);
		},
		error: function(){
			window.location.href=path+"/base/toIndex.do";
		}
	});
}*/

/**
 * 所在地区过滤
 */
function filterConditionByRegion(province) {
    // $("#localCity").attr("value", "*");
    $("#localCity").val("*");
    var localOnly = $('.sort .only');
    if ($("#province").val() == province) { //取消当前所在地区的选中状态
        $("#province").val("*");
        $(".sift li[class = 'myline_province']").hide(); // 去掉所在地区条件
        $(".sift li[class = 'myline_localCity']").hide(); // 去掉所在城市条件
        //localOnly.val('仅显示本地仪器与设施');//仅显示本地仪器按钮变化
        //localOnly.css('width','136px');
        if ($('.only').hasClass('big_facility')) {
            localOnly.val("仅显示本地设施");
            localOnly.css('width', '100px');
        }
        if ($('.only').hasClass('big_instru')) {
            localOnly.val("仅显示本地仪器");
            localOnly.css('width', '100px');
        }
        if ($('.only').hasClass('all_instru')) {
            localOnly.val("仅显示本地仪器与设施");
            localOnly.css('width', '136px');
        }
        localOnly.removeClass('only_checked');
    } else {
        $(".sift li[class = 'myline_province']").show(); // 在global.js有重复
        $(".sift li[class = 'myline_province'] i").html(province);
        $("#province").val(province);
        localOnly.val('显示所有地区');//仅显示本地仪器按钮变化
        localOnly.addClass('only_checked');
    }
    filterSearch();
}

/**
 * 搜索结果过滤
 */
function filterCondition(paramID, paramValue) {
    switch (paramID) {
        case "所属大类" :
            paramID = "instruType";
            /*if ($("#"+paramID).val() == paramValue) {
                $("#"+paramID).val("*");
            } else {
                $("#"+paramID).val(paramValue);
            }*/
            $("#" + paramID).val(paramValue);
            if (paramValue == "重大科研基础设施") {
                $('.sift ul .myline_instruMainType').hide();
                /*$('.sort .only').val('仅显示本地设施');*/
            } else if ($("#instruMainType").val() != "*") {
                $('.sift ul .myline_instruMainType').show();
            }/*else if(paramValue == "大型科研仪器"){
				$('.sort .only').val('仅显示本地仪器');
			}*/
            // $(".sift li").hide(); // 去掉筛选条件
            // $("#instruMainType").val("*");
            /*$("#subject").val("*");
            $("#madeIn").val("*");
            $("#province").val("*");
            $("#institution").val("*");
            $("#type").val("*");*/
            //$("#filterSearchForm").submit();
            filterSearch();
            return;
        case "产地国别" :
            paramID = "madeIn";
            break;
        case "所属分类" :
            paramID = "instruMainType";
            break;
        case "学科领域" :
            paramID = "subject";
            break;
        case "仪器类别" :
            paramID = "type";
            break;
        case "设施类别" :
            paramID = "type";
            break;
        case "所在城市" :
            paramID = "localCity";
            // $(".sift li").hide(); // 去掉筛选条件,此处不当，在global.js的可以
            $(".sift li[class = 'myline_province']").hide(); // 去掉省份条件
            $("#province").val("*");
            $("#localCity").val("*");
            filterSearch();
            return;
        default :
            break;
    }
    if ($("#" + paramID).val() == paramValue) {
        $("#" + paramID).val("*");
        $(".sift li[class = 'myline_" + paramID + "']").hide(); // 去掉相关筛选条件
        filterSearch();
    } else {
        $("#" + paramID).val(paramValue);
        filterSearch();
    }
}

function filterConditionByIns(paramID, paramValue) {
    $("#localCity").attr("value", "*");
    if ($("#" + paramID).val() == paramValue) {
        $("#" + paramID).val("*");
        $(".sift li[class = 'myline_institution']").hide(); // 去掉相关筛选条件
        filterSearch();
    } else {
        $("#" + paramID).val(paramValue);
        filterSearch();
    }
}

function filterConditionByCity(province, city) {
    $(".sift li[class = 'myline_province']").hide(); // 去掉所在地区条件

    $(".sift li[class = 'myline_localCity']").show(); // 出现所在城市条件
    $(".sift li[class = 'myline_localCity'] i").html(city.substring(0, city.length - 1));

    $("#province").val(province);
    $("#localCity").val(city);
    filterSearch();
    // $("#localCity").val("*");
}

function filterSearch() {
    $("#currentPage").attr("value", "1");
    $.ajax({
        type: "get",
        url: path + "/search/fs.do",
        data: $("#filterSearchForm").serialize(),
        dataType: "json",
        success: function (data) {
            if (data[1].baseInstruList != null && data[1].baseInstruList != "") {
                $("#instruResult").html(getSearchResultStr(data[1].baseInstruList));
            } else {
                $("#instruResult").html("");
            }
            $("#facetSearchResult").html(getFacetResultStr(data[0]));
            for (var num = 0; num < $('.myline').length; num++) {
                var myClass = $('.myline').eq(num).find('.myline_info').find('.myline_content').find('.myline_mid').find('li').attr('class');
                $('.myline').eq(num).find('.myline_info').attr('id', myClass + '_info');
            }

            myLineMadeIn = [];
            myLineProvince = [];
            myLineInstitution = [];
            myLineSubject = [];
            myLineInstruMainType = [];
            var myMadeIn = $('#myline_madeIn_info').find('.myline_content').find('.myline_mid').find('li');
            for (var madeIn = 0; madeIn < myMadeIn.length; madeIn++) {
                var myLineText = myMadeIn.eq(madeIn).find('a').html();
                if (madeIn < myMadeIn.length - 1) {
                    myLineMadeIn += myLineText + ',';
                } else if (madeIn == myMadeIn.length - 1) {
                    myLineMadeIn += myLineText;
                }
            }
            var myProvince = $('#myline_province_info').find('.myline_content').find('.myline_mid').find('li');
            for (var lineProvince = 0; lineProvince < myProvince.length; lineProvince++) {
                var myLineText = myProvince.eq(lineProvince).find('a').html();
                if (lineProvince < myProvince.length - 1) {
                    myLineProvince += myLineText + ',';
                } else if (lineProvince == myProvince.length - 1) {
                    myLineProvince += myLineText;
                }
            }
            var myInstitution = $('#myline_institution_info').find('.myline_content').find('.myline_mid').find('li');
            for (var lineInstitution = 0; lineInstitution < myInstitution.length; lineInstitution++) {
                var myLineText = myInstitution.eq(lineInstitution).find('a').html();
                if (lineInstitution < myInstitution.length - 1) {
                    myLineInstitution += myLineText + ',';
                } else if (lineInstitution == myInstitution.length - 1) {
                    myLineInstitution += myLineText;
                }
            }
            var mySubject = $('#myline_subject_info').find('.myline_content').find('.myline_mid').find('li');
            for (var lineSubject = 0; lineSubject < mySubject.length; lineSubject++) {
                var myLineText = mySubject.eq(lineSubject).find('a').html();
                if (lineSubject < mySubject.length - 1) {
                    myLineSubject += myLineText + ',';
                } else if (lineSubject == mySubject.length - 1) {
                    myLineSubject += myLineText;
                }
            }
            var myInstruMainType = $('#myline_instruMainType_info').find('.myline_content').find('.myline_mid').find('li');
            for (var InstruMainType = 0; InstruMainType < myInstruMainType.length; InstruMainType++) {
                var myLineText = myInstruMainType.eq(InstruMainType).find('a').html();
                if (InstruMainType < myInstruMainType.length - 1) {
                    myLineInstruMainType += myLineText + ',';
                } else if (InstruMainType == myInstruMainType.length - 1) {
                    myLineInstruMainType += myLineText;
                }
            }

            var myScriptTag = "<script id='scroll_bar' src='" + path + "/js/scroll_bar.js'></script>";
            $('body').append(myScriptTag);
            newSearch();
            newNumLength();
            $("#totalPageNum").html(data[1].page.totalPageNum);
            $("#totalPageNum1").html(data[1].page.totalPageNum);
            $("#lastPageNum").attr("value", data[1].page.totalPageNum);
            $("#currentPage1").html(1);
            $("#current").attr("value", 1); // 页面中显示的当前页码
            $("#resultNumForShow").html(data[1].page.totalNum);
            showPageNumber(1, data[1].page.totalPageNum);
            var myUlWidth = $('.instrument_right .page ul').width();
            var mySpanWidth = $('.instrument_right .page .all_page').width();
            $('.instrument_right .page').css('width', 63 + 6 + myUlWidth + 63 + 8 + mySpanWidth + 6 + 38);
        },
        error: function (data) {
            window.location.href = path + "/base/toIndex.do";
        }
    });
}

/**
 * 高级搜索
 */

/*function seniorSearch() {
    $("#subject").attr("value", $("#subjectSearch option:selected").val());
    $("#instruMainType").attr("value", $("#insMainTypeSearch option:selected").val());
    $("#instruType").attr("value", $("#instruTypeSearch option:selected").val());
    $("#province").attr("value", $("#provinceSearch option:selected").val());

	if ($("#institutionSearch").val() == null || $("#institutionSearch").val() == '') {
		$("#institution").attr("value", "*");
	} else {
		$("#institution").attr("value", $("#institutionSearch").val());
	}
    $("#seniorSearchForm").submit();
 //   $("#").attr("value", $("#").val());
}*/

/**
 * 仪器详细信息
 * @param id
 * @param type
 */
function instruDetail(id, type) {
    $("#instruID").attr("value", id);
    $("#detail_instruType").attr("value", type);
    $("#detailSearchForm").attr("target", "_blank");
    $("#detailSearchForm").submit();
}


/**
 * 首页左侧导航栏按仪器所属范畴搜索
 */
function searchInstruByInstitution(institution) {
    // $("#institution").val(institution); //用这种方式，点击历史栏的回退，会保留这个值，导致其他条件的搜索也会带上这个条件
    $("input[name=institution]").attr("value", institution);
    $("input[name=keyword]").attr("value", "*");
    $("#searchForm").submit();
}

/**
 * 首页左侧导航栏按仪器所属类型搜索
 */
function searchInstruByTypes(instr_main_type) {
    $("input[name=instruMainType]").attr("value", instr_main_type);
    $("input[name=instruType]").attr("value", "大型科研仪器");
    // $("#instruType").val("大型科研仪器");
    $("input[name=keyword]").val("*");
    $("#searchForm").submit();
}

/**
 * 首页左侧导航栏按学科领域
 */
function searchInstruBySubject(subject) {
    $("input[name=subject]").attr("value", subject);
    $("input[name=keyword]").attr("value", "*");
    $("#searchForm").submit();
}

/**
 * 首页左侧导航栏按地区搜索
 */
function searchInstruByRegion(province) {
    $("input[name=province]").attr("value", province);
    $("input[name=keyword]").attr("value", "*");
    $("#searchForm").submit();
}


//政策法规搜索
function regulationSearch() {
    var keywordtitle = $("#regulationKeyword").val();
    if (keywordtitle == null || keywordtitle == "") {
        alert("请输入文本");
    } else {
        $("#reguKeyword").attr("value", $("#regulationKeyword").val());
        $.ajax({
            type: "get",
            url: path1 + "/search/sr.do",
            data: $("#regulationSearchForm").serialize(),
            dataType: "text",
            success: function (data) {
                $("#show1").hide();
                $("#Showregulation").html(data);
            },
            error: function () {
                alert("出错,请稍后再试！");
            }
        });
    }
}

//在线平台搜索
function subPlatSearch() {
    //$("#searchSubplat").show();
    var keywords = $("#subPlatKeyword").val();
    if (keywords == "" || keywords == null) {
        alert("请输入文本！")
    } else {
        $("#keywords").attr("value", $("subPlatKeyword").val());
        $.ajax({
            type: "get",
            url: path + "/search/subplatsr.do",
            data: $("#pageForm_plat").serialize(),
            dataType: "text",
            success: function (data) {

                $('#searchSubplat').append("<li>" + data + "</li>");
            },
            error: function () {
                alert("出错，请稍后再试！");
            }
        })
    }
}

/**
 * 仪器预约部分
 * 目前只是把中科院的链接进行了直接跳转；
 * 后期会根据参数进行跳转
 */
function order(serviceURL, indexID, instruType) {
    $.ajax({
        type: "post",
        url: path + "/user/ifNull.do",
        data: {"indexID": indexID, "instruType": instruType},
        dataType: "text",
        success: function (data) {
            if (data == "false")
                alert("请先登录");
            else if (serviceURL == null || serviceURL == '')
                alert("预约地址为空！请与页面仪器联系人联系");
            else {
                if (serviceURL.indexOf('?') != -1)
                    window.location.href = serviceURL + "&platType=1";
                else
                    window.location.href = serviceURL + "?platType=1";
            }
        },
        error: function () {
            alert("出错,请稍后再试！");
        }
    });

}

/* ===================================== FZQ ================================= */
/**
 * 重新搜索的表单提交
 * @Author  FZQ
 */
function reSearch() {
    $("input[name=keyword]").attr("value", "*");
    $("#searchForm").submit();
}

function deSearch(instruType) {
    $("input[name=keyword]").attr("value", "*");
    $("input[name=instruType]").attr("value", instruType);
    $("option[name=instruType]").attr("value", instruType);
    $("#searchForm").submit();
}

function instruSearch(name) {
    $("#searchForm input[name='keyword']").attr("value", name);
    $("#searchForm").submit();
}

/**
 * 用于ajax页面部分搜索结果展示
 * @return
 */
function getSearchResultStr(baseInstruList) {
    var stringBuffer = "";
    for (var i = 0; i < baseInstruList.length; i++) {
        stringBuffer += (
            "<li>"
            + "<a href=javascript:; onclick=\"instruDetail('" + baseInstruList[i].instruID + "','" + baseInstruList[i].instruType + "');\" title='" + baseInstruList[i].cname + "'>"
            + "<img src='" + baseInstruList[i].imageURL + "' />"
            + "</a>"
            + "<p>" + baseInstruList[i].cname + "</p>"
            + "<p>" + baseInstruList[i].insName + "</p>"
            + "<div class='instru_eval'>"
            + "<span>" + simplifyProvince(baseInstruList[i].province) + " &nbsp;&nbsp; " + simplifyCity(baseInstruList[i].city) + "</span>"
            + "</div>"
            + "</li>"
        );
    }
    return stringBuffer.toString();
}

/**
 * 获得分类聚合的结果
 * @param facetResultMap
 * @return
 */
function getFacetResultStr(facetResultMap) {

    var stringBuffer = "";
    for (var key in facetResultMap) { //替代了原先的keySet()
        if (facetResultMap.hasOwnProperty(key) && key != "所在城市") { // 消除警告
            var facetValue = getFacetInstruTypeStr(key, facetResultMap[key]);
            stringBuffer += constructorFacetResult(key, facetValue);
        }
    }

    return stringBuffer.toString();
}

/**
 * 构造每个facet的聚合
 * @return
 */
function constructorFacetResult(facetTitle, facetValue) {
    if (facetTitle == "仪器类别" || facetTitle == "设施类别") {
        return "<div class='myline clearfix'>"
            + "<span>" + facetTitle + "</span>"
            + "<div class='myline_info'>"
            + "<div class='myline_special'>"
            + "<input type='text' placeholder='搜索 " + facetTitle + "'>"
            + "<em></em>"
            + "</div>"
            + "<div class='myline_content'>"
            + "<ul class='myline_mid clearfix'>" + facetValue + "</ul>"
            + "</div>"
            + "<div class='over_scroll_bar'>"
            + "<span></span>"
            + "</div>"
            + "</div>"
            + "</div>";
    } else {
        return "<div class='myline clearfix'>"
            + "<span>" + facetTitle + "</span>"
            + "<div class='myline_info'>"
            + "<div class='myline_special'>"
            + "<input type='text' placeholder='搜索 " + facetTitle + "'>"
            + "<em></em>"
            + "</div>"
            + "<div class='myline_content'>"
            + "<ul class='myline_mid clearfix'>" + facetValue + "</ul>"
            + "</div>"
            + "<div class='over_scroll_bar'>"
            + "<span></span>"
            + "</div>"
            + "</div>"
            + "<button>更多</button>"
            + "<i>多选</i>"
            + "<input type='button' class='ensure' value='确定'>"
            + "<input type='button' class='cancel' value='取消'>"
            + "</div>";
    }
}

/**
 * 逻辑判断主体
 * @param key, value
 * @return
 */
function getFacetInstruTypeStr(key, value) {
    var stringBuffer = "";
    if (key == "所在地区") {
        for (var i = 0; i < value.length; i++) {
            stringBuffer += ("<li class='myline_province'><a href='javascript:;' onclick='filterConditionByRegion(\"" + value[i][0] + "\")' title='" + value[i][0] + "'>" + value[i][0] + "</a></li>");
        }
    } else if (key == "所属单位") {
        for (i = 0; i < value.length; i++) {
            stringBuffer += ("<li class='myline_institution'><a href='javascript:;' onclick='filterConditionByIns(\"institution\",\"" + value[i][0] + "\")' title='" + value[i][0] + "'>" + value[i][0] + "</a></li>");
        }
    } else if (key == "所属分类") {
        for (i = 0; i < value.length; i++) {
            stringBuffer += ("<li class='myline_instruMainType'><a href='javascript:;' onclick='filterCondition(\"" + key + "\",\"" + value[i][0] + "\")' title='" + value[i][0] + "'>" + value[i][0] + "</a></li>");
        }
    } else if (key == "学科领域") {
        for (i = 0; i < value.length; i++) {
            stringBuffer += ("<li class='myline_subject'><a href='javascript:;' onclick='filterCondition(\"" + key + "\",\"" + value[i][0] + "\")' title='" + value[i][0] + "'>" + value[i][0] + "</a></li>");
        }
    } else if (key == "产地国别") {
        for (i = 0; i < value.length; i++) {
            stringBuffer += ("<li class='myline_madeIn'><a href='javascript:;' onclick='filterCondition(\"" + key + "\",\"" + value[i][0] + "\")' title='" + value[i][0] + "'>" + value[i][0] + "</a></li>");
        }
    } else if (key == "仪器类别" || key == "设施类别") {
        for (i = 0; i < value.length; i++) {
            stringBuffer += ("<li class='myline_type'><a href='javascript:;' onclick='filterCondition(\"" + key + "\",\"" + value[i][0] + "\")' title='" + value[i][0] + "'>" + value[i][0] + "</a></li>");
        }
    }
    return stringBuffer.toString();
}

/**
 * 把表单里省的原值放入临时存储，然后更改之，为了搜索其中的市
 * @param province
 */
function changeProvince(province) {
    $("#provinceTemp").val($("#province").val()); // 记忆原本的省份信息
    $("#province").val(province); // 传参
}

/**
 * 地区切换部分，点击省，下面出来市
 */
function showCities(index) {
    var line = parseInt(index / 6); //行数
    var cityBox = $('.address_hidden .prov').children('li[class="prov_hidden"]').eq(line); // 找到这一行元素的框

    cityBox.find('i').html('');  //先把city的值清空
    cityBox.show().siblings('.prov_hidden').hide();
    var ll = '';

    $.ajax({
        type: "get",
        url: path + "/search/cities.do",
        data: $("#filterSearchForm").serialize(),
        dataType: "json",
        success: function (data) {
            for (var i = 1; i < data.length; i++) { //第一个是省，所以从1开始
                ll += "<span><i onclick='filterConditionByCity(\"" + data[0][0] + "\",\"" + data[i][0] + "\")'>" + data[i][0] + "</i></span>";
            }
            cityBox.append(ll);
        },
        error: function (data) {
            alert("该省份没有相关仪器");
        }
    });
}

/**
 * 恢复表单里省的原值
 */
function recoverProvince() {
    $("#province").val($("#provinceTemp").val());
}

/**
 * 获取地址栏参数
 */
function getUriParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]).replace(/((<script)|(<iframe)|(<a))[\s\S]*((<\/script>)|(<\/iframe>)|(<\/a>)|(\/>))?/i, ""); // 去掉可能夹杂的js代码
    }
    return null;
}

/**
 * 简化省名
 */
function simplifyProvince(province) {
    var pro = "";
    switch (province) {
        case "北京市":
            pro = "";
            break;
        case "上海市":
            pro = "";
            break;
        case "天津市":
            pro = "";
            break;
        case "重庆市":
            pro = "";
            break;
        case "黑龙江省":
            pro = "黑龙江";
            break;
        case "内蒙古自治区":
            pro = "内蒙古";
            break;
        default:
            pro = province.substring(0, 2);
            break;
    }
    return pro;
}

/**
 * 简化市名
 */
function simplifyCity(city) {
    var len = city.length;
    if (city.substring(len - 1, len) == '市')
        return city.substring(0, len - 1);
    else
        return city;
}

/**
 * 大型装置专题的翻页
 */
function deviceSpecial(go) {
    var start = parseInt($("#start").val());
    if (0 <= start && start < 50)
        if (go == "pre")
            start -= 8;
        else
            start += 8;
    $("#start").val(start);
    if (start <= 0)
        $("#leftSpan").hide();
    else
        $("#leftSpan").show();
    if (start >= 48)
        $("#rightSpan").hide();
    else
        $("#rightSpan").show();

    $.ajax({
        type: "get",
        url: path + "base/large56.do",
        data: {"firstResult": start},
        dataType: "json",
        success: function (data) {
            var str = "";
            for (var i = 0; i < data.length; i++) {
                str += "<li>"
                    + "<a href='" + path + "/base/deviceSpecialDetail.do?specialId=" + data[i].id + "'>"
                    + "<img src='" + data[i].image + "'>"
                    + "<span>" + data[i].cname + "</span>"
                    + "</a></li>";
            }
            $("#larges").html(str);
        },
        error: function () {
            alert("好像没有了");
        }
    });
}


/* ===================================== /FZQ ================================= */
