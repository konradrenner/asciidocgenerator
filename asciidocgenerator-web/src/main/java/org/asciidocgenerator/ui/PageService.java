package org.asciidocgenerator.ui;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.asciidocgenerator.Trail;
import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.domain.content.Categorie;
import org.asciidocgenerator.ui.controller.ContentPage;
import org.asciidocgenerator.ui.controller.PageTrail;

public class PageService {

	private static final String LAYOUT_CONTENT_PAGE = "contentpage";
	private static final String PAGE_CONTENT = "content";
	private static final String LAYOUT_SIDE_NAVIGATION = "sidenavigation";
	private static final String ARTICLE_LIST = "articleList";
	private static final String ARTICLE_DETAIL = "articleDetail";
	private static final String CATEGORIES_LIST = "categoriesList";
	private static final String NAVIGATION_KEY = "articleNavigationKey";

	private HttpServletRequest request;

	public PageService(HttpServletRequest request) {
		this.request = request;
	}

	public void setContent(ContentPage page) {
		request.setAttribute(LAYOUT_CONTENT_PAGE, page.getRelContentPagePath());
		request.setAttribute(PAGE_CONTENT, page.getContent());
	}

	public void setSideNavigation(String page) {
		request.setAttribute(LAYOUT_SIDE_NAVIGATION, page);
	}

	public void setArticleList(List<Article> articles) {
		request.setAttribute(ARTICLE_LIST, articles);
	}

	public void setArticle(Article article) {
		request.setAttribute(ARTICLE_DETAIL, article);
	}

	public void setCategoriesList(List<Categorie> categories) {
		request.setAttribute(CATEGORIES_LIST, categories);
	}

	public void setArticleNavigationKey(String categories) {
		request.setAttribute(NAVIGATION_KEY, categories);
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Gibt den aufgerufenene Relativen Pfad zurück:<br/>
	 * Aufgerufener Pfad: http://localhost:8080/asciidoctorgenerator-web/article/Projekt2.html<br/>
	 * Ergebnis: article/Projekt2.html
	 * 
	 * @return aufgerufenene Relativen Pfad
	 */
	public Trail getRequestedRelativeUrl() {
		String calledUri = request.getRequestURI();
		String contextRoot = request.getServletContext().getContextPath();

		int startOfRelativeUrl = calledUri.indexOf(contextRoot) + contextRoot.length() + 1;
		return new PageTrail(calledUri.substring(startOfRelativeUrl));
	}
}
