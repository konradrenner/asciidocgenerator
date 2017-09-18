package org.asciidocgenerator;

import java.util.List;

public interface Trail {

	String getFullPath();

	List<String> getFragments();

	default String getSeparator() {
		return "/";
	}
}
