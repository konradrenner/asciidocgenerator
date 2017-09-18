package org.asciidocgenerator;

import java.util.Set;

import org.asciidocgenerator.domain.RenderedDocument;

public class DocumentsRenderedEvent<T extends RenderedDocument> {

	private final Set<T> renderedDocuments;

	public DocumentsRenderedEvent(Set<T> renderedDocuments) {
		super();
		this.renderedDocuments = renderedDocuments;
	}

	public Set<T> getRenderedDocuments() {
		return renderedDocuments;
	}

	@Override
	public String toString() {
		return "DocumentsRenderedEvent [renderedDocuments=" + renderedDocuments + "]";
	}

}
