package org.asciidocgenerator.domain.navigation;

import java.util.NavigableSet;

public interface NavigationTree {

	NavigableSet<? extends NavigationTreeNode> getNodes();
}
