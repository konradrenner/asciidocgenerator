package org.asciidocgenerator.ui.categories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.domain.content.CategoriesService;
import org.asciidocgenerator.ui.PageService;

@WebServlet("/category")
@Logged
public class CategoryServlet
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriesService service;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String categoriename = req.getParameter("categoriename");

		Set<Article> articles = service.getCategory(categoriename).getArticles();

		new PageService(req).setArticleList(new ArrayList<>(articles));
		RequestDispatcher rd = req.getRequestDispatcher("/app/contentlist.jsp");
		rd.forward(req, resp);
	}
}
