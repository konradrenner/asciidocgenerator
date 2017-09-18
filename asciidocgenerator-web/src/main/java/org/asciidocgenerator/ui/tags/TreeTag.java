package org.asciidocgenerator.ui.tags;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.function.Supplier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.asciidocgenerator.domain.navigation.NavigationTree;
import org.asciidocgenerator.domain.navigation.NavigationTreeNode;

public class TreeTag
		extends TagSupport {

	private static final long serialVersionUID = 1L;

	private NavigationTree tree;

	private String toggleFunction;

	private String contextPath;

	private String navigationPrefix;

	public void setNavigationPrefix(String navigationPrefix) {
		this.navigationPrefix = navigationPrefix;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void setToggleFunction(String toggleFunction) {
		this.toggleFunction = toggleFunction;
	}

	public void setTree(NavigationTree tree) {
		this.tree = tree;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter jspWriter = pageContext.getOut();
		try {
			iterateFolder(tree.getNodes(), jspWriter, true);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	void iterateFolder(	SortedSet<? extends NavigationTreeNode> nodes,
						JspWriter jspWriter,
						boolean childrenHidden) throws IOException {
		for (NavigationTreeNode node : nodes) {
			processNode(node, jspWriter, childrenHidden);
		}
	}

	void processNode(NavigationTreeNode node, JspWriter jspWriter, boolean hiddenNode) throws IOException {
		if (node.getChilds().isEmpty()) {
			jspWriter.print(createNodeTag(node, hiddenNode));
		} else {
			jspWriter.print(createFolderStartTag(node, hiddenNode));
			iterateFolder(node.getChilds(), jspWriter, true);
			jspWriter.print(createFolderEndTag());
		}
	}

	String createFolderStartTag(NavigationTreeNode node, boolean childrenHidden) {
		StringBuilder sb = new StringBuilder("<li id='");
		sb.append(node.getNavigationPath());
		sb.append("' >");
		sb.append(buildDescriptorForLayout(node, this::buildToggleItem));
		sb.append("<ul");
		if (childrenHidden) {
			sb.append(" class='hiddenElement'");
		}
		sb.append(">");
		return sb.toString();
	}

	String createFolderEndTag() {
		return "</ul></li>";
	}

	String createNodeTag(NavigationTreeNode node, boolean hidden) {
		StringBuilder sb = new StringBuilder("<li id='");
		sb.append(node.getNavigationPath());
		if (hidden) {
			// sb.append("' class='hiddenElement");
		}
		sb.append("' >");
		sb.append(buildDescriptorForLayout(node, this::buildPlaceholderForLayout));
		sb.append("</li>");
		return sb.toString();
	}

	String buildDescriptorForLayout(NavigationTreeNode node,
									Supplier<String> function) {
		return "<span>" + function.get() + buildLink(node) + "</span>";
	}

	String buildToggleItem() {
		return "<span class=\"hoverable nottoggled toggleIcon\" onclick=\""	+ buildJavascript()
				+ "\" >"
				+ "<img src=\""
				+ contextPath
				+ "/resources"
				+ File.separator
				+ "icons"
				+ File.separator
				+ "expand_more.png\" />"
				+ "</span>";
	}

	String buildPlaceholderForLayout() {
		return "<span class=\"hiddenSpan\">+</span>";
	}

	String buildJavascript() {
		StringBuilder sb = new StringBuilder(this.toggleFunction);
		String iconPath = contextPath + "/resources/icons/";
		int indexOfThis = this.toggleFunction.indexOf("this");
		if (indexOfThis > -1) {
			sb.insert(indexOfThis	+ 4,
						", '" + iconPath + "expand_less.png'" + ", '" + iconPath + "expand_more.png'");
		}
		return sb.toString();
	}

	String buildLink(NavigationTreeNode node) {
		StringBuilder sb;
		if (node.articlesAttached()) {
			sb = new StringBuilder("<a class=\"hoverable\" href=\"");
			sb.append(this.contextPath);
			sb.append(navigationPrefix);
			sb.append(node.getNavigationPath());
			sb.append("\" >");
			sb.append(node.getName());
			sb.append("</a>");
		} else {
			sb = new StringBuilder("<span class=\"noArticlesAttached\" >");
			sb.append(node.getName());
			sb.append("</span>");
		}

		return sb.toString();
	}
}
