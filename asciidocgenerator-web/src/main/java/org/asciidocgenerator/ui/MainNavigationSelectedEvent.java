package org.asciidocgenerator.ui;


public class MainNavigationSelectedEvent
		implements NavigationSelectedEvent {

	private NavigationPathPrefix pathPrefix;
	private String navigationTab;
	private String groupName;


	public MainNavigationSelectedEvent(NavigationPathPrefix pathPrefix, String groupName, String navigationTab) {
		super();
		this.navigationTab = navigationTab;
		this.groupName = groupName;
		this.pathPrefix = pathPrefix;
	}

	@Override
	public NavigationPathPrefix getPathPrefix() {
		return pathPrefix;
	}

	public String getNavigationTab() {
		return navigationTab;
	}

	@Override
	public String getGroup() {
		return groupName;
	}


	@Override
	public String toString() {
		return "MainNavigationSelectedEvent [pathPrefix="	+ pathPrefix
				+ ", navigationTab="
				+ navigationTab
				+ ", groupName="
				+ groupName
				+ "]";
	}


}
