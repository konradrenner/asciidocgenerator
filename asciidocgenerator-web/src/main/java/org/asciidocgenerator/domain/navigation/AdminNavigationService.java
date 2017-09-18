package org.asciidocgenerator.domain.navigation;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.Trail;

@RequestScoped
@Logged
public class AdminNavigationService {

	public NavigationTree getSideNavigation(NavigationFactory factory, String groupId) {

		ResourceBundle bundle = ResourceBundle.getBundle("org/asciidocgenerator/ui/messages/messages");

		NavigationTreeBuilder treeBuilder = factory.createSideNavigationTree();

		treeBuilder.addPath(new AdminNavigationTrail(groupId, "settings", "groups"),
							bundle.getString("label_groups"),
							true);

		treeBuilder.addPath(new AdminNavigationTrail(groupId, "settings", "reorg"),
							bundle.getString("label_reorg"),
							true);

		return treeBuilder.build();
	}

	static class AdminNavigationTrail
			implements Trail {

		private final List<String> fragments;

		public AdminNavigationTrail(String... fragments) {
			this.fragments = Arrays.asList(fragments);
		}

		@Override
		public String getFullPath() {
			StringBuilder sb = new StringBuilder();
			fragments.stream().forEach(fragment -> {
				sb.append(fragment).append(getSeparator());
			});
			return sb.substring(0, sb.length() - 1);
		}

		@Override
		public List<String> getFragments() {
			return fragments;
		}

		@Override
		public String toString() {
			return "AdminNavigationTrail [fragments=" + fragments + "]";
		}

	}
}
