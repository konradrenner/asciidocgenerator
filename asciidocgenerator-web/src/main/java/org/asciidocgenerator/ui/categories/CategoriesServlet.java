package org.asciidocgenerator.ui.categories;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.content.CategoriesService;
import org.asciidocgenerator.ui.PageService;
import org.asciidocgenerator.ui.controller.ContentPage;
import org.asciidocgenerator.ui.controller.DefaultPageHandler;
import org.asciidocgenerator.ui.controller.PageController;

@WebServlet("/categories/*")
@Logged
public class CategoriesServlet
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriesService service;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new DefaultPageHandler(new PageController() {
			@Override
			public void execute(PageService pageService) throws ServletException, IOException {
				ContentPage contentPage = ContentPage	.newContentPage("/app/categories.jsp")
														.build();
				pageService.setCategoriesList(service.getCategories());
				pageService.setContent(contentPage);
			}
		}).doWork(req, resp);
	}
}
