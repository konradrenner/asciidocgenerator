package org.asciidocgenerator.domain.navigation;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class NavigationPath
		implements Serializable {

	private static final long serialVersionUID = 1L;

	private String groupId;
	private String mainNavigation;
	private String navigationPath;

	public NavigationPath(String groupId, String mainNavigation, String navigationPath) {
		super();
		this.groupId = groupId;
		this.mainNavigation = mainNavigation;
		this.navigationPath = navigationPath;
	}

	public NavigationPath() {
		// Tool
	}

	public String getMainNavigationName() {
		return mainNavigation;
	}

	public String getNavigationPath() {
		return navigationPath;
	}

	public String getGroupId() {
		return groupId;
	}


	@Override
	public String toString() {
		return "NavigationPath [groupId="	+ groupId
				+ ", mainNavigation="
				+ mainNavigation
				+ ", navigationPath="
				+ navigationPath
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(groupId, mainNavigation, navigationPath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavigationPath other = (NavigationPath) obj;

		return Objects.equals(groupId, other.getGroupId())
					&& Objects.equals(mainNavigation, other.getMainNavigationName())
				&& Objects.equals(navigationPath, other.getNavigationPath());
	}

}
