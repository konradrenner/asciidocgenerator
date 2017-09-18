<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="org.asciidocgenerator.ui.messages.messages" />
<fmt:message key="label_btn_reorg" var="button_reorg" />
<div class="reorgPage">
	<form action = "${pageContext.request.contextPath}/admin/${UIService.groupId}/settings/reorg" method = "POST">
		<span class="reorgRow"><fmt:message key="description_reorg" /></span>
  	<input class="dokuSubmit" type="submit"  value="${button_reorg}">
  </form>
</div>
