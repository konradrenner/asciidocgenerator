package org.asciidocgenerator.ui;


public class GroupSelectedEvent
		implements NavigationSelectedEvent {

	private NavigationPathPrefix pathPrefix;
	private String groupName;

	public GroupSelectedEvent(NavigationPathPrefix pathPrefix, String groupName) {
		super();
		this.groupName = groupName;
		this.pathPrefix = pathPrefix;
	}

	@Override
	public NavigationPathPrefix getPathPrefix() {
		return pathPrefix;
	}

	@Override
	public String getGroup() {
		return groupName;
	}

	@Override
	public String toString() {
		return "GroupSelectedEvent [pathPrefix=" + pathPrefix + ", groupName=" + groupName + "]";
	}


}
