<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="org.asciidocgenerator.ui.messages.messages" />
<fmt:message key="label_store" var="button_store" />
<div class="groupsPage">
  <form action = "${pageContext.request.contextPath}/admin/${UIService.groupId}/settings/groups" method = "POST">
    <div class="divTable paleBlueRows">
			<div class="divTableHeading">
				<div class="divTableRow">
					<div class="divTableHead"><fmt:message key="label_id" /></div>
					<div class="divTableHead"><fmt:message key="label_description" /></div>
				</div>
			</div>
			<div class="divTableBody">
				<c:forEach var="group" items="${groupsController.groups}">
					<div class="divTableRow">
						<div class="divTableCell"><label for="${group.id}">${group.id}</label></div>
						<div class="divTableCell"><input type="text" name="${group.id}" id="${group.id}" value="${group.name}" class="divTableInput"></div>
					</div>
				</c:forEach>
			</div>
		</div>
		<input class="dokuSubmit" type="submit" value="${button_store}">
  </form>
</div>
