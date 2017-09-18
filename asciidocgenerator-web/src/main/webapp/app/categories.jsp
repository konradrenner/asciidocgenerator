<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle
	basename="org.asciidocgenerator.ui.messages.messages" />
<div id="wrapper-categories">
	<div id="wrapper-inner-categories">
		<div id="wrapper-nav-categories" class="categories">
			<header>
				<fmt:message key="label_categories" />
			</header>
			<ul id="categories">
			<c:if test="${not empty categoriesList}">
				<c:forEach items="${categoriesList}" var="categorie">
					<li><a href="#" onclick="Category.select(this,'<c:out value="${categorie.name}"/>')"><c:out value = "${categorie.name}" /></a></li>
				</c:forEach>
			</c:if>
			</ul>
		</div>
		<div id="categoriesContent" class="categories">
		</div>
	</div>
</div>