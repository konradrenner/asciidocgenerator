package org.asciidocgenerator.ui.navigation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.ui.PageService;
import org.asciidocgenerator.ui.controller.DefaultPageHandler;
import org.asciidocgenerator.ui.controller.PageController;

@WebServlet("/navigation/*")
@Logged
public class NavigationServlet
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new DefaultPageHandler(new PageController() {

			@Override
			public void execute(PageService pageService) throws ServletException, IOException {
				pageService.setSideNavigation("/app/sideNavigation.jsp");
			}
		}).doWork(req, resp);
	}
}
