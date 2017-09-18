package org.asciidocgenerator.domain.navigation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.asciidocgenerator.DocumentsRenderedEvent;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.RenderedDocument;
import org.asciidocgenerator.domain.content.ContentRepository;

@RequestScoped
@Logged
public class DocumentNavigationService {

	@Inject
	private NavigationRepository repository;

	@Inject
	private ContentRepository contentRepository;

	@Resource(lookup = "java:global/defaultGroupName")
	private String defaultGroupName;

	public DocumentNavigationService() {

	}

	// for testing
	public DocumentNavigationService(ContentRepository repo, NavigationRepository nav) {
		contentRepository = repo;
		repository = nav;
	}

	public NavigationGroup getNavigationGroup(String groupId) {
		return repository.loadNavigationGroup(groupId).orElse(new Group(defaultGroupName, defaultGroupName));
	}

	public SortedSet<NavigationTab> getMainNavigationTabs(String groupId, NavigationFactory factory) {
		List<MainNavigation> hauptnavigation = repository.loadMainNavigation(groupId);
		TreeSet<NavigationTab> reiter = new TreeSet<>();

		for (MainNavigation navi : hauptnavigation) {
			reiter.add(factory.createMainNavigationTab(groupId, navi.getMainNavigationName()));
		}

		return reiter;
	}

	public NavigationTree getSideNavigation(NavigationFactory factory, NavigationTab tab) {
		MainNavigation mainNavigation = repository	.loadMainNavigationEntry(tab.getGroup(), tab.getName())
													.orElseThrow(NoSuchElementException::new);

		Set<SideNavigation> seitennavigation = mainNavigation.getSidenavigation();
		NavigationTreeBuilder baumBuilder = factory.createSideNavigationTree();

		Set<String> sideNavigationPathsWithArticles =
													contentRepository.findSideNavigationPathsFromArticles(	tab.getGroup(),
																											tab.getName());

		for (SideNavigation snavi : seitennavigation) {
			baumBuilder.addPath(snavi, sideNavigationPathsWithArticles.contains(snavi.getFullPath()));
		}

		return baumBuilder.build();
	}

	public SortedSet<NavigationGroup> getNavigationGroups() {
		return repository.loadNavigationGroups();
	}

	public void updateNavigationGroup(NavigationGroup group) {
		Group dbGroup = repository.loadNavigationGroup(group.getId()).get();
		dbGroup.setName(group.getName());
		repository.updateGroup(dbGroup);
	}

	public void handleRenderedDocuments(@Observes DocumentsRenderedEvent<? extends RenderedDocument> event) {
		Set<? extends RenderedDocument> documents = event.getRenderedDocuments();
		documents.stream().forEach(this::applyChanges);
	}

	void applyChanges(RenderedDocument event) {
		Optional<MainNavigation> hauptnavigationsPunkt = repository.loadMainNavigationEntry(event.getGroup(),
																							event.getMainNavigation());
		
		if (hauptnavigationsPunkt.isPresent()) {
			MainNavigation mainNavigation = hauptnavigationsPunkt.get();
			mainNavigation.addSidenavigation(event.getNavigationPath());
			repository.updateNavigation(mainNavigation);
		} else {
			Group group = repository.loadNavigationGroup(event.getGroup())
									.orElse(new Group(event.getGroup(), event.getGroup()));
			MainNavigation mainNavigation = new MainNavigation(group, event.getMainNavigation());
			mainNavigation.addSidenavigation(event.getNavigationPath());
			repository.insertNavigation(mainNavigation);
		}
	}
}