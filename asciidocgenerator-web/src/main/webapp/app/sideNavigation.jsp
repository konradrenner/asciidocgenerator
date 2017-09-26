<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="doku" uri="/WEB-INF/tld/asciidocgenerator.tld"%>
<c:set var="language"
	value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
	scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle
	basename="org.asciidocgenerator.ui.messages.messages" />
<div id="sideNavigation">
	<span id="sideNavigationAllSelector" class="allSelectorNottoggled toggleIcon" onClick="SideNavigation.unfoldAll(this)"><fmt:message key="label_sidenavigation_allEntries" /></span>
	
	
	<ul id="sideNavigationLinks">
		<doku:tree tree="${sideNavigationService.navigation}"
			navigationPrefix="/${sideNavigationService.sideNavigationPrefix}/"
			contextPath="${pageContext.request.contextPath}"
			toggleFunction="SideNavigation.handleClick(this)" />
	</ul>
	
</div>
