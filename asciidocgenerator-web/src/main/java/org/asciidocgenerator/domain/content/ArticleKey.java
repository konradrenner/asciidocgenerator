package org.asciidocgenerator.domain.content;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ArticleKey
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "navigationspfad")
	private String navigationspfad;

	@NotNull
	@Column(name = "ablagepfad")
	private String ablagepfad;

	public ArticleKey() {
		// Tool
	}

	ArticleKey(String navigationspfad, String ablagepfad) {
		super();
		this.navigationspfad = navigationspfad;
		this.ablagepfad = ablagepfad;
	}

	@SuppressWarnings("synthetic-access")
	public static WithNavigationspfad newInstance() {
		return new Builder();
	}

	public interface WithNavigationspfad {

		WithAblagepfad navigationspfad(String value);
	}

	public interface WithAblagepfad {

		Finish ablagepfad(String value);
	}

	public interface Finish {

		ArticleKey build();
	}

	public static final class Builder
			implements WithNavigationspfad, WithAblagepfad, Finish {

		private String navigationspfad;
		private String ablagepfad;

		private Builder() {
			// nothing
		}

		@Override
		public ArticleKey build() {
			return new ArticleKey(navigationspfad, ablagepfad);
		}

		@Override
		public Finish ablagepfad(String value) {
			this.ablagepfad = value;
			return this;
		}

		@Override
		public WithAblagepfad navigationspfad(String value) {
			this.navigationspfad = value;
			return this;
		}

	}

	public String getNavigationspfad() {
		return navigationspfad;
	}

	public String getAblagepfad() {
		return ablagepfad;
	}

	@Override
	public String toString() {
		return "BeitragKey [navigationspfad=" + navigationspfad + ", ablagepfad=" + ablagepfad + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(navigationspfad, ablagepfad);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticleKey other = (ArticleKey) obj;
		return Objects.equals(navigationspfad, other.getNavigationspfad())
				&& Objects.equals(ablagepfad, other.getAblagepfad());
	}

}
