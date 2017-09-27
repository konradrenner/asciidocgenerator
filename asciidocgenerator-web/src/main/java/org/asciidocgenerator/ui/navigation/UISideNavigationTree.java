package org.asciidocgenerator.ui.navigation;

import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.DokuGeneratorException.ErrorCode;
import org.asciidocgenerator.Trail;
import org.asciidocgenerator.domain.navigation.NavigationTree;
import org.asciidocgenerator.domain.navigation.NavigationTreeBuilder;
import org.asciidocgenerator.domain.navigation.NavigationTreeNode;

public class UISideNavigationTree
		implements NavigationTree, NavigationTreeBuilder {

	// Ebene 1+2 ist immer die Gruppe + Hauptnavigationsreiter (die Ebenen sind 1 basiert (die Ebene entspricht der
	// Anzahl der Pfad
	// Fragmente))
	private static final int FIRST_SIDENAVIGATIONLAYER = 3;

	private final NavigableSet<UISideNavigationTreeNode> nodes;

	public UISideNavigationTree() {
		this.nodes = new TreeSet<>();
	}

	@Override
	public NavigableSet<? extends NavigationTreeNode> getNodes() {
		return Collections.unmodifiableNavigableSet(nodes);
	}

	@Override
	public void addPath(Trail path, boolean contentAttached) {
		List<String> getFragments = path.getFragments();
		addPath(path, getPathName(getFragments), contentAttached);
	}

	@Override
	public void addPath(Trail path, String name, boolean contentAttached) {
		List<String> getFragments = path.getFragments();
		UISideNavigationTreeNode node = new UISideNavigationTreeNode(	name,
																		path.getFullPath(),
																		getFragments.size(),
																		contentAttached);
		addPathRecursive(path, FIRST_SIDENAVIGATIONLAYER, nodes, node, contentAttached);
	}

	void addPathRecursive(	final Trail pfad,
							final int currentLayer,
							final NavigableSet<UISideNavigationTreeNode> nodeOfLayer,
							final UISideNavigationTreeNode newNode,
							boolean articlesExisting) {
		if (currentLayer == newNode.getLayer()) {
			nodeOfLayer.add(newNode);
		} else {
			NavigableSet<UISideNavigationTreeNode> nodeOfLayerBeneath = lookForNodeInChildSectionBeam(	pfad,
																										currentLayer,
																										nodeOfLayer,
																										articlesExisting);
			addPathRecursive(pfad, currentLayer + 1, nodeOfLayerBeneath, newNode, articlesExisting);
		}
	}

	// SucheNachKnotenInKindTeilbaum
	private NavigableSet<UISideNavigationTreeNode> lookForNodeInChildSectionBeam(	Trail path,
																					int currentLayer,
																					NavigableSet<UISideNavigationTreeNode> nodeOfLayer,
																					boolean articlesAttached) {
		String pathForSearch = buildNavigationPathForSearch(currentLayer, path);
		UISideNavigationTreeNode wantedNode = new UISideNavigationTreeNode(	path.getFragments().get(currentLayer),
																			pathForSearch,
																			currentLayer,
																			articlesAttached);
		UISideNavigationTreeNode node = nodeOfLayer.ceiling(wantedNode);
		return node.getChildsModifyable();
	}

	String buildNavigationPathForSearch(int layer, Trail path) {
		List<String> pathFragments = path.getFragments();
		if (pathFragments.isEmpty() || pathFragments.size() == 1) {
			throw new DokuGeneratorException(ErrorCode.SIDE_NAVIGATION_NOT_VALID);
		}

		StringBuilder sb = new StringBuilder(pathFragments.get(0));
		for (int i = 1; i < layer; i++) {
			sb.append(path.getSeparator());
			sb.append(pathFragments.get(i));
		}
		return sb.toString();
	}

	List<String> removeMainNavigationName(List<String> fragments) {
		return fragments.subList(1, fragments.size());
	}

	private String getPathName(List<String> pathFragments) {
		return pathFragments.get(pathFragments.size() - 1);
	}

	@Override
	public NavigationTree build() {
		return this;
	}

	@Override
	public String toString() {
		return "UISideNavigationTree [nodes=" + nodes + "]";
	}

}
