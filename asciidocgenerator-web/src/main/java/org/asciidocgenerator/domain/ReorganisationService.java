package org.asciidocgenerator.domain;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.content.Article;
import org.asciidocgenerator.domain.content.ContentRepository;
import org.asciidocgenerator.domain.navigation.MainNavigation;
import org.asciidocgenerator.domain.navigation.NavigationRepository;

@Singleton
@Logged
public class ReorganisationService {

	private Logger LOG = Logger.getLogger("asciidocgenerator-web");

	@Inject
	private ContentRepository contentRepo;

	@Inject
	private NavigationRepository navigationRepo;

	@Schedule(hour = "00", minute = "10", timezone = "Europe/Vienna", persistent = false)
	public void removeNonExistingData() {
		Set<Article> articlesWithNotExistingFiles = evaluateArticlesWithNotExistingFiles();

		LOG.log(Level.INFO, "Articles without files:{0}", articlesWithNotExistingFiles);

		Set<MainNavigation> mainNavsForUpdate = deleteArticles(articlesWithNotExistingFiles);
		LOG.log(Level.INFO, "MainNavigations for update:{0}", mainNavsForUpdate);
		updateMainNavigations(mainNavsForUpdate);
		deleteGroups();
	}

	Set<Article> evaluateArticlesWithNotExistingFiles() {
		List<Article> allArticles = contentRepo.findAllArticles();

		LinkedHashSet<Article> ret = new LinkedHashSet<>();
		allArticles	.stream()
					.filter(article -> (Files.notExists(Paths.get(article.getStorageLocation()))))
					.forEach(ret::add);
		return ret;
	}

	Set<MainNavigation> deleteArticles(Set<Article> articles) {
		final Set<MainNavigation> mainNavsForUpdate = new HashSet<>();

		articles.stream().forEach(article -> {
			deleteArticle(article);
			mainNavsForUpdate.add(removeSideNavigation(article));
		});

		return mainNavsForUpdate;
	}

	void deleteArticle(Article article) {
		contentRepo.delete(article);
	}

	MainNavigation removeSideNavigation(Article article) {
		MainNavigation mainNavigation = navigationRepo	.loadMainNavigationEntry(	article.getGroup(),
																					article.getMainNavigation())
														.get();
		mainNavigation.markSidenavigationForDeletion(article.getNavigationPath());
		return mainNavigation;
	}

	void updateMainNavigations(Set<MainNavigation> mainNavs) {
		mainNavs.stream().forEach(this::updateMainNavigation);
	}

	void updateMainNavigation(MainNavigation mainNav) {
		navigationRepo.deleteMarkedNavigations(mainNav);
	}

	void deleteGroups() {
		navigationRepo.deleteGroupsWithoutMainNavigations();
	}
}
