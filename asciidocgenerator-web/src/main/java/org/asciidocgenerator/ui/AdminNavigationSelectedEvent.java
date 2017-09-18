package org.asciidocgenerator.ui;


public class AdminNavigationSelectedEvent
		implements NavigationSelectedEvent {

	private String adminPage;
	private String groupName;


	public AdminNavigationSelectedEvent(String groupName, String adminPage) {
		super();
		this.adminPage = adminPage;
		this.groupName = groupName;
	}

	public String getAdminPage() {
		return adminPage;
	}

	@Override
	public NavigationPathPrefix getPathPrefix() {
		return NavigationPathPrefix.ADMIN;
	}

	@Override
	public String getGroup() {
		return groupName;
	}

	@Override
	public String toString() {
		return "AdminNavigationSelectedEvent [adminPage=" + adminPage + ", groupName=" + groupName + "]";
	}

}
