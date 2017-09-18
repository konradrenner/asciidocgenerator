package org.asciidocgenerator.domain.navigation;

import org.asciidocgenerator.Trail;

public interface NavigationTreeBuilder {

	void addPath(Trail path, boolean contentAttached);

	void addPath(Trail path, String name, boolean contentAttached);

	NavigationTree build();
}
