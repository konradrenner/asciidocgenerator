package org.asciidocgenerator.ui;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.ui.controller.ContentPage;
import org.asciidocgenerator.ui.controller.DefaultPageHandler;
import org.asciidocgenerator.ui.controller.PageController;

@WebServlet("/error/*")
public class ErrorServlet
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new DefaultPageHandler(new PageController() {

			@SuppressWarnings("synthetic-access")
			public void execute(PageService pageService) throws ServletException, IOException {
				if (resp.getStatus() == 404) {
					ContentPage contentPage = ContentPage
															.newContentPage("app/error/error.jsp")
															.build();
					pageService.setContent(contentPage);
				} else {
					RequestDispatcher rd = req.getRequestDispatcher("/app/error/error.jsp");
					resp.sendError(500);
					rd.forward(req, resp);
				}
			}
		}).doWork(req, resp);
	}
}
