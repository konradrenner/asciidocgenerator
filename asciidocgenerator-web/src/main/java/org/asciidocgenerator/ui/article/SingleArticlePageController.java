package org.asciidocgenerator.ui.article;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.domain.rendering.HtmlDocument;
import org.asciidocgenerator.ui.PageService;
import org.asciidocgenerator.ui.controller.ContentPage;
import org.asciidocgenerator.ui.controller.PageController;

public class SingleArticlePageController
		implements PageController {

	private final Article article;
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public SingleArticlePageController(HttpServletRequest req, HttpServletResponse resp, Article article) {
		super();
		this.article = article;
		this.request = req;
		this.response = resp;
	}

	@Override
	public void execute(PageService pageService) throws ServletException, IOException {
		HtmlDocument html = new HtmlDocument(article.getKey().getAblagepfad());
		if (!html.exists()) {
			response.sendError(404);
			return;
		}

		String content = html.readContent();

		content = ArticleServlet.CONTEXT_ROOT_PLACEHOLDER.matcher(content).replaceAll(request.getContextPath());

		ContentPage contentPage = ContentPage.newContentPage("/app/content.jsp").withContent(content).build();
		pageService.setContent(contentPage);
		pageService.setArticleNavigationKey(article.getNavigationKey());
		pageService.setSideNavigation("/app/sideNavigation.jsp");
		pageService.setArticle(article);
	}
}
