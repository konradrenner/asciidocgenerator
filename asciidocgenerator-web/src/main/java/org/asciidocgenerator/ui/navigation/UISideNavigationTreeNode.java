package org.asciidocgenerator.ui.navigation;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

import org.asciidocgenerator.domain.navigation.NavigationTreeNode;

public class UISideNavigationTreeNode
		implements NavigationTreeNode, Comparable<NavigationTreeNode> {

	private final String name;
	private final String path;
	private final NavigableSet<UISideNavigationTreeNode> childs;
	private final int layer;
	private final boolean articlesAttached;

	public UISideNavigationTreeNode(String name, String path, int layer, boolean articlesAttached) {
		super();
		this.name = name;
		this.path = path;
		this.childs = new TreeSet<>();
		this.layer = layer;
		this.articlesAttached = articlesAttached;
	}

	public int getLayer() {
		return layer;
	}

	@Override
	public boolean articlesAttached() {
		return articlesAttached;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getNavigationPath() {
		return this.path;
	}

	@Override
	public NavigableSet<? extends NavigationTreeNode> getChilds() {
		return Collections.unmodifiableNavigableSet(this.childs);
	}

	NavigableSet<UISideNavigationTreeNode> getChildsModifyable() {
		return this.childs;
	}

	@Override
	public String toString() {
		return "UISideNavigationTreeNode [name="	+ name
				+ ", path="
				+ path
				+ ", childs="
				+ childs
				+ ", layer="
				+ layer
				+ ", articlesAvailable="
				+ articlesAttached
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(path);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UISideNavigationTreeNode other = (UISideNavigationTreeNode) obj;
		return Objects.equals(this.path, other.getNavigationPath());
	}

	public int compareTo(NavigationTreeNode o) {
		if (this.equals(o)) {
			return 0;
		}
		return this.path.compareTo(o.getNavigationPath());
	}


}
