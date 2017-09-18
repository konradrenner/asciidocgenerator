package org.asciidocgenerator.domain.navigation;


public interface NavigationTab
		extends Comparable<NavigationTab> {

	String getName();

	String getGroup();
}
