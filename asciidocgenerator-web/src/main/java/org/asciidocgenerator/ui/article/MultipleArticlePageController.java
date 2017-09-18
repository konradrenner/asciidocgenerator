package org.asciidocgenerator.ui.article;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.ui.PageService;
import org.asciidocgenerator.ui.controller.ContentPage;
import org.asciidocgenerator.ui.controller.PageController;

public class MultipleArticlePageController
		implements PageController {

	private final List<Article> articles;
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public MultipleArticlePageController(HttpServletRequest req, HttpServletResponse resp, List<Article> articles) {
		super();
		this.articles = articles;
		this.request = req;
		this.response = resp;
	}

	@Override
	public void execute(PageService pageService) throws ServletException, IOException {
		ContentPage contentPage = ContentPage.newContentPage("/app/contentlist.jsp").build();
		pageService.setArticleList(articles);
		pageService.setSideNavigation("/app/sideNavigation.jsp");
		pageService.setContent(contentPage);
	}
}
