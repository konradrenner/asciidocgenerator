package org.asciidocgenerator.domain.navigation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.asciidocgenerator.Trail;

@Entity
@Table(name = "HAUPTNAVIGATION")
@NamedQuery(name = "Hauptnavigation.findAll",
			query = "select navi from MainNavigation navi where navi.group.id = :groupId order by navi.mainNavigationName")
@IdClass(MainNavigationKey.class)
public class MainNavigation
		implements Serializable, Comparable<MainNavigation> {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "gruppeId", referencedColumnName = "id")
	private Group group;

	@Id
	@Column(name = "hauptnavigationname")
	private String mainNavigationName;

	@Column
	private int sortierung;

	@OneToMany(mappedBy = "mainNavigation", cascade = ALL)
	private Set<SideNavigation> sideNavigation;

	@Transient
	private Trail groupMainNavigationTrail;

	@Transient
	private Map<String, SideNavigation> sideNavigationPathCache;

	@Transient
	private Set<SideNavigation> markedForDeletion;

	public MainNavigation() {
		// Tool
	}

	public MainNavigation(Group group, String mainNavigationName) {
		super();
		this.group = group;
		this.mainNavigationName = mainNavigationName;
		this.sideNavigation = new TreeSet<>();
		init();
	}

	@PostLoad
	private void init() {
		groupMainNavigationTrail = new InternalTrail(group.getId(), mainNavigationName);
	}

	static final class InternalTrail
			implements Trail, Serializable {

		private final String fullPath;

		InternalTrail(String groupId, String name) {
			fullPath = groupId + getSeparator() + name;
		}

		@Override
		public String getFullPath() {
			return fullPath;
		}

		@Override
		public List<String> getFragments() {
			return Arrays.asList(fullPath.split(getSeparator()));
		}

		@Override
		public String toString() {
			return "InternalTrail{" + "fullPath=" + fullPath + '}';
		}

	}

	public String getMainNavigationName() {
		return mainNavigationName;
	}

	public NavigationGroup getGroup() {
		return group;
	}

	public int getSortierung() {
		return sortierung;
	}

	public void setSortierung(int sortierung) {
		this.sortierung = sortierung;
	}

	public SortedSet<SideNavigation> getSidenavigation() {
		return new TreeSet<>(sideNavigation);
	}

	public Trail getMainNavigationTrail() {
		return groupMainNavigationTrail;
	}

	public void markSidenavigationForDeletion(String navigationPath) {
		initDeletionCaches();
		SideNavigation navigation = this.sideNavigationPathCache.get(navigationPath);
		this.markedForDeletion.add(navigation);
	}

	void deletedMarkedNavigations(Consumer<Object> deleteFunction) {
		if (allSideNavigationsMarkedForDeletion()) {
			// Delete MainNavigation if all of the mapped SideNavigations are marked for deletion
			deleteFunction.accept(this);
		} else {
			this.markedForDeletion.stream().forEach(deleteFunction);
		}
	}

	private boolean allSideNavigationsMarkedForDeletion() {
		return this.markedForDeletion.size() == this.sideNavigation.size();
	}

	private void initDeletionCaches() {
		if (this.sideNavigationPathCache == null) {
			this.sideNavigationPathCache = new HashMap<>();
			this.sideNavigation.stream().forEach(nav -> {
				this.sideNavigationPathCache.put(nav.getFullPath(), nav);
			});
		}
		if (this.markedForDeletion == null) {
			this.markedForDeletion = new LinkedHashSet<>();
		}
	}

	/**
	 * Creates and adds a new SideNavigation to the MainNavigation. If the given navigationPath does not start with the
	 * mainNavigationName, nothing will be created and an empty Set is returned.
	 * 
	 * @param navigationPath
	 * @return Set<SideNavigation> of added Sidenavigation
	 */
	Set<SideNavigation> addSidenavigation(String navigationPath) {
		if (navigationPath.startsWith(getMainNavigationTrail().getFullPath())) {
			Set<SideNavigation> createdSides = new LinkedHashSet<>();
			streamNavigationPathCharacteristics(navigationPath)	.map(this::newSidenavigation)
																.filter(newSideNavigtion -> (!sideNavigation.contains(newSideNavigtion)))
																.peek(createdSides::add)
																.forEach(this::add);
			return createdSides;
		}
		return Collections.emptySet();
	}

	SideNavigation newSidenavigation(String path) {
		return new SideNavigation(new NavigationPath(group.getId(), mainNavigationName, path));
	}

	void add(SideNavigation navigationPath) {
		this.sideNavigation.add(navigationPath);
	}

	Stream<String> streamNavigationPathCharacteristics(String path) {
		final String separator = getMainNavigationTrail().getSeparator();
		String[] fragments = path.split(separator);
		if (justContainsNavigationPrefix(fragments)) {
			return Stream.empty();
		}

		List<String> characteristics = new ArrayList<>(fragments.length);
		StringBuilder currentCharacteristic = new StringBuilder(fragments[0]).append(separator).append(fragments[1]);
		for (int i = 2; i < fragments.length; i++) {
			currentCharacteristic.append(separator).append(fragments[i]);
			characteristics.add(currentCharacteristic.toString());
		}
		return characteristics.stream();
	}

	// Navigation Prefix is group and mainnavigation name
	private boolean justContainsNavigationPrefix(String[] fragments) {
		return fragments.length < 3;
	}

	@Override
	public int hashCode() {
		return Objects.hash(group.getId(), mainNavigationName);
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
		MainNavigation other = (MainNavigation) obj;
		return Objects.equals(mainNavigationName, other.getMainNavigationName())
				&& Objects.equals(group, other.getGroup());
	}

	@Override
	public String toString() {
		return "MainNavigation [group="	+ group
				+ ", mainNavigationName="
				+ mainNavigationName
				+ ", sortierung="
				+ sortierung
				+ ", sideNavigation="
				+ sideNavigation
				+ "]";
	}

	@Override
	public int compareTo(MainNavigation o) {
		if (this.equals(o)) {
			return 0;
		}

		int ret = this.sortierung - o.sortierung;
		if (ret == 0) {
			ret = this.mainNavigationName.compareTo(o.mainNavigationName);
		}
		return ret;
	}

}
