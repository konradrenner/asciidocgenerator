<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="org.asciidocgenerator.ui.messages.messages" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EA210 Doku Plattform</title>
        <base id="baseContext" href="<c:url value=""/>" target="_self" />
        <link type="text/css" rel="stylesheet"	href="<c:url value="/resources/css/style.css" />" />
	<link type="text/css" rel="stylesheet"	href="<c:url value="/resources/css/error.css" />" />
	<link type="text/css" rel="stylesheet"	href="<c:url value="/resources/css/roboto.css" />" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/resources/css/font-awesome.min-4.7.0.css" />" />
	<link type="text/css" rel="stylesheet"	href="<c:url value="/resources/css/asciidoctor.css" />" />
	<link rel="icon" href="<c:url value="/resources/favicon.ico" />" type="image/x-icon">
	<script type="text/javascript" src="<c:url value="/resources/js/navigation.js" />"></script>
</head>
<body>
	<div id="mainNavigation">
		<jsp:include page="../mainNavigation.jsp"></jsp:include>
	</div>
	<div id="webContent">
		<div id="errorMessage">
			<div>
				<span id="errorCode">404</span><span id="message"><fmt:message key="error_404" /></span><br>
				<span id="link"><a href="${pageContext.request.contextPath}"><fmt:message key="label_home" /></a></span>
			</div>
		</div>
	</div>
</body>
</html>