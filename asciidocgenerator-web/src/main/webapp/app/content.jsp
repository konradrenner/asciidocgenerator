<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle
	basename="org.asciidocgenerator.ui.messages.messages" />
<div id="document">
  <input type="hidden" id="articleNavigationKey" value="${articleNavigationKey}"/>
	<c:if test="${not empty content}">
		${content}
</c:if>
</div>
<div id="infoModal" class="modal" onClick="Info.clickInModal(event)">
	<div class="modal-content">
		<div class="modal-header">
			${articleDetail.title}
			<a class="modalclose icon" onClick="Info.hideModal()"></a>
		</div>
		<div class="modal-body">	
			<p><strong><fmt:message key="label_details_renderTimestamp" /></strong> ${articleDetail.renderTimestamp}</p>
			<p><strong><fmt:message key="label_details_categories" /></strong> <c:if test="${not empty articleDetail.categories}">
						<c:forEach items="${articleDetail.categories}" var="x" varStatus="status">
							<c:if test="${!status.first}">,&nbsp;</c:if>
							<a href="<c:url value="/categories/list/${x.name}"/>" ><c:out value="${x.name}"/></a>
						</c:forEach> 
			</c:if></p>
			<hr>
			<p><strong><fmt:message key="label_details_metaInformation" /></strong></p>
			<div class="modal-metaInfos"> 
				<p><strong><fmt:message key="label_details_repositoryname" /></strong>  ${articleDetail.metaInformation.repositoryname}</p>
				<p><strong><fmt:message key="label_details_projectname" /></strong>  ${articleDetail.metaInformation.projektname}</p>
				<p><strong><fmt:message key="label_details_vcsversion" /></strong>  ${articleDetail.metaInformation.vcsversion}</p>
				<p><strong><fmt:message key="label_details_vcsurl" /></strong>  <a href="${articleDetail.metaInformation.vcsurl}" target="_blank">${articleDetail.metaInformation.vcsurl}</a></p>
			
			
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="<c:url value="/resources/js/pdf.js" />"></script>