<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.wangxl.licensesystem.base.controller"%>
<%@ page import="com.wangxl.licensesystem.base.controller.BaseController" %>
<%
    String path = request.getContextPath();
    String rootPath=path+"/";
    if(BaseController.getValue("fullBaseUrl")==true)
    {
        rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path+"/";
    } 

%>

