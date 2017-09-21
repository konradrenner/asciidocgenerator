package org.asciidocgenerator.domain.rendering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.asciidocgenerator.BaseDirectoryService;
import org.asciidocgenerator.FilesDownloadedEvent;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.Keyword;
import org.asciidocgenerator.domain.MetaInformation;
import org.asciidocgenerator.domain.RenderedDocumentRegister;
import org.asciidoctor.AsciiDocDirectoryWalker;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.DirectoryWalker;

@Stateless
@Logged
public class HtmlRenderServiceBean {

	private final static Logger LOG = Logger.getLogger("asciidocgenerator-web");

	@Inject
	private BaseDirectoryService baseDirectoryService;

	@Inject
	private RenderedDocumentRegister documentRegister;

	@Inject
	private Asciidoctor asciidoc;

	private HtmlContentEditor contentEditor;
	private MetaInformation metaInformation;
	private String baseProjectPath;

	@Asynchronous
	public void render(@Observes FilesDownloadedEvent downloadedEvent) throws IOException {

		baseProjectPath =
						Paths.get(baseDirectoryService.getBaseDirectory(), downloadedEvent.getProjectName()).toString();
		DirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(baseProjectPath);
		List<File> asciidocFiles = directoryWalker.scan();
		contentEditor = new HtmlContentEditor();
		metaInformation = MetaInformation	.newInstance()
											.projektname(downloadedEvent.getProjectName())
											.repositoryname(downloadedEvent.getRepositoryName())
											.vcsversion(downloadedEvent.getVcsversion())
											.vcsurl(downloadedEvent.getVcsurl())
											.build();
		LOG.log(Level.INFO, "Got new files for rendering: {0}", downloadedEvent);
		LOG.log(Level.INFO, "baseProjectPath: {0}", baseProjectPath);
		for (File file : asciidocFiles) {
			generateHtml(file);
		}

		documentRegister.publishRegisteredDocuments();
	}

	void generateHtml(File file) throws IOException {

		AsciidocDocument doku = new AsciidocDocument(file.toPath(), asciidoc).loadDocumentHeader().loadContent();
		String navigation = doku.getNavigation();

		if (navigation != null) {

			String fileName = file.getName();
			String newFileName = fileName.replaceAll(".adoc", ".html");
			Path newFilePath = Paths.get(file.getParent(), newFileName);

			doku.newRender().setContentEditor(contentEditor).enableContentEditing().convertToHtml(newFilePath);

			String[] navigationFragments = navigation.split("/");
			String title = doku.getMainDocumentTitle();
			String group = navigationFragments[0];
			String mainNavigation = navigationFragments[1];
			String navigationPath = String.join("/", navigationFragments);
			String words = doku.getKeywords();

			Keyword[] keywords;
			if (words == null) {
				keywords = new Keyword[0];
			} else {
				keywords = Arrays	.stream(words.split(", +"))
									.map(documentRegister::createKeyword)
									.toArray(Keyword[]::new);
			}

			documentRegister.newRenderedDocument()
							.title(title)
							.group(group)
							.mainNavigation(mainNavigation)
							.navigationPath(navigationPath)
							.navigationKey(UUID.randomUUID().toString())
							.storageLocation(newFilePath.toString())
							.metainformation(metaInformation)
							.addKeywords(keywords)
							.build();
		}
	}

}