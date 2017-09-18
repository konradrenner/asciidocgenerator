package org.asciidocgenerator.ui.navigation;

import java.util.Objects;

import org.asciidocgenerator.domain.navigation.NavigationTab;

public class UIMainNavigationTab
		implements NavigationTab {

	private final String group;
	private final String name;

	public UIMainNavigationTab(String group, String name) {
		super();
		this.name = name;
		this.group = group;
	}

	@Override
	public int compareTo(NavigationTab o) {
		if (name.equals(o.getName()))
			return 0;
		return name.compareTo(o.getName());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return "UIMainNavigationTab [group=" + group + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		UIMainNavigationTab other = (UIMainNavigationTab) obj;

		return Objects.equals(other.getGroup(), group) && Objects.equals(other.getName(), name);
	}

}
