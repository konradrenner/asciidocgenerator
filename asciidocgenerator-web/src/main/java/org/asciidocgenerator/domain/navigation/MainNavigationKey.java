package org.asciidocgenerator.domain.navigation;

import java.io.Serializable;
import java.util.Objects;

public class MainNavigationKey
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private String group;
	private String mainNavigationName;

	public MainNavigationKey(String group, String mainNavigationName) {
		super();
		this.mainNavigationName = mainNavigationName;
		this.group = group;
	}

	public MainNavigationKey() {
		// Tool
	}

	public String getMainNavigationName() {
		return mainNavigationName;
	}

	public String getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return "SeitennavigationKey [mainNavigationName="	+ mainNavigationName
				+ ", group="
				+ group
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(mainNavigationName, group);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MainNavigationKey other = (MainNavigationKey) obj;

		return Objects.equals(mainNavigationName, other.getMainNavigationName())
				&& Objects.equals(group, other.getGroup());
	}

}
