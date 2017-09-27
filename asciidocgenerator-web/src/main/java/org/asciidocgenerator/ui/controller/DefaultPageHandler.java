package org.asciidocgenerator.ui.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.ui.HTMLErrorCodes;
import org.asciidocgenerator.ui.PageService;

public class DefaultPageHandler {

	private PageController controller;

	public DefaultPageHandler(PageController controller) {
		this.controller = controller;
	}

	public DefaultPageHandler() {
	}

	public void doWork(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			if (this.controller != null) {
				this.controller.execute(new PageService(req));
			}
			RequestDispatcher rd = req.getRequestDispatcher("/app/layout.jsp");
			rd.forward(req, resp);
		} catch (DokuGeneratorException e) {
			resp.sendError(HTMLErrorCodes.NOT_FOUND);
		}
	}
}
