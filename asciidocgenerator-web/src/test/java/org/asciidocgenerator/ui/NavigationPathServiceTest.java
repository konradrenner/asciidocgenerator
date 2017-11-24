package org.asciidocgenerator.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.Trail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NavigationPathServiceTest {

	@Mock
	private Trail trail;

	private NavigationPathService underTest;

	private URLEncoderService encoderService;

	@Before
	public void setUp() {
		encoderService = new URLEncoderService();
	}

	@Test(expected = DokuGeneratorException.class)
	public void trailTooShort() {
		when(trail.getFragments()).thenReturn(Collections.emptyList());

		underTest = new NavigationPathService(trail);
	}

	@Test
	public void all3ComponentsInTrail() {
		List<String> fragments = Arrays.asList("navigation", "group", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		assertEquals(NavigationPathPrefix.NAVIGATION, underTest.getPathPrefix());
		assertEquals("group", underTest.getGroupId());
		assertEquals("test", underTest.getNavigation().get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void notExistingNavigationPrefix() {
		List<String> fragments = Arrays.asList("hallo", "group", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);
	}

	@Test
	public void noNavigationComponentInTrail() {
		List<String> fragments = Arrays.asList("navigation", "group");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		assertEquals(NavigationPathPrefix.NAVIGATION, underTest.getPathPrefix());
		assertEquals("group", underTest.getGroupId());
		assertFalse(underTest.getNavigation().isPresent());
	}

	@Test
	public void adminEvent() {
		List<String> fragments = Arrays.asList("admin", "group", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		AdminNavigationSelectedEvent event = (AdminNavigationSelectedEvent) underTest.createNavigationSelectedEvent(encoderService::decode);

		assertEquals(NavigationPathPrefix.ADMIN, event.getPathPrefix());
		assertEquals("group", event.getGroup());
		assertEquals("test", event.getAdminPage());
	}

	@Test
	public void mainnavigationEvent() {
		List<String> fragments = Arrays.asList("article", "group", "test");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		MainNavigationSelectedEvent event = (MainNavigationSelectedEvent) underTest.createNavigationSelectedEvent(encoderService::decode);

		assertEquals(NavigationPathPrefix.ARTICLE, event.getPathPrefix());
		assertEquals("group", event.getGroup());
		assertEquals("test", event.getNavigationTab());
	}

	@Test
	public void groupEvent() {
		List<String> fragments = Arrays.asList("navigation", "group");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		GroupSelectedEvent event = (GroupSelectedEvent) underTest.createNavigationSelectedEvent(encoderService::decode);

		assertEquals(NavigationPathPrefix.NAVIGATION, event.getPathPrefix());
		assertEquals("group", event.getGroup());
	}

	@Test
	public void mainnavigationEventEncoded() {
		List<String> fragments = Arrays.asList("article", "grou%20p", "tes%20t");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		MainNavigationSelectedEvent event = (MainNavigationSelectedEvent) underTest.createNavigationSelectedEvent(encoderService::decode);

		assertEquals(NavigationPathPrefix.ARTICLE, event.getPathPrefix());
		assertEquals("grou p", event.getGroup());
		assertEquals("tes t", event.getNavigationTab());
	}

	@Test
	public void groupEventEncoded() {
		List<String> fragments = Arrays.asList("navigation", "grou%20p");
		when(trail.getFragments()).thenReturn(fragments);

		underTest = new NavigationPathService(trail);

		GroupSelectedEvent event = (GroupSelectedEvent) underTest.createNavigationSelectedEvent(encoderService::decode);

		assertEquals(NavigationPathPrefix.NAVIGATION, event.getPathPrefix());
		assertEquals("grou p", event.getGroup());
	}
}
