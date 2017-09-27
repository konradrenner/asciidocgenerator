package org.asciidocgenerator.ui.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import org.asciidocgenerator.ui.PageService;

public interface PageController {

	void execute(PageService pageService) throws ServletException, IOException;

}
