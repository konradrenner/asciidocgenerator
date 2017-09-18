package org.asciidocgenerator.domain.navigation;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.asciidocgenerator.domain.navigation.Group;
import org.asciidocgenerator.domain.navigation.MainNavigation;
import org.asciidocgenerator.domain.navigation.NavigationPath;
import org.asciidocgenerator.domain.navigation.SideNavigation;
import org.junit.Before;
import org.junit.Test;


public class MainNavigationTest {

	private MainNavigation underTest;

	@Before
	public void setUp() {
		underTest = new MainNavigation(new Group("Group", "Group"), "Test");
	}

	@Test
	public void falscheHauptnavigation() {
		String pfad = "Hallo";

		Set<SideNavigation> sidenavigation = underTest.addSidenavigation(pfad);

		assertTrue(sidenavigation.isEmpty());
	}

	@Test
	public void nurHauptnavigationHinzufuegen() {
		String pfad = "Test";

		Set<SideNavigation> sidenavigation = underTest.addSidenavigation(pfad);

		assertTrue(sidenavigation.isEmpty());
		assertTrue(underTest.getSidenavigation().isEmpty());
	}

	@Test
	public void leererPfad() {
		String pfad = "";

		Set<SideNavigation> sidenavigation = underTest.addSidenavigation(pfad);

		assertTrue(sidenavigation.isEmpty());
	}

	@Test
	public void werteSchonVorhanden() {
		String pfad = "Group/Test/Hallo";
		SideNavigation side = new SideNavigation(new NavigationPath("Group", "Test", "Group/Test/Hallo"));
		underTest.add(side);

		Set<SideNavigation> sidenavigation = underTest.addSidenavigation(pfad);

		// Muss gleiche Instanz sein, ansonsten haette der Filter nicht gegriffen
		assertTrue(sidenavigation.isEmpty());
		assertTrue(underTest.getSidenavigation().size() == 1);
		assertTrue(underTest.getSidenavigation().iterator().next() == side);
	}

	@Test
	public void neueWerte() {
		String pfad = "Group/Test/Hallo";
		SideNavigation side = new SideNavigation(new NavigationPath("Group", "Test", pfad));
		underTest.add(side);
		String neuerpfad = "Group/Test/Hallo/Neue Werte/Sollen/Rein";
		
		Set<SideNavigation> sidenavigation = underTest.addSidenavigation(neuerpfad);

		assertTrue(sidenavigation.size() == 3);
		assertTrue(sidenavigation.contains(new SideNavigation(new NavigationPath("Group", "Test", "Group/Test/Hallo/Neue Werte"))));
		assertTrue(sidenavigation.contains(new SideNavigation(new NavigationPath(	"Group",
																					"Test",
																					"Group/Test/Hallo/Neue Werte/Sollen"))));
		assertTrue(sidenavigation.contains(new SideNavigation(new NavigationPath(	"Group",
																					"Test",
																					"Group/Test/Hallo/Neue Werte/Sollen/Rein"))));

		Set<SideNavigation> naviInUnderTest = underTest.getSidenavigation();
		assertTrue(naviInUnderTest.size() == 4);
		assertTrue(naviInUnderTest.contains(new SideNavigation(new NavigationPath(	"Group",
																					"Test",
																					"Group/Test/Hallo"))));
		assertTrue(naviInUnderTest.contains(new SideNavigation(new NavigationPath(	"Group",
																					"Test",
																					"Group/Test/Hallo/Neue Werte"))));
		assertTrue(naviInUnderTest.contains(new SideNavigation(new NavigationPath(	"Group",
																					"Test",
																					"Group/Test/Hallo/Neue Werte/Sollen"))));
		assertTrue(naviInUnderTest.contains(new SideNavigation(new NavigationPath(	"Group",
																					"Test",
																					"Group/Test/Hallo/Neue Werte/Sollen/Rein"))));
	}
}
