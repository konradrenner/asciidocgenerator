package org.asciidocgenerator.ui.navigation;

import java.util.SortedSet;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.navigation.DocumentNavigationService;
import org.asciidocgenerator.domain.navigation.NavigationGroup;
import org.asciidocgenerator.domain.navigation.NavigationTab;
import org.asciidocgenerator.ui.UiService;

@Named("mainNavigationService")
@RequestScoped
@Logged
public class MainNavigationService {

	@Inject
	private DocumentNavigationService service;

	@Inject
	private UiService uiService;

	public SortedSet<NavigationTab> getDocumentNavigation() {
		return service.getMainNavigationTabs(uiService.getGroupId(), new UINavigationFactory());
	}
	
	public SortedSet<NavigationGroup> getNavigationGroups() {
		return service.getNavigationGroups();
	}
}
