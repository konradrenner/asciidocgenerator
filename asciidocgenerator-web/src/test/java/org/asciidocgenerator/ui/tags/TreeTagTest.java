package org.asciidocgenerator.ui.tags;

import org.asciidocgenerator.domain.navigation.NavigationTree;
import org.asciidocgenerator.domain.navigation.NavigationTreeNode;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TreeTagTest {

	private static final String TOGGLE = "toggle(this)";
	private static final String NAME = "Node Name";
	private static final String PATH = "abc/def";
	private static final String CTXPATH = "/asciidoctorgenerator-web";
	private static final String PREFIX = "/article/Test";

	@Mock
	private NavigationTree tree;

	private TreeTag underTest;

	@Before
	public void setUp() {
		underTest = new TreeTag();
		underTest.setContextPath(CTXPATH);
		underTest.setToggleFunction(TOGGLE);
		underTest.setNavigationPrefix(PREFIX);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testBuildingLinkWhenArticleAttached() {
		NavigationTreeNode node = mock(NavigationTreeNode.class);
		when(node.getNavigationPath()).thenReturn(PATH);
		when(node.getName()).thenReturn(NAME);
		when(node.articlesAttached()).thenReturn(true);

		String expected = String.format("<a class=\"hoverable\" href=\"%s%s%s\" >%s</a>", CTXPATH, PREFIX, PATH, NAME);
		String actual = underTest.buildLink(node);
		assertEquals(expected, actual);

		verify(node).articlesAttached();
	}

	@SuppressWarnings("boxing")
	@Test
	public void testBuildingLinkWhenNoArticleAttached() {
		NavigationTreeNode node = mock(NavigationTreeNode.class);
		when(node.getName()).thenReturn(NAME);
		when(node.articlesAttached()).thenReturn(false);

		String expected = String.format("<span class=\"noArticlesAttached\" >%s</span>", NAME);
		assertEquals(expected, underTest.buildLink(node));
	}

	@Test
	public void testBuildinOfPlaceholderForLayout() {
		String expected = "<span class=\"hiddenSpan\">+</span>";
		assertEquals(expected, underTest.buildPlaceholderForLayout());
	}

	@Test
	public void testCreationOfFolderStartTagWhenHidden() {
		NavigationTreeNode treeNode = mock(NavigationTreeNode.class);
		when(treeNode.getNavigationPath()).thenReturn(PATH);
		when(treeNode.getName()).thenReturn(NAME);
	}

	@Test
	public void testCreationOfFolderEndTag() {
		String expected = "</ul></li>";
		String actual = underTest.createFolderEndTag();
		assertEquals(expected, actual);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testBuildDescriptorForLayoutWhenFolder() {
		NavigationTreeNode node = mock(NavigationTreeNode.class);
		when(node.getNavigationPath()).thenReturn(PATH);
		when(node.getName()).thenReturn(NAME);
		when(node.articlesAttached()).thenReturn(false);

		String function = underTest.buildToggleItem();
		String link = underTest.buildLink(node);
		String expected = String.format("<span>%s%s</span>", function, link);
		String actual = underTest.buildDescriptorForLayout(node, underTest::buildToggleItem);

		assertEquals(expected, actual);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testBuildDescriptorForLayoutWhenArticle() {
		NavigationTreeNode node = mock(NavigationTreeNode.class);
		when(node.getNavigationPath()).thenReturn(PATH);
		when(node.getName()).thenReturn(NAME);
		when(node.articlesAttached()).thenReturn(false);

		String function = underTest.buildPlaceholderForLayout();
		String link = underTest.buildLink(node);
		String expected = String.format("<span>%s%s</span>", function, link);
		String actual = underTest.buildDescriptorForLayout(node, underTest::buildPlaceholderForLayout);

		assertEquals(expected, actual);
	}

	@Test
	public void testBuildPlaceholderForLayout() {
		assertEquals(buildPlaceholderForLayout(), underTest.buildPlaceholderForLayout());
	}

	@Test
	public void testBuildingToggleItem() {
		String actual = underTest.buildToggleItem();
		assertEquals(buildToggleItem(), actual);
	}

	String buildToggleItem() {
		return "<span class=\"hoverable nottoggled toggleIcon\" onclick=\"toggle(this)\" ></span>";
	}

	String buildPlaceholderForLayout() {
		return "<span class=\"hiddenSpan\">+</span>";
	}
}
