package org.asciidocgenerator.ui.navigation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.asciidocgenerator.domain.navigation.AdminNavigationService;
import org.asciidocgenerator.domain.navigation.DocumentNavigationService;
import org.asciidocgenerator.domain.navigation.NavigationTab;
import org.asciidocgenerator.domain.navigation.NavigationTree;
import org.asciidocgenerator.ui.MainNavigationSelectedEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SideNavigationServiceTest {

	@Mock
	private NavigationTab navigationTab;

	@Mock
	private DocumentNavigationService service;

	@Mock
	private AdminNavigationService adminNavService;

	@Mock
	private NavigationTree navigationTree;

	@Mock
	private MainNavigationSelectedEvent event;

	private SideNavigationService underTest;

	private UINavigationFactory uiNav = new UINavigationFactory();

	@Before
	public void setUp() {
		underTest = new SideNavigationService(navigationTab, service, uiNav);
	}

	@Test
	public void noNavigationTree() {
		underTest = new SideNavigationService();
		NavigationTree result = underTest.getNavigation();

		assertEquals(new UISideNavigationTree().toString(), result.toString());
	}

	@Test
	public void withDocumentNavigationTree() {
		when(service.getSideNavigation(uiNav,
										navigationTab)).thenReturn(navigationTree);
		NavigationTree result = underTest.getNavigation();

		assertEquals(navigationTree, result);
		verify(service).getSideNavigation(uiNav, navigationTab);
		verify(adminNavService, times(0)).getSideNavigation(uiNav, navigationTab.getGroup());
	}

	@Test
	public void withAdminNavigationTree() {
		underTest = new SideNavigationService(navigationTab, adminNavService, uiNav);
		when(adminNavService.getSideNavigation(uiNav, navigationTab.getGroup())).thenReturn(navigationTree);
		NavigationTree result = underTest.getNavigation();

		assertEquals(navigationTree, result);
		verify(service, times(0)).getSideNavigation(uiNav, navigationTab);
		verify(adminNavService).getSideNavigation(uiNav, navigationTab.getGroup());
	}

	@Test
	public void navigationsTabSelected() {
		when(event.getNavigationTab()).thenReturn("Test");
		when(event.getGroup()).thenReturn("Group");

		underTest.navigationsTabSelected(event);

		assertEquals(	new UIMainNavigationTab(event.getGroup(), event.getNavigationTab()).toString(),
						underTest.getCurrenTabSelection().toString());

	}

	@Test
	public void navigationsTabNotSelected() {
		when(event.getNavigationTab()).thenReturn(null);

		underTest.navigationsTabSelected(event);

		assertEquals(null, underTest.getCurrenTabSelection());

	}
}

