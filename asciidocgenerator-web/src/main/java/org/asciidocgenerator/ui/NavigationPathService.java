package org.asciidocgenerator.ui;

import java.util.List;
import java.util.Optional;

import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.Trail;
import org.asciidocgenerator.DokuGeneratorException.ErrorCode;

public class NavigationPathService {

	private final NavigationPathPrefix pathPrefix;
	private final String groupId;
	private String navigation;

	public NavigationPathService(Trail relativePath) {
		super();
		List<String> fragments = relativePath.getFragments();
		if (fragments.size() < 2) {
			throw new DokuGeneratorException(ErrorCode.MAIN_NAVIGATION_NOT_DEFINED);
		}

		this.pathPrefix = NavigationPathPrefix.valueOf(fragments.get(0).toUpperCase());
		this.groupId = fragments.get(1);
		if (fragments.size() > 2) {
			this.navigation = fragments.get(2);
		}
	}

	public NavigationPathPrefix getPathPrefix() {
		return pathPrefix;
	}

	public String getGroupId() {
		return groupId;
	}

	public Optional<String> getNavigation() {
		return Optional.ofNullable(navigation);
	}

	public NavigationSelectedEvent createNavigationSelectedEvent() {
		switch (pathPrefix) {
			case ADMIN:
				return new AdminNavigationSelectedEvent(getGroupId(), getNavigation().get());
			default:
				if(getNavigation().isPresent()){
					return new MainNavigationSelectedEvent(getPathPrefix(), getGroupId(), getNavigation().get());
				}
				return new GroupSelectedEvent(getPathPrefix(), getGroupId());
		}
	}

	@Override
	public String toString() {
		return "NavigationPathService [pathPrefix="	+ pathPrefix
				+ ", groupId="
				+ groupId
				+ ", navigation="
				+ navigation
				+ "]";
	}
}
