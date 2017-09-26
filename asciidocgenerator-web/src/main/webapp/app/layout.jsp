<%@page import="org.asciidocgenerator.ui.NavigationPathPrefix"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><c:out value="${UIService.titleName}" /></title>
	<base id="baseContext" href='<c:url value=""/>' target="_self"/>
        <link rel="icon" href="<c:url value="/resources/favicon.png" />" type="image/x-icon">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,300italic,400,400italic,600,600italic%7CNoto+Serif:400,400italic,700,700italic%7CDroid+Sans+Mono:400,700">
	<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/style.css" />" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/asciidoctor.css" />" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/coderay.css" />" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/roboto.css" />" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min-4.7.0.css" />" />
	<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/navigation.js" />"></script>
</head>

<body onclick="DropDown.close();">
	<div id="mainNav">
		<jsp:include page="mainNavigation.jsp"></jsp:include>
	</div>
		
	<div id="webContent">
		<c:if test="${not empty sidenavigation}">
			<jsp:include page="${sidenavigation}"></jsp:include>
		</c:if>
		<c:if test="${not empty contentpage}">
			<jsp:include page="${contentpage}"></jsp:include>
		</c:if>
	</div>
</body>
</html>
