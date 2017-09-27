package org.asciidocgenerator.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class MetaInformation
		implements Serializable {

	@NotNull
	@Column
	private String vcsversion;
	@NotNull
	@Column
	private String projektname;
	@NotNull
	@Column
	private String repositoryname;
	@NotNull
	@Column
	private String vcsurl;

	@SuppressWarnings("synthetic-access")
	public static WithProjektname newInstance() {
		return new Builder();
	}

	public MetaInformation() {
		super();
	}

	MetaInformation(String vcsversion, String projektname, String repositoryname, String vcsurl) {
		super();
		this.vcsversion = vcsversion;
		this.projektname = projektname;
		this.repositoryname = repositoryname;
		this.vcsurl = vcsurl;
	}

	public String getVcsversion() {
		return vcsversion;
	}

	public String getProjektname() {
		return projektname;
	}

	public String getRepositoryname() {
		return repositoryname;
	}

	public String getVcsurl() {
		return vcsurl;
	}

	public interface WithVcsversion {

		WithVcsurl vcsversion(String value);
	}

	public interface WithProjektname {

		WithRepositoryname projektname(String value);
	}

	public interface WithRepositoryname {

		WithVcsversion repositoryname(String value);
	}

	public interface WithVcsurl {

		Finish vcsurl(String value);
	}

	public interface Finish {

		MetaInformation build();
	}

	public static final class Builder
			implements WithProjektname, WithRepositoryname, WithVcsurl, WithVcsversion, Finish {

		private String vcsversion;
		private String projektname;
		private String repositoryname;
		private String vcsurl;

		private Builder() {
			// nothing
		}

		@Override
		public MetaInformation build() {
			return new MetaInformation(vcsversion, projektname, repositoryname, vcsurl);
		}

		@Override
		public WithVcsurl vcsversion(String value) {
			this.vcsversion = value;
			return this;
		}

		@Override
		public Finish vcsurl(String value) {
			this.vcsurl = value;
			return this;
		}

		@Override
		public WithVcsversion repositoryname(String value) {
			this.repositoryname = value;
			return this;
		}

		@Override
		public WithRepositoryname projektname(String value) {
			this.projektname = value;
			return this;
		}

	}

	@Override
	public String toString() {
		return "BeitragMetainformation [vcsversion="	+ vcsversion
				+ ", projektname="
				+ projektname
				+ ", repositoryname="
				+ repositoryname
				+ ", vcsurl="
				+ vcsurl
				+ "]";
	}

}
