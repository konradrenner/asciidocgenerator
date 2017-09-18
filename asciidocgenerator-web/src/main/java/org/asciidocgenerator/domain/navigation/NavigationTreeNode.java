package org.asciidocgenerator.domain.navigation;

import java.util.NavigableSet;

public interface NavigationTreeNode {

	String getName();

	String getNavigationPath();

	NavigableSet<? extends NavigationTreeNode> getChilds();

	boolean articlesAttached();
}
