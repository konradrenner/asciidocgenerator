package org.asciidocgenerator.ui.article;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.domain.content.ContentService;
import org.asciidocgenerator.domain.rendering.PdfRenderServiceBean;

@WebServlet("/pdf/*")
@Logged
public class PdfServlet
		extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private PdfRenderServiceBean renderServiceBean;

	@Inject
	private ContentService contentService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String navigationKey = req.getRequestURL().toString();
		navigationKey = navigationKey.substring(navigationKey.lastIndexOf('/') + 1);
		Article article = contentService.getArticleByNavigationKey(navigationKey);
		Path sourcefile = Paths.get(article.getStorageLocation());

		String filename = article.getFilename();
		filename = filename.substring(0, filename.lastIndexOf('.'));

		resp.setContentType("application/pdf");
		resp.setHeader("Content-disposition", "attachment; filename=" + filename + ".pdf");

		try (OutputStream destination = resp.getOutputStream()) {
			renderServiceBean.streamPdf(sourcefile, destination);
		}
	}

}
