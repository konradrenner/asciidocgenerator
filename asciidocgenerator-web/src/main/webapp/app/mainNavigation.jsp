<%@page import="org.asciidocgenerator.ui.NavigationPathPrefix"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="org.asciidocgenerator.ui.messages.messages" />

<div class="mainNavigation">
	<ul>
		<li>
			<ul>
				<li>
					<span id="gruppenAuswahlButton" class="icon" onclick="DropDown.toggle(event);"></span>
				</li>
			</ul>
		</li>
		<li>
			<ul>
				<li>
					<ul>
					  	<c:out value="${UIService.groupName}" />
					</ul>
				</li>
				<li>
					<ul id="mainNavigationItem">
						<c:forEach var="documentMainNavigationTab"
							items="${mainNavigationService.documentNavigation}">
							<li id="${documentMainNavigationTab.name}" class="link"><a
								href="<c:url value="/navigation/${UIService.groupId}/${documentMainNavigationTab.name}" />"><c:out value="${documentMainNavigationTab.name}"/></a></li>
						</c:forEach>
					</ul>
				</li>
			</ul>
		</li>
		<li>
			<ul id="mainNavigationActions">
				<li class="iconhidden"><a onClick="Info.showModal()" id="detailsIcon" class="icon"></a></li>
				<li class="iconhidden"><a href="<c:url value="/pdf/"/>"  target="_blank" id="pdf" class="icon"></a></li>
				<li><a href="<c:url value="/categories/list"/>" id="categoriesIcon" class="icon"></a></li>
				<li><a href="<c:url value="/admin/${UIService.groupId}/groups"/>" id="adminIcon" class="icon"></a></li>
			</ul>
		</li>
	</ul>
</div>

	
<div id="dropdown" class="dropdown-content">
	<c:forEach var="navigationGroup" items="${mainNavigationService.navigationGroups}">
  	<a href="<c:url value="/navigation/${navigationGroup.id}" />" ><c:out value="${navigationGroup.name}" /></a>
	</c:forEach>
</div>