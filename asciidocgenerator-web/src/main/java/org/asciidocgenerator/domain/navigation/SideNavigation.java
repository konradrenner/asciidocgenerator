package org.asciidocgenerator.domain.navigation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.asciidocgenerator.Trail;

@Entity
@Table(name = "SEITENNAVIGATION")
public class SideNavigation
		implements Serializable, Trail, Comparable<SideNavigation> {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({	@AttributeOverride(name = "groupId", column = @Column(name = "gruppeId")),
							@AttributeOverride(name = "mainNavigation", column = @Column(name = "hauptnavigationname")),
							@AttributeOverride(name = "navigationPath", column = @Column(name = "navigationspfad")) })
	private NavigationPath key;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumns({	@JoinColumn(name = "gruppeId",
								referencedColumnName = "gruppeId",
								insertable = false,
								updatable = false),
					@JoinColumn(name = "hauptnavigationname",
								referencedColumnName = "hauptnavigationname",
								insertable = false,
								updatable = false) })
	private MainNavigation mainNavigation;

	@Transient
	private List<String> pathFragments;

	SideNavigation(NavigationPath key) {
		super();
		this.key = key;
		init();
	}

	public SideNavigation() {
		// Tool
	}

	@PostLoad
	private void init() {
		String[] splitted = key.getNavigationPath().split(getSeparator());
		pathFragments = Arrays.asList(splitted);
	}

	public MainNavigation getMainNavigation() {
		return mainNavigation;
	}

	@Override
	public String getFullPath() {
		return key.getNavigationPath();
	}

	@Override
	public List<String> getFragments() {
		return pathFragments;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key.getGroupId(), key.getMainNavigationName(), key.getNavigationPath());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SideNavigation other = (SideNavigation) obj;

		return Objects.equals(key, other.key);
	}

	@Override
	public String toString() {
		return "SideNavigation [groupId="	+ key.getGroupId()
				+ ", mainNavigation="
				+ key.getMainNavigationName()
				+ ", navigationPath="
				+ key.getNavigationPath()
				+ ", pathFragments="
				+ pathFragments
				+ "]";
	}

	@Override
	public int compareTo(SideNavigation o) {
		if (this.equals(o)) {
			return 0;
		}
		return key.getNavigationPath().compareTo(o.key.getNavigationPath());
	}
}
