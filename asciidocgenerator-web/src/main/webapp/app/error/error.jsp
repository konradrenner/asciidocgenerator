<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="org.asciidocgenerator.ui.messages.messages" />
<html>
	<head>
	</head>
	<body>
		<div>
			<fmt:message key="label_error_code" /> <%= response.getStatus() %><br>
			<fmt:message key="label_call_sysadmin" />
		</div>
	</body>
</html>