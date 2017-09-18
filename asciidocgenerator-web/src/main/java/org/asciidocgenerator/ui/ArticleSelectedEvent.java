package org.asciidocgenerator.ui;

import java.util.Objects;

public class ArticleSelectedEvent
		implements NavigationSelectedEvent {

	private NavigationPathPrefix pathPrefix;
	private String groupName;
	private String navigationPath;
	private String titleName;

	public ArticleSelectedEvent(NavigationPathPrefix pathPrefix,
								String groupName,
								String navigationPath,
								String titleName) {
		super();
		this.groupName = groupName;
		this.pathPrefix = pathPrefix;
		this.navigationPath = navigationPath;
		this.titleName = titleName;
	}

	public String getNavigationPath() {
		return navigationPath;
	}

	public String getTitleName() {
		return titleName;
	}

	@Override
	public NavigationPathPrefix getPathPrefix() {
		return pathPrefix;
	}

	@Override
	public String getGroup() {
		return groupName;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.pathPrefix);
        hash = 47 * hash + Objects.hashCode(this.groupName);
        hash = 47 * hash + Objects.hashCode(this.navigationPath);
        hash = 47 * hash + Objects.hashCode(this.titleName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArticleSelectedEvent other = (ArticleSelectedEvent) obj;
        if (!Objects.equals(this.groupName, other.groupName)) {
            return false;
        }
        if (!Objects.equals(this.navigationPath, other.navigationPath)) {
            return false;
        }
        if (!Objects.equals(this.titleName, other.titleName)) {
            return false;
        }
        if (this.pathPrefix != other.pathPrefix) {
            return false;
        }
        return true;
    }


	@Override
	public String toString() {
		return "ArticleSelectedEvent [pathPrefix="	+ pathPrefix
				+ ", groupName="
				+ groupName
				+ ", navigationPath="
				+ navigationPath
				+ ", titleName="
				+ titleName
				+ "]";
	}

}
