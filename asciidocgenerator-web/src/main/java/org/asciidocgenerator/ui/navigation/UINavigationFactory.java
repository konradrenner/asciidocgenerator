package org.asciidocgenerator.ui.navigation;

import org.asciidocgenerator.domain.navigation.NavigationFactory;
import org.asciidocgenerator.domain.navigation.NavigationTab;
import org.asciidocgenerator.domain.navigation.NavigationTreeBuilder;

public class UINavigationFactory
		implements NavigationFactory {

	@Override
	public NavigationTab createMainNavigationTab(String group, String name) {
		return new UIMainNavigationTab(group, name);
	}

	@Override
	public NavigationTreeBuilder createSideNavigationTree() {
		return new UISideNavigationTree();
	}

}
