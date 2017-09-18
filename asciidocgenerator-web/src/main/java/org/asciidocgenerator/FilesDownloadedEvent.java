package org.asciidocgenerator;

import java.nio.file.Path;

public class FilesDownloadedEvent {

	private final String repositoryName;
	private final String projectName;
	private final Path directory;
	private final String vcsurl;
	private final String vcsversion;

	public FilesDownloadedEvent(String repositoryName,
								String projectName,
								Path directory,
								String vcsurl,
								String vcsversion) {
		super();
		this.repositoryName = repositoryName;
		this.projectName = projectName;
		this.directory = directory;
		this.vcsurl = vcsurl;
		this.vcsversion = vcsversion;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getProjectName() {
		return projectName;
	}

	public Path getDirectory() {
		return directory;
	}

	public String getVcsurl() {
		return vcsurl;
	}

	public String getVcsversion() {
		return vcsversion;
	}

	@Override
	public String toString() {
		return "FilesDownloadedEvent [repositoryName="	+ repositoryName
				+ ", projectName="
				+ projectName
				+ ", directory="
				+ directory
				+ ", vcsurl="
				+ vcsurl
				+ ", vcsversion="
				+ vcsversion
				+ "]";
	}


}
