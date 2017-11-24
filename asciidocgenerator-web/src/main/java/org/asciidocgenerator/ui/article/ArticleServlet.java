package org.asciidocgenerator.ui.article;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Pattern;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.Trail;
import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.domain.content.ContentService;
import org.asciidocgenerator.domain.rendering.RenderingPlaceHolder;
import org.asciidocgenerator.ui.ArticleSelectedEvent;
import org.asciidocgenerator.ui.HTMLErrorCodes;
import org.asciidocgenerator.ui.NavigationPathPrefix;
import org.asciidocgenerator.ui.PageService;
import org.asciidocgenerator.ui.controller.DefaultPageHandler;
import org.asciidocgenerator.ui.controller.PageTrail;

@WebServlet("/article/*")
@Logged
public class ArticleServlet
        extends HttpServlet {

    public static final Pattern CONTEXT_ROOT_PLACEHOLDER = Pattern.compile(RenderingPlaceHolder.CONTEXT_ROOT.getValue());
    private static final long serialVersionUID = 1L;

    @Inject
    private ContentService service;

    @Inject
    private Event<ArticleSelectedEvent> event;

    public ArticleServlet() {
    }

    // for testing
    ArticleServlet(ContentService service, Event<ArticleSelectedEvent> event) {
        this.service = service;
        this.event = event;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Trail trail = new PageService(req).getRequestedRelativeUrl();

        PageTrail strippedPageTrail = strippedPageTrail(trail);
        List<Article> articles = service.getArticles(strippedPageTrail);

        if (articles.isEmpty()) {
            RequestDispatcher rq = req.getRequestDispatcher("/app/error/error404.jsp");
            resp.sendError(HTMLErrorCodes.NOT_FOUND);
            rq.forward(req, resp);
        } else {
            if (trail.getFullPath().endsWith(".html")) {
                displayArticle(req, resp, trail, articles);
            } else {
                evaluateArticle(req, resp, articles);
            }
        }
    }

    /* private -> testing */
    PageTrail strippedPageTrail(Trail trail) throws UnsupportedEncodingException {
        String fullPath = URLDecoder.decode(trail.getFullPath(), "UTF-8");
        int firstSeperator = fullPath.indexOf(trail.getSeparator());
        int seperatorBeforeFilename = fullPath.length();
        if (fullPath.endsWith(".html")) {
            seperatorBeforeFilename = fullPath.lastIndexOf(trail.getSeparator());
        }
        return new PageTrail(fullPath.substring(firstSeperator + 1, seperatorBeforeFilename));
    }

    /* private -> testing */
    void evaluateArticle(HttpServletRequest req, HttpServletResponse resp, List<Article> articles) throws IOException,
            ServletException {
        if (articles.size() == 1) {
            final String requestURI = req.getRequestURI();
            resp.sendRedirect(requestURI + "/" + articles.get(0).getFilename());
        } else {
            new DefaultPageHandler(new MultipleArticlePageController(articles)).doWork(req, resp);
        }
    }

    /* private -> testing */
    void displayArticle(HttpServletRequest req,
            HttpServletResponse resp,
            Trail trail,
            List<Article> articles) throws ServletException, IOException {
        List<String> fragments = trail.getFragments();

        for (Article article : articles) {
            if (fragments.get(fragments.size() - 1).equals(article.getFilename())) {
                ArticleSelectedEvent selectedEvent = new ArticleSelectedEvent(NavigationPathPrefix.ARTICLE,
                        article.getGroup(),
                        article.getNavigationPath(),
                        article.getTitle());
                event.fire(selectedEvent);

                new DefaultPageHandler(new SingleArticlePageController(req, resp, article)).doWork(req, resp);
                return;
            }
        }
        resp.sendError(HTMLErrorCodes.NOT_FOUND);
    }
}
