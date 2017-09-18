package org.asciidocgenerator.ui.admin;

import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.navigation.DocumentNavigationService;
import org.asciidocgenerator.domain.navigation.NavigationGroup;

@RequestScoped
@Named
@Logged
public class GroupsController {

	@Inject
	private DocumentNavigationService service;
	
	public SortedSet<NavigationGroup> getGroups() {
		return service.getNavigationGroups();
	}

	public void updateGroups(@Observes ManipulateSettingCommand command) {
		if (command.getSetting().equals("groups")) {
			Map<String, String[]> values = command.getValues();
			values.entrySet().stream().map(UINavigationGroup::new).forEach(service::updateNavigationGroup);
		}
	}

	static class UINavigationGroup
			implements NavigationGroup {

		private final String id;
		private String name;

		public UINavigationGroup(Map.Entry<String, String[]> requestMapEntry) {
			this.id = requestMapEntry.getKey();
			String[] value = requestMapEntry.getValue();
			this.name = this.id;
			if (value.length > 0 && value[0].trim().length() > 0) {
				this.name = value[0];
			}
		}


		@Override
		public int compareTo(NavigationGroup o) {
			if (o.equals(this)) {
				return 0;
			}
			return getName().compareTo(o.getName());
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UINavigationGroup other = (UINavigationGroup) obj;
			return Objects.equals(other.getId(), getId());
		}

		@Override
		public String toString() {
			return "UINavigationGroup [id=" + id + ", name=" + name + "]";
		}

	}
}
