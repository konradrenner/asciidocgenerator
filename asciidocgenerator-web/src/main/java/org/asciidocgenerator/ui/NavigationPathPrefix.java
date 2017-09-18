package org.asciidocgenerator.ui;

public enum NavigationPathPrefix {
									NAVIGATION,
									ARTICLE,
									CATEGORY,
									CATEGORIES,
									ADMIN;

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
