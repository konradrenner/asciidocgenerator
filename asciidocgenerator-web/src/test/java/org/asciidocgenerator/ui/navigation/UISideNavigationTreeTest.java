package org.asciidocgenerator.ui.navigation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.asciidocgenerator.Trail;
import org.asciidocgenerator.domain.navigation.NavigationTreeNode;
import org.asciidocgenerator.ui.navigation.UISideNavigationTree;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UISideNavigationTreeTest {

	private final String TRENNZEICHEN = "/";

	List<Trail> testdaten;

	UISideNavigationTree underTest;

	@Before
	public void setUp() {
		testdaten = new ArrayList<>();
		underTest = new UISideNavigationTree();
		initTestdaten(testdaten);
	}

	@Test
	public void testeBaumInitialisierung() {
		for (Trail pfad : testdaten) {
			underTest.addPath(pfad, true);
		}

		List<? extends NavigationTreeNode> nodes = new ArrayList<>(underTest.getNodes());
		assertEquals(3, nodes.size());
		assertEquals("Druck", nodes.get(0).getName());
		assertEquals("HeadForm", nodes.get(1).getName());
		assertEquals("Navigation", nodes.get(2).getName());

		List<? extends NavigationTreeNode> childs = new ArrayList<>(nodes.get(0).getChilds());
		assertEquals(0, childs.size());

		childs = new ArrayList<>(nodes.get(1).getChilds());
		assertEquals(2, childs.size());
		assertEquals("Legacy", childs.get(0).getName());
		assertEquals("Neu", childs.get(1).getName());

		childs = new ArrayList<>(nodes.get(2).getChilds());
		assertEquals(0, childs.size());
	}

	@Test
	public void testNavigationspfadBuildEbene1() {
		final int ersteEbeneMitSeitennavigation = 3;
		Trail pfad = mock(Trail.class);
		when(pfad.getSeparator()).thenReturn(TRENNZEICHEN);
		String navipfad = "Group/SWOPT/Headform";
		when(pfad.getFullPath()).thenReturn(navipfad);
		when(pfad.getFragments()).thenReturn(Arrays.asList(navipfad.split(TRENNZEICHEN)));

		String buildNavigationsPfadForSearch = underTest.buildNavigationPathForSearch(	ersteEbeneMitSeitennavigation,
																						pfad);

		assertEquals("Group/SWOPT/Headform", buildNavigationsPfadForSearch);
	}


	private void initTestdaten(List<Trail> td) {
		Trail pfad = mock(Trail.class);
		when(pfad.getSeparator()).thenReturn(TRENNZEICHEN);
		String navipfad = "Group/SWOPT/HeadForm";
		when(pfad.getFullPath()).thenReturn(navipfad);
		when(pfad.getFragments()).thenReturn(Arrays.asList(navipfad.split(TRENNZEICHEN)));
		td.add(pfad);

		pfad = mock(Trail.class);
		when(pfad.getSeparator()).thenReturn(TRENNZEICHEN);
		navipfad = "Group/SWOPT/HeadForm/Legacy";
		when(pfad.getFullPath()).thenReturn(navipfad);
		when(pfad.getFragments()).thenReturn(Arrays.asList(navipfad.split(TRENNZEICHEN)));
		td.add(pfad);

		pfad = mock(Trail.class);
		when(pfad.getSeparator()).thenReturn(TRENNZEICHEN);
		navipfad = "Group/SWOPT/HeadForm/Neu";
		when(pfad.getFullPath()).thenReturn(navipfad);
		when(pfad.getFragments()).thenReturn(Arrays.asList(navipfad.split(TRENNZEICHEN)));
		td.add(pfad);

		pfad = mock(Trail.class);
		when(pfad.getSeparator()).thenReturn(TRENNZEICHEN);
		navipfad = "Group/SWOPT/Druck";
		when(pfad.getFullPath()).thenReturn(navipfad);
		when(pfad.getFragments()).thenReturn(Arrays.asList(navipfad.split(TRENNZEICHEN)));
		td.add(pfad);

		pfad = mock(Trail.class);
		when(pfad.getSeparator()).thenReturn(TRENNZEICHEN);
		navipfad = "Group/SWOPT/Navigation";
		when(pfad.getFullPath()).thenReturn(navipfad);
		when(pfad.getFragments()).thenReturn(Arrays.asList(navipfad.split(TRENNZEICHEN)));
		td.add(pfad);

	}
}
