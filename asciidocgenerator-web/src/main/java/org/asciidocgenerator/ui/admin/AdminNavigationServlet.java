package org.asciidocgenerator.ui.admin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.Trail;
import org.asciidocgenerator.ui.PageService;
import org.asciidocgenerator.ui.controller.ContentPage;
import org.asciidocgenerator.ui.controller.DefaultPageHandler;
import org.asciidocgenerator.ui.controller.PageController;

@WebServlet("/admin/*")
@Logged
public class AdminNavigationServlet
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private Event<ManipulateSettingCommand> command;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		new DefaultPageHandler(new PageController() {

			@Override
			public void execute(PageService pageService) throws ServletException, IOException {
				Trail relativeUrl = pageService.getRequestedRelativeUrl();
				List<String> fragments = relativeUrl.getFragments();

				ContentPage contentPage = ContentPage	.newContentPage("admin/"	+ fragments.get(fragments.size() - 1)
																		+ ".jsp")
														.build();
				pageService.setContent(contentPage);
				pageService.setSideNavigation("/app/sideNavigation.jsp");
			}
		}).doWork(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String[]> parameterMap = req.getParameterMap();

		PageService pageService = new PageService(req);
		Trail relativeUrl = pageService.getRequestedRelativeUrl();
		List<String> fragments = relativeUrl.getFragments();

		ManipulateSettingCommand settingCommand = new ManipulateSettingCommand(	fragments.get(fragments.size() - 1),
																				parameterMap);
		command.fire(settingCommand);

		doGet(req, resp);
	}

}
