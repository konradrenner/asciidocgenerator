package org.asciidocgenerator.domain.navigation;


public interface NavigationFactory {

	NavigationTab createMainNavigationTab(String group, String name);

	NavigationTreeBuilder createSideNavigationTree();

}
