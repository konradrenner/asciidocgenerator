package org.asciidocgenerator.ui.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.asciidocgenerator.ui.controller.PageTrail;
import org.junit.Test;

public class PageTrailTest {

	@Test(expected = IllegalArgumentException.class)
	public void test_null_instanciation() {
		new PageTrail(null);
		fail("NULL-Instanz darf nicht existieren!");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_single_space_instanciation() {
		new PageTrail(" ");
		fail("Pagetrail darf nicht nur Spaces enthalten!");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_multiple_spaces_instanciation() {
		new PageTrail("    ");
		fail("Pagetrail darf nicht nur Spaces enthalten!");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_emptry_instanciation() {
		new PageTrail("");
		fail("Pagetrail darf nicht leer sein!");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_with_html_ending1() {
		new PageTrail("/Test/testing.html");
		fail("Pagetrail darf nicht mit / beginnen");
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_with_html_ending2() {
		new PageTrail(" /Test/testing.html");
		fail("Pagetrail darf nicht mit / beginnen");
	}

	@Test
	public void test_with_html_ending() {
		List<String> fragments = new ArrayList<>();
		fragments.add("Test");
		fragments.add("testing.html");

		PageTrail trail = new PageTrail("Test/testing.html");
		assertEquals("Fullpath", trail.getFullPath(), "Test/testing.html");
		assertEquals("Fragments", trail.getFragments(), fragments);
	}
}
