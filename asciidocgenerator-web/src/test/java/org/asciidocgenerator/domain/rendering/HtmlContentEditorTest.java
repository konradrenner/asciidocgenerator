package org.asciidocgenerator.domain.rendering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.asciidocgenerator.domain.rendering.HtmlContentEditor;
import org.junit.Before;
import org.junit.Test;


public class HtmlContentEditorTest {

	private HtmlContentEditor underTest;

	@Before
	public void setUp() {
		underTest = new HtmlContentEditor();
	}

	@Test
	public void emptyGivenList() {
		List<String> testdata = Collections.emptyList();

		List<String> content = underTest.editedContent(testdata);

		assertTrue(content.isEmpty());
	}

	public void containsNoNavigationLine() {
		List<String> testdata = Arrays.asList("asdfsf", "asdfsdf");

		List<String> content = underTest.editedContent(testdata);

		assertEquals(testdata, content);
	}

	public void containsNavigationLine() {
		List<String> testdata = Arrays.asList("asdfsf", ":navigation:", "sdfsdf");

		List<String> content = underTest.editedContent(testdata);

		List<String> expected = Arrays.asList(	"asdfsf",
												":navigation:",
												":data-uri:" + System.lineSeparator(),
												"sdfsdf");
		assertEquals(expected, content);
	}
}
