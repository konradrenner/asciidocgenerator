package org.asciidocgenerator.ui.controller;

public class ContentPage {

	private String relContentPagePath;
	private String content;

	private ContentPage() {
		super();
	}

	public String getRelContentPagePath() {
		return relContentPagePath;
	}

	public String getContent() {
		return content;
	}

	@SuppressWarnings("synthetic-access")
	public static Builder newContentPage(String relContentPagePath) {
		return new Builder(relContentPagePath);
	}

	public static class Builder {

		private ContentPage page;

		@SuppressWarnings("synthetic-access")
		private Builder(String relContentPagePath) {
			this.page = new ContentPage();
			this.page.relContentPagePath = relContentPagePath;
		}

		@SuppressWarnings("synthetic-access")
		public Builder withContent(String htmlContent) {
			this.page.content = htmlContent;
			return this;
		}

		public ContentPage build() {
			return page;
		}
	}
}
