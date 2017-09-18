package org.asciidocgenerator.ui.navigation;

import java.util.NoSuchElementException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.DokuGeneratorException.ErrorCode;
import org.asciidocgenerator.domain.navigation.AdminNavigationService;
import org.asciidocgenerator.domain.navigation.DocumentNavigationService;
import org.asciidocgenerator.domain.navigation.NavigationTab;
import org.asciidocgenerator.domain.navigation.NavigationTree;
import org.asciidocgenerator.ui.AdminNavigationSelectedEvent;
import org.asciidocgenerator.ui.GroupSelectedEvent;
import org.asciidocgenerator.ui.MainNavigationSelectedEvent;
import org.asciidocgenerator.ui.NavigationPathPrefix;


@Named("sideNavigationService")
@RequestScoped
@Logged
public class SideNavigationService {

	@Inject
	private DocumentNavigationService service;

	@Inject
	private AdminNavigationService adminNaviService;

	private NavigationTab currentTabSelection;
	private NavigationPathPrefix sideNavigationPrefix;
	private String groupId;

	private UINavigationFactory uiNavigationFactory = new UINavigationFactory();

	public SideNavigationService() {
	}

	//for testing
	SideNavigationService(	NavigationTab nav,
							DocumentNavigationService service,
							UINavigationFactory uiNav) {
		currentTabSelection = nav;
		if (service != null) {
			this.service = service;
		}
		uiNavigationFactory = uiNav;
		sideNavigationPrefix = NavigationPathPrefix.ARTICLE;
	}

	// for testing
	SideNavigationService(NavigationTab nav, AdminNavigationService service, UINavigationFactory uiNav) {
		currentTabSelection = nav;
		if (service != null) {
			this.adminNaviService = service;
		}
		uiNavigationFactory = uiNav;
		sideNavigationPrefix = NavigationPathPrefix.ADMIN;
	}

	// for testing
	NavigationTab getCurrenTabSelection() {
		return currentTabSelection;
	}

	public NavigationTree getNavigation() {
		if (sideNavigationPrefix == NavigationPathPrefix.ADMIN) {
			return adminNaviService.getSideNavigation(uiNavigationFactory, groupId);
		}

		if (currentTabSelection == null) {
			return new UISideNavigationTree();
		}

		NavigationTree sideNavigationTree = service.getSideNavigation(uiNavigationFactory,
																				currentTabSelection);
		return sideNavigationTree;
	}

	public String getSideNavigationPrefix() {
		return sideNavigationPrefix.toString();
	}

	public void navigationsTabSelected(@Observes MainNavigationSelectedEvent event) {
		String navigationTab = event.getNavigationTab();
		if (navigationTab == null) {
			currentTabSelection = null;
			return;
		}

		sideNavigationPrefix = NavigationPathPrefix.ARTICLE;

		currentTabSelection = new UIMainNavigationTab(event.getGroup(), event.getNavigationTab());
	}

	public void groupSelected(@Observes GroupSelectedEvent event) {
		try {
			NavigationTab first = service.getMainNavigationTabs(event.getGroup(), uiNavigationFactory).first();
			navigationsTabSelected(new MainNavigationSelectedEvent(	NavigationPathPrefix.ARTICLE,
																	event.getGroup(),
																	first.getName()));
		} catch (NoSuchElementException e) {
			throw new DokuGeneratorException(ErrorCode.MAIN_NAVIGATION_NOT_VALID, e);
		}
	}

	public void adminSelected(@Observes AdminNavigationSelectedEvent event) {
		sideNavigationPrefix = NavigationPathPrefix.ADMIN;
		groupId = event.getGroup();
	}
}
