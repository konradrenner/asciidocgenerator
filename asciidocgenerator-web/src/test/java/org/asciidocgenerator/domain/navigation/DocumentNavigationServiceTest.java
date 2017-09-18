package org.asciidocgenerator.domain.navigation;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.asciidocgenerator.domain.RenderedDocument;
import org.asciidocgenerator.domain.navigation.DocumentNavigationService;
import org.asciidocgenerator.domain.navigation.Group;
import org.asciidocgenerator.domain.navigation.MainNavigation;
import org.asciidocgenerator.domain.navigation.NavigationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DocumentNavigationServiceTest {

	private DocumentNavigationService underTest;

	@Mock
	private NavigationRepository repo;

	@Mock
	private RenderedDocument renderedDocument;

	@Mock
	private MainNavigation mainNavigation;

	private Group group;

	@Before
	public void setUp() {
		underTest = new DocumentNavigationService(null, repo);
	}

	@Test
	public void keinNavigationsPunkt() {
		when(renderedDocument.getGroup()).thenReturn("TestGroup");
		group = new Group(renderedDocument.getGroup(), renderedDocument.getGroup());
		when(repo.loadNavigationGroup(renderedDocument.getGroup())).thenReturn(Optional.ofNullable(group));
		when(renderedDocument.getMainNavigation()).thenReturn("Test");
		when(renderedDocument.getNavigationPath()).thenReturn("testing");
		when(repo.loadMainNavigationEntry(	renderedDocument.getGroup(),
											renderedDocument.getMainNavigation())).thenReturn(Optional.empty());

		underTest.applyChanges(renderedDocument);

		verify(repo).insertNavigation(new MainNavigation(	new Group(	renderedDocument.getGroup(),
																		renderedDocument.getGroup()),
															renderedDocument.getMainNavigation()));
	}

	@Test
	public void navigationsPunktVorhanden() {
		when(repo.loadNavigationGroup(renderedDocument.getGroup())).thenReturn(Optional.ofNullable(group));

		when(repo.loadMainNavigationEntry(	renderedDocument.getGroup(),
											renderedDocument.getMainNavigation())).thenReturn(Optional.ofNullable(mainNavigation));

		underTest.applyChanges(renderedDocument);

		verify(repo).updateNavigation(mainNavigation);
	}

}
