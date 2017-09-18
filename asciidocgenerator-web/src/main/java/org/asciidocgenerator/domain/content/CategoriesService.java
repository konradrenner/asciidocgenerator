package org.asciidocgenerator.domain.content;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asciidocgenerator.Logged;

@Stateless
@Logged
public class CategoriesService {

	@Inject
	private CategorieRepository repository;

	public List<Categorie> getCategories() {
		return repository.getAllCategories();
	}

	public Categorie getCategory(String name) {
		return repository.findCateorie(name).get();
	}
}
