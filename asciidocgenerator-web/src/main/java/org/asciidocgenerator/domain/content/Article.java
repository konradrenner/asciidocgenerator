package org.asciidocgenerator.domain.content;

import static javax.persistence.CascadeType.REFRESH;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.asciidocgenerator.domain.MetaInformation;
import org.asciidocgenerator.domain.RenderedDocument;

@Entity
@Table(name = "BEITRAG")
@NamedQueries({ @NamedQuery(name = "Article.findByNavigationPath",
							query = "select a from Article a where a.key.navigationspfad = :navipath"),
				@NamedQuery(name = "Article.findSideNavigationPathsFromArticles",
							query = "select distinct b.key.navigationspfad from Article b where b.key.navigationspfad like :pathstart"),
				@NamedQuery(name = "Article.findArticleByNavigationKey",
							query = "select b from Article b where b.navigationKey = :navigationKey"),
				@NamedQuery(name = "Article.findAll", query = "select b from Article b ") })
public class Article
		implements Serializable, RenderedDocument {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ArticleKey key;

	@Column
	@NotNull
	private String title;

	@Column
	@NotNull
	private String navigationKey;

	@Embedded
	private MetaInformation metainformation;

	@Column(name = "renderzeitpunkt")
	private Timestamp letzerRenderzeitpunkt;

	@ManyToMany(cascade = REFRESH)
	@JoinTable(	name = "BEITRAGSKATEGORIE",
				joinColumns = {	@JoinColumn(name = "navigationspfad", referencedColumnName = "navigationspfad"),
								@JoinColumn(name = "ablagepfad", referencedColumnName = "ablagepfad") },
				inverseJoinColumns = { @JoinColumn(name = "kategoriename", referencedColumnName = "kategoriename") })
	private Set<Categorie> kategorien;

	@Transient
	private String group;

	@Transient
	private String mainNavigation;


	public Article() {
		super();
	}

	public Article(	ArticleKey key,
					String navigationKey,
					String group,
					String mainNavigtion,
					String title,
					MetaInformation metainformation,
					Timestamp letzerRenderzeitpunkt) {
		super();
		this.navigationKey = navigationKey;
		this.key = key;
		this.metainformation = metainformation;
		this.letzerRenderzeitpunkt = copy(letzerRenderzeitpunkt);
		this.title = title;
		this.group = group;
		this.mainNavigation = mainNavigtion;
		this.kategorien = new HashSet<>();
	}


	@Override
	public String getNavigationKey() {
		return navigationKey;
	}

	@Override
	public String getGroup() {
		if (group == null) {
			String navipath = key.getNavigationspfad();
			group = navipath.substring(0, navipath.indexOf('/'));
		}
		return group;
	}

	@Override
	public String getMainNavigation() {
		if (mainNavigation == null) {
			String navipath = key.getNavigationspfad();
			int afterFirstSeparator = navipath.indexOf('/') + 1;
			mainNavigation = navipath.substring(afterFirstSeparator, navipath.indexOf('/', afterFirstSeparator));
		}
		return mainNavigation;
	}

	@Override
	public String getNavigationPath() {
		return key.getNavigationspfad();
	}

	@Override
	public String getStorageLocation() {
		return key.getAblagepfad();
	}

	@Override
	public MetaInformation getMetaInformation() {
		return metainformation;
	}

	@Override
	public Timestamp getRenderTimestamp() {
		if (letzerRenderzeitpunkt != null) {
			return copy(letzerRenderzeitpunkt);
		}
		return null;
	}

	@Override
	public Set<Categorie> getCategories() {
		return Collections.unmodifiableSet(kategorien);
	}

	void setTitle(String value) {
		this.title = value;
	}

	void setMetainformation(MetaInformation info) {
		this.metainformation = info;
	}

	void addCategories(Set<Categorie> categories) {
		this.kategorien.addAll(categories);
	}

	void clearCategories() {
		this.kategorien.clear();
	}

	public String getTitle() {
		return title;
	}

	public String getFilename() {
		final String ablagepfad = key.getAblagepfad();
		final int lastIndexOfSeperator = ablagepfad.lastIndexOf(File.separator);
		return ablagepfad.substring(lastIndexOfSeperator + 1);
	}

	public ArticleKey getKey() {
		return key;
	}

	public MetaInformation getMetainformation() {
		return metainformation;
	}

	private static Timestamp copy(Timestamp ts) {
		Timestamp newone = new Timestamp(ts.getTime());
		newone.setNanos(ts.getNanos());
		return newone;
	}

	@Override
	public String toString() {
		return "Article [key="	+ key
				+ ", title="
				+ title
				+ ", navigationKey="
				+ navigationKey
				+ ", metainformation="
				+ metainformation
				+ ", letzerRenderzeitpunkt="
				+ letzerRenderzeitpunkt
				+ ", kategorien="
				+ kategorien
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		return Objects.equals(key, other.getKey());
	}
}
