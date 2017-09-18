package org.asciidocgenerator.domain.content;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.Trail;

@Stateless
@Logged
public class ContentRepository {

	@PersistenceContext(unitName = "asciidoctorgenerator-web")
	private EntityManager entityManager;

	public List<Article> findArticles(Trail trail) {
		return entityManager.createNamedQuery("Article.findByNavigationPath", Article.class)
							.setParameter("navipath", trail.getFullPath())
							.getResultList();
	}

	public Article findByNavigationKey(String key) {
		return entityManager.createNamedQuery("Article.findArticleByNavigationKey", Article.class)
							.setParameter("navigationKey", key)
							.getSingleResult();
	}

	public List<Article> findAllArticles() {
		return entityManager.createNamedQuery("Article.findAll", Article.class).getResultList();
	}

	public Set<String> findSideNavigationPathsFromArticles(String group, String mainNavigation) {
		List<String> resultList = entityManager
												.createNamedQuery(	"Article.findSideNavigationPathsFromArticles",
																	String.class)
												.setParameter("pathstart", group + "_" + mainNavigation + "%")
										.getResultList();

		return new HashSet<>(resultList);
	}

	public Optional<Article> findArticle(ArticleKey key) {
		return Optional.ofNullable(entityManager.find(Article.class, key));
	}

	public void update(Article article) {
		entityManager.merge(article);
	}

	public void insert(Article article) {
		entityManager.persist(article);
	}

	public void delete(Article article) {
		entityManager.remove(article);
	}
}
