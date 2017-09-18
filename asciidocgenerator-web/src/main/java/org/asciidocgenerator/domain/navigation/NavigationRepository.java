package org.asciidocgenerator.domain.navigation;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asciidocgenerator.Logged;

@Stateless
@Logged
public class NavigationRepository {

	@PersistenceContext(unitName = "asciidoctorgenerator-web")
	private EntityManager entityManager;

	public Optional<MainNavigation> loadMainNavigationEntry(String groupId, String name) {
		MainNavigation mainNavigation = entityManager.find(MainNavigation.class, new MainNavigationKey(groupId, name));
		return Optional.ofNullable(mainNavigation);
	}

	public List<MainNavigation> loadMainNavigation(String groupId) {
		return entityManager.createNamedQuery("Hauptnavigation.findAll", MainNavigation.class)
							.setParameter("groupId", groupId)
							.getResultList();
	}

	public void insertNavigation(MainNavigation navi) {
		entityManager.persist(navi);
	}

	public void updateNavigation(MainNavigation navi) {
		entityManager.merge(navi);
	}

	public void deleteMarkedNavigations(MainNavigation navi) {
		navi.deletedMarkedNavigations(entityManager::remove);
	}

	public void insertGroup(Group group) {
		entityManager.persist(group);
	}

	public void updateGroup(Group group) {
		entityManager.merge(group);
	}

	public void deleteGroupsWithoutMainNavigations() {
		entityManager	.createNativeQuery("delete from GRUPPE g where not exists (select 1 from HAUPTNAVIGATION h where h.gruppeId = g.id)")
						.executeUpdate();
	}

	public SortedSet<NavigationGroup> loadNavigationGroups() {
		List<Group> resultList = entityManager.createNamedQuery("Group.findAll", Group.class)
														.getResultList();
		return new TreeSet<>(resultList);
	}

	public Optional<Group> loadNavigationGroup(String id) {
		return Optional.ofNullable(entityManager.find(Group.class, id));
	}
}
