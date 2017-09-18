package org.asciidocgenerator.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.Trail;
import org.asciidocgenerator.ui.AdminNavigationSelectedEvent;
import org.asciidocgenerator.ui.GroupSelectedEvent;
import org.asciidocgenerator.ui.MainNavigationSelectedEvent;
import org.asciidocgenerator.ui.NavigationPathPrefix;
import org.asciidocgenerator.ui.NavigationPathService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NavigationPathServiceTest {

	@Mock
	private Trail trail;

	private NavigationPathService underTest;


	@Test(expected = DokuGeneratorException.class)
	public void trailTooShort() {
		when(trail.getFragments()).thenReturn(Collections.emptyList());

		underTest = new NavigationPathService(trail);
	}

	@Test
	public void all3ComponentsInTrail() {
		List<String> fragments = Arrays.asList("navigation", "ea210", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		assertEquals(NavigationPathPrefix.NAVIGATION, underTest.getPathPrefix());
		assertEquals("ea210", underTest.getGroupId());
		assertEquals("test", underTest.getNavigation().get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void notExistingNavigationPrefix() {
		List<String> fragments = Arrays.asList("hallo", "ea210", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);
	}

	@Test
	public void noNavigationComponentInTrail() {
		List<String> fragments = Arrays.asList("navigation", "ea210");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		assertEquals(NavigationPathPrefix.NAVIGATION, underTest.getPathPrefix());
		assertEquals("ea210", underTest.getGroupId());
		assertFalse(underTest.getNavigation().isPresent());
	}

	@Test
	public void adminEvent() {
		List<String> fragments = Arrays.asList("admin", "ea210", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		AdminNavigationSelectedEvent event = (AdminNavigationSelectedEvent) underTest.createNavigationSelectedEvent();

		assertEquals(NavigationPathPrefix.ADMIN, event.getPathPrefix());
		assertEquals("ea210", event.getGroup());
		assertEquals("test", event.getAdminPage());
	}

	@Test
	public void mainnavigationEvent() {
		List<String> fragments = Arrays.asList("article", "ea210", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		MainNavigationSelectedEvent event = (MainNavigationSelectedEvent) underTest.createNavigationSelectedEvent();

		assertEquals(NavigationPathPrefix.ARTICLE, event.getPathPrefix());
		assertEquals("ea210", event.getGroup());
		assertEquals("test", event.getNavigationTab());
	}

	@Test
	public void groupEvent() {
		List<String> fragments = Arrays.asList("navigation", "ea210");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		GroupSelectedEvent event = (GroupSelectedEvent) underTest.createNavigationSelectedEvent();

		assertEquals(NavigationPathPrefix.NAVIGATION, event.getPathPrefix());
		assertEquals("ea210", event.getGroup());
	}
}
