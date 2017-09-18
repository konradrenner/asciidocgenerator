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
<div id="document">
	<div id="articleList">
		<div id="articleListHeader">
			<header>
				<fmt:message key="label_articles" />
			</header>
		</div>
		<ul class="articleList">
			<c:if test="${not empty articleList}">
				<c:forEach var="article" items="${articleList}">
					<li><a
						href="<c:url value="/article/${article.key.navigationspfad}/${article.filename}" />">${article.title}</a></li>
				</c:forEach>
			</c:if>
		</ul>
	</div>
</div>