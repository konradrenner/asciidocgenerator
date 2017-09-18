package org.asciidocgenerator.ui.article;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asciidocgenerator.Trail;
import org.asciidocgenerator.domain.MetaInformation;
import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.domain.content.ArticleKey;
import org.asciidocgenerator.domain.content.ContentService;
import org.asciidocgenerator.ui.ArticleSelectedEvent;
import org.asciidocgenerator.ui.NavigationPathPrefix;
import org.asciidocgenerator.ui.PageService;
import org.asciidocgenerator.ui.controller.PageTrail;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServletTest {

	@Mock
	private HttpServletRequest req;

	@Mock
	private HttpServletResponse resp;

	@Mock
	private ContentService service;

	@Mock
	private Article article;

	@Mock
	private Event<ArticleSelectedEvent> event;

	private ArticleServlet underTest;

	private ArticleServlet spyUnderTest;

	private List<Article> articles;

	@Before
	public void setUp() {
		underTest = new ArticleServlet(service, event);
		spyUnderTest = spy(underTest);
		articles = new ArrayList<Article>();
	}

	@Test
	public void multipleArticles() throws IOException, ServletException {
		articles.add(article);
		articles.add(article);
		when(req.getRequestDispatcher("/app/layout.jsp")).thenReturn(mock(RequestDispatcher.class));

		underTest.evaluateArticle(req, resp, articles);

		verify(resp, never()).sendRedirect(req.getRequestURI() + "/" + articles.get(0).getFilename());


	}

	@Test
	public void oneArticle() throws IOException, ServletException {
		articles.add(article);

		underTest.evaluateArticle(req, resp, articles);

		verify(resp).sendRedirect(req.getRequestURI() + "/" + articles.get(0).getFilename());

	}

	@Test
	public void noArticles() throws ServletException, IOException {
		PageTrail trail = new PageTrail("article/Test/Fahrzeuge/Aut1os");

		RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
		PageService pageService = mock(PageService.class);
		when(req.getServletContext()).thenReturn(mock(ServletContext.class));
		when(req.getServletContext().getContextPath()).thenReturn("");
		when(req.getRequestURI()).thenReturn("article/Test/Fahrzeuge/Aut1os");
		when(pageService.getRequestedRelativeUrl()).thenReturn(trail);

		when(service.getArticles(new ArticleServlet().strippedPageTrail(trail))).thenReturn(articles);
		when(req.getRequestDispatcher("/app/error/error404.jsp")).thenReturn(requestDispatcher);

		spyUnderTest.doGet(req, resp);

		verify(req).getRequestDispatcher("/app/error/error404.jsp");
	}

	@Test
	public void pathWithHtml() throws ServletException, IOException {
		PageTrail trail = new PageTrail("article/Test/Fahrzeuge/Autos/Bentley/Bentley.html");

		articles.add(new Article(	ArticleKey	.newInstance()
												.navigationspfad("Test/Fahrzeuge/Autos/Bentley")
												.ablagepfad("C:\tmp/article/Projekt1/Bentley/Bentley.html")
												.build(),
									"Test",
									"Projekt1",
									"TestKey",
									"Bently",
									MetaInformation	.newInstance()
													.projektname("Projekt1")
													.repositoryname("Projekt1")
													.vcsversion("1.0.0")
													.vcsurl("gitlab.e-bk.m086/Projekt1")
													.build(),
									new Timestamp(10000)));
		RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
		PageService pageService = mock(PageService.class);
		when(req.getServletContext()).thenReturn(mock(ServletContext.class));
		when(req.getServletContext().getContextPath()).thenReturn("");
		when(req.getRequestURI()).thenReturn("/article/Test/Fahrzeuge/Autos/Bentley/Bentley.html");
		when(pageService.getRequestedRelativeUrl()).thenReturn(trail);

		when(service.getArticles(new ArticleServlet().strippedPageTrail(trail))).thenReturn(articles);
            when(req.getRequestDispatcher("/app/layout.jsp")).thenReturn(requestDispatcher);

		spyUnderTest.doGet(req, resp);

		verify(spyUnderTest).displayArticle(req, resp, trail, articles);

	}

	@Test
	public void pathWithoutHtml() throws ServletException, IOException {
		PageTrail trail = new PageTrail("article/Test/Fahrzeuge/Autos");

		articles.add(new Article(	ArticleKey	.newInstance()
												.navigationspfad("Test/Fahrzeuge/Autos")
												.ablagepfad("C:\tmp/article/Projekt1/Autos.html")
												.build(),
									"Test",
									"Fahrzeuge",
									"d56acabd-d405-4025-b391-9a508606bcc0",
									"Auto Kollektion",
									MetaInformation	.newInstance()
													.projektname("Projekt1")
													.repositoryname("Projekt1")
													.vcsversion("1.0.0")
													.vcsurl("gitlab.e-bk.m086/Projekt1")
													.build(),
									new Timestamp(10000)));
		articles.add(new Article(	ArticleKey	.newInstance()
												.navigationspfad("Test/Fahrzeuge/Autos")
												.ablagepfad("C:\tmp/article/Projekt1/Maserati.html")
												.build(),
												"Test",
									"Fahrzeuge",
									"b49c2cfd-57ac-433d-9ad2-69496fb61c88",
									"Auto Kollektion 2",
									MetaInformation	.newInstance()
													.projektname("Projekt1")
													.repositoryname("Projekt1")
													.vcsversion("1.0.0")
													.vcsurl("gitlab.e-bk.m086/Projekt1")
													.build(),
									new Timestamp(10000)));
		RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
		PageService pageService = mock(PageService.class);
		when(req.getServletContext()).thenReturn(mock(ServletContext.class));
		when(req.getServletContext().getContextPath()).thenReturn("");
		when(req.getRequestURI()).thenReturn("/article/Test/Fahrzeuge/Autos");
		when(pageService.getRequestedRelativeUrl()).thenReturn(trail);
		when(service.getArticles(new ArticleServlet().strippedPageTrail(trail))).thenReturn(articles);
		when(req.getRequestDispatcher("/app/layout.jsp")).thenReturn(requestDispatcher);

		spyUnderTest.doGet(req, resp);

		verify(spyUnderTest).evaluateArticle(req, resp, articles);
	}

	@Test
	public void strippedPageTrail_endsWithHtml() throws UnsupportedEncodingException {
		PageTrail trail = new PageTrail("article/Test/Fahrzeuge/Autos/Bentley/Bentley.html");

		PageTrail result = underTest.strippedPageTrail(trail);

		assertEquals(new PageTrail("Test/Fahrzeuge/Autos/Bentley").toString(), result.toString());

	}

	@Test
	public void strippedPageTrail_endsNotWithHtml() throws UnsupportedEncodingException {
		PageTrail trail = new PageTrail("article/Group/Navi/testing.pdf");

		PageTrail result = underTest.strippedPageTrail(trail);

		assertEquals(new PageTrail("Group/Navi/testing.pdf").toString(), result.toString());

	}

	@Test
	public void noArticle() throws ServletException, IOException {
		Timestamp timestamp = mock(Timestamp.class);
		String navigationKey = "TestKey";
		ArticleKey articleKey = mock(ArticleKey.class);
		MetaInformation metaInformation = mock(MetaInformation.class);
		Trail trail = new PageTrail("Test/testing.html");
		articles.add(new Article(articleKey, "Test", "Fahrzeuge", navigationKey, "Test1", metaInformation, timestamp));
		articles.add(new Article(articleKey, "Test", "Fahrzeuge", navigationKey, "Test2", metaInformation, timestamp));
		articles.add(new Article(articleKey, "Test", "Fahrzeuge", navigationKey, "Test3", metaInformation, timestamp));
            when(articleKey.getAblagepfad()).thenReturn("/Test/testing_wrong.html");

		spyUnderTest.displayArticle(req, resp, trail, articles);

		verify(resp).sendError(404);


	}

	@Test
	public void displayArticle() throws ServletException, IOException {
		Timestamp timestamp = mock(Timestamp.class);
		String navigationKey = "TestKey";
		ArticleKey articleKey = mock(ArticleKey.class);
		MetaInformation metaInformation = mock(MetaInformation.class);
            Trail trail = new PageTrail("TestFile/Test/testing.html");
            Article articleUnderTest = new Article(articleKey,
                    "Test",
                    "Fahrzeuge",
                    navigationKey,
                    "testing.html",
                    metaInformation,
                    timestamp);
            articles.add(articleUnderTest);
            when(articleKey.getAblagepfad()).thenReturn("testing.html");
		RequestDispatcher mockRequestDispatcher = mock(RequestDispatcher.class);
		when(req.getRequestDispatcher("/app/layout.jsp")).thenReturn(mockRequestDispatcher);

            spyUnderTest.displayArticle(req, resp, trail, articles);
            
            ArticleSelectedEvent selectedEvent = new ArticleSelectedEvent(NavigationPathPrefix.ARTICLE, articleUnderTest.getGroup(), articleUnderTest.getNavigationPath(), articleUnderTest.getTitle());

            verify(event).fire(selectedEvent);

	}
}
