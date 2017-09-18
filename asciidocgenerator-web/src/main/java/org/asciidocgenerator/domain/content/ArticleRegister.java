package org.asciidocgenerator.domain.content;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.asciidocgenerator.DocumentsRenderedEvent;
import org.asciidocgenerator.domain.AbstractRenderedDocumentBuilder;
import org.asciidocgenerator.domain.Keyword;
import org.asciidocgenerator.domain.MetaInformation;
import org.asciidocgenerator.domain.RenderedDocument;
import org.asciidocgenerator.domain.RenderedDocumentRegister;
import org.asciidocgenerator.domain.AbstractRenderedDocumentBuilder.WithTitle;

@Dependent
public class ArticleRegister
		implements RenderedDocumentRegister {

	@Inject
	private Event<DocumentsRenderedEvent<Article>> event;

	private Set<Article> renderedArticles;

	@PostConstruct
	void init() {
		this.renderedArticles = new HashSet<>();
	}

	@Override
	public WithTitle<? extends RenderedDocument> newRenderedDocument() {
		return new ArticleBuilder(renderedArticles::add);
	}

	@Override
	public Keyword createKeyword(String name) {
		return new Categorie(name);
	}

	@Override
	public void publishRegisteredDocuments() {
		DocumentsRenderedEvent<Article> ev = new DocumentsRenderedEvent<>(renderedArticles);
		event.fire(ev);
	}

	static class ArticleBuilder
			extends AbstractRenderedDocumentBuilder<Article>
			implements AbstractRenderedDocumentBuilder.WithTitle<Article>,
			AbstractRenderedDocumentBuilder.WithMainNavigation<Article>,
			AbstractRenderedDocumentBuilder.WithNavigationPath<Article>,
			AbstractRenderedDocumentBuilder.WithStorageLocation<Article>,
			AbstractRenderedDocumentBuilder.WithMetainformation<Article>,
			AbstractRenderedDocumentBuilder.WithNavigationKey<Article>,
			AbstractRenderedDocumentBuilder.WithGroup<Article> {

		private String storageLocation;
		private String navigationPath;
		private String mainNavigation;
		private String group;
		private String title;
		private String navigationKey;
		private MetaInformation metainformation;
		private Timestamp renderTimestamp;
		private Set<Categorie> categories;
		private Consumer<Article> consumer;

		ArticleBuilder(Consumer<Article> consumer) {
			renderTimestamp = new Timestamp(System.currentTimeMillis());
			categories = new HashSet<>();
			this.consumer = consumer;
		}

		@Override
		public WithMainNavigation<Article> group(String value) {
			this.group = value;
			return this;
		}

		@Override
		public AbstractRenderedDocumentBuilder<Article> metainformation(MetaInformation value) {
			this.metainformation = value;
			return this;
		}

		@Override
		public WithMetainformation<Article> storageLocation(String value) {
			this.storageLocation = value;
			return this;
		}

		@Override
		public WithNavigationKey<Article> navigationPath(String value) {
			this.navigationPath = value;
			return this;
		}

		@Override
		public WithStorageLocation<Article> navigationKey(String value) {
			this.navigationKey = value;
			return this;
		}

		@Override
		public WithNavigationPath<Article> mainNavigation(String value) {
			this.mainNavigation = value;
			return this;
		}

		@Override
		public WithGroup<Article> title(String value) {
			this.title = value;
			return this;
		}

		@Override
		public AbstractRenderedDocumentBuilder<Article> addKeywords(Keyword... keywords) {
			Arrays.stream(keywords).map(Categorie::new).forEach(categories::add);
			return this;
		}

		@Override
		public AbstractRenderedDocumentBuilder<Article> renderTimestamp(Timestamp tst) {
			this.renderTimestamp = tst;
			return this;
		}

		@Override
		public Article build() {
			ArticleKey key = new ArticleKey(navigationPath, storageLocation);
			Article article =
							new Article(key,
										navigationKey,
										group,
										mainNavigation,
										title,
										metainformation,
										renderTimestamp);
			article.addCategories(categories);
			consumer.accept(article);
			return article;
		}

	}
}
