package org.asciidocgenerator.domain;

import org.asciidocgenerator.DocumentsRenderedEvent;

public interface RenderedDocumentRegister {

	AbstractRenderedDocumentBuilder.WithTitle<? extends RenderedDocument> newRenderedDocument();

	Keyword createKeyword(String name);

	/**
	 * Publishes all registered documents via the {@link DocumentsRenderedEvent}
	 */
	void publishRegisteredDocuments();
}
