<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="org.asciidocgenerator.ui.messages.messages" />
<div class="welcome-wrapper">
	<div class="welcome-message">
		<span id="welcome"><fmt:message key="label_welcome" /></span><br>
                <span class="medium module"><c:out value="${UIService.groupName}"/></span><span class="medium"><fmt:message key="label_of_company" /></span>
        </div>
</div>