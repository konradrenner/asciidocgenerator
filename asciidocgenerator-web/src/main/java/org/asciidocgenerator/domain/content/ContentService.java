package org.asciidocgenerator.domain.content;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.asciidocgenerator.DocumentsRenderedEvent;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.Trail;

@Stateless
@Logged
public class ContentService {

	@Inject
	private ContentRepository repository;

	@Inject
	private CategorieRepository categorieRepository;

	public ContentService() {
		// ejb
	}

	// for testing
	ContentService(ContentRepository repo, CategorieRepository cat) {
		repository = repo;
		categorieRepository = cat;
	}

	public List<Article> getArticles(Trail trail) {
		return repository.findArticles(trail);
	}

	public Article getArticleByNavigationKey(String key) {
		return repository.findByNavigationKey(key);
	}

	public void handleRenderedDocuments(@Observes DocumentsRenderedEvent<Article> event) {
		Set<Article> documents = event.getRenderedDocuments();
		documents.stream().forEach(this::applyChanges);
	}

	void applyChanges(Article article) {
		Optional<Article> repoArticle = repository.findArticle(article.getKey());
		storeCategories(article.getCategories());
		if (repoArticle.isPresent()) {
			Article oldArticle = repoArticle.get();
			modifyArticle(article, oldArticle);
			repository.update(oldArticle);
		} else {
			repository.insert(article);
		}
	}

	void modifyArticle(Article newOne, Article oldOne) {
		oldOne.clearCategories();
		oldOne.addCategories(newOne.getCategories());
		oldOne.setTitle(newOne.getTitle());
		oldOne.setMetainformation(newOne.getMetainformation());
	}

	void storeCategories(Set<Categorie> categories) {
		categories	.stream()
					.filter(categorie -> !categorieRepository.findCateorie(categorie.getName()).isPresent())
					.forEach(categorieRepository::insert);
	}
}
