package org.asciidocgenerator.ui.tags;

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
			jspWriter.print(createNodeTag(node));
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

	String createNodeTag(NavigationTreeNode node) {
		return String.format(	"<li id=\"%s\">%s</li>",
								node.getNavigationPath(),
								buildDescriptorForLayout(node, this::buildEmptyString));
	}

	String buildDescriptorForLayout(NavigationTreeNode node, Supplier<String> function) {
		return "<span>" + function.get() + buildLink(node) + "</span>";
	}

	String buildToggleItem() {
		return String.format(	"<span class=\"hoverable nottoggled toggleIcon\" onclick=\"%s\" ></span>",
								this.toggleFunction);
	}

	String buildEmptyString() {
		return "";
	}

	String buildLink(NavigationTreeNode node) {
		if (node.articlesAttached()) {
			return String.format(	"<a class=\"hoverable\" href=\"%s\" >%s</a>",
									this.contextPath + navigationPrefix + node.getNavigationPath(),
									node.getName());
		}
		return String.format("<span class=\"noArticlesAttached\" >%s</span>", node.getName());

	}
}
