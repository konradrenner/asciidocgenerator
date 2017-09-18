package org.asciidocgenerator.domain.content;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asciidocgenerator.Logged;

@Stateless
@Logged
public class CategorieRepository {

	@PersistenceContext(unitName = "asciidoctorgenerator-web")
	private EntityManager entityManager;

	public List<Categorie> getAllCategories() {
		List<Categorie> resultList = entityManager	.createNamedQuery("Categorie.findAll", Categorie.class)
													.getResultList();

		// Ensure that Categories are always loaded from DB (maybe new Articles had been created in the meantime)
		resultList.stream().forEach(entityManager::refresh);
		return resultList;
	}

	public Optional<Categorie> findCateorie(String name) {
		return Optional.ofNullable(entityManager.find(Categorie.class, name));
	}

	public void insert(Categorie categorie) {
		entityManager.persist(categorie);
	}
}
