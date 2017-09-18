package org.asciidocgenerator.domain;

import java.sql.Timestamp;

public abstract class AbstractRenderedDocumentBuilder<T extends RenderedDocument> {

	public abstract AbstractRenderedDocumentBuilder<T> addKeywords(Keyword... keywords);

	public abstract AbstractRenderedDocumentBuilder<T> renderTimestamp(Timestamp tst);

	public abstract T build();

	public interface WithTitle<T extends RenderedDocument> {

		WithGroup<T> title(String value);
	}

	public interface WithMainNavigation<T extends RenderedDocument> {

		WithNavigationPath<T> mainNavigation(String value);
	}

	public interface WithGroup<T extends RenderedDocument> {

		WithMainNavigation<T> group(String value);
	}

	public interface WithNavigationPath<T extends RenderedDocument> {

		WithNavigationKey<T> navigationPath(String value);
	}

	public interface WithNavigationKey<T extends RenderedDocument> {

		WithStorageLocation<T> navigationKey(String value);
	}

	public interface WithStorageLocation<T extends RenderedDocument> {

		WithMetainformation<T> storageLocation(String value);
	}

	public interface WithMetainformation<T extends RenderedDocument> {

		AbstractRenderedDocumentBuilder<T> metainformation(MetaInformation value);
	}
}