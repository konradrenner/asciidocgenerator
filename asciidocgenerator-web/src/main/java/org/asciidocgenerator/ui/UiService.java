package org.asciidocgenerator.ui;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.asciidocgenerator.domain.navigation.DocumentNavigationService;
import org.asciidocgenerator.domain.navigation.NavigationGroup;

@Named("UIService")
@RequestScoped
public class UiService {

	@Resource(lookup = "java:global/defaultGroupName")
	private String defaultGroupName;

	@Inject
	private DocumentNavigationService service;

	private String groupName;
	private String groupId;
	private String pathPrefix;
	private String titleName;

	@PostConstruct
	public void init() {
		this.groupName = defaultGroupName;
		this.groupId = defaultGroupName;
		this.pathPrefix = NavigationPathPrefix.NAVIGATION.toString();
		this.titleName = defaultGroupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getTitleName() {
		return titleName;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	void updateGroupId(@Observes NavigationSelectedEvent event) {
		NavigationGroup navigationGroup = service.getNavigationGroup(event.getGroup());
		this.groupName = navigationGroup.getName();
		this.groupId = navigationGroup.getId();
		this.pathPrefix = event.getPathPrefix().toString();
		this.titleName = navigationGroup.getName();
	}

	void updateTitle(@Observes ArticleSelectedEvent event) {
		this.titleName = event.getTitleName();
	}
}