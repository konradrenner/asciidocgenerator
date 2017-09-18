package org.asciidocgenerator.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.ui.controller.ContentPage;
import org.asciidocgenerator.ui.controller.DefaultPageHandler;
import org.asciidocgenerator.ui.controller.PageController;

@WebServlet("/index")
@Logged
public class WelcomeServlet
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new DefaultPageHandler(new PageController() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void execute(PageService pageService) throws ServletException, IOException {
				ContentPage contentPage = ContentPage.newContentPage("/app/index.jsp").build();
				pageService.setContent(contentPage);
			}
		}).doWork(req, resp);
	}
}
