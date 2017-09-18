package org.asciidocgenerator.domain.content;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.asciidocgenerator.domain.Keyword;

@Entity
@Table(name = "KATEGORIE")
@NamedQuery(name = "Categorie.findAll",
			query = "select cat from Categorie cat order by CAT.kategoriename")
public class Categorie
		implements Serializable, Keyword {

	private static final long serialVersionUID = 1L;

	@Id
	private String kategoriename;

	@ManyToMany(mappedBy = "kategorien")
	private Set<Article> beitraege;

	public Categorie(String name) {
		super();
		this.kategoriename = name;
	}

	public Categorie(Keyword keyword) {
		super();
		this.kategoriename = keyword.getName();
	}

	public Categorie() {
		// Tool
	}

	public Set<Article> getArticles() {
		return Collections.unmodifiableSet(beitraege);
	}

	@Override
	public String getName() {
		return kategoriename;
	}

	@Override
	public String toString() {
		return "Categorie [name=" + kategoriename + ", articles=" + beitraege + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(kategoriename);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categorie other = (Categorie) obj;
		return Objects.equals(kategoriename, other.getName());
	}

}
