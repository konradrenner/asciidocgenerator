package org.asciidocgenerator.domain.rendering;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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

	public HtmlRenderServiceBean() {
	}

	// For Tests
	HtmlRenderServiceBean(BaseDirectoryService baseDirectoryService) {
		this.baseDirectoryService = baseDirectoryService;
	}

	@Asynchronous
	public void render(@Observes FilesDownloadedEvent downloadedEvent) {

		final String baseProjectPath = Paths.get(	baseDirectoryService.getBaseDirectory(),
													downloadedEvent.getProjectName())
											.toString();
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
			try {
				generateHtml(file);
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "cannot create html", e);
				generateExceptionHTML(file, e);
			}
		}

		documentRegister.publishRegisteredDocuments();
	}

	void generateHtml(File origin) throws IOException {

		Path originPath = origin.toPath();
		AsciidocDocument doku = new AsciidocDocument(originPath, asciidoc).loadDocumentHeader().loadContent();
		String navigation = doku.getNavigation();

		if (navigation != null) {

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

			String fileName = origin.getName();
			String newFileName = fileName.replaceAll(".adoc", ".html").replaceAll(" ", "_");
			Path newFilePath = Paths.get(origin.getParent(), newFileName);
			try {
				doku.newRender().setContentEditor(contentEditor).enableContentEditing().convertToHtml(newFilePath);
			} catch (Exception e) {
				Logger.getLogger("asciidocgenerator-web").log(Level.SEVERE, "exception occured while rendering", e);
				generateExceptionHTMLPage(origin, newFilePath, e);
			}
			documentRegister.newRenderedDocument()
							.title(title)
							.group(group)
							.mainNavigation(mainNavigation)
							.navigationPath(navigationPath)
							.navigationKey(UUID.randomUUID().toString())
							.storageLocation(newFilePath.toString())
							.metainformation(updateDocuURL(metaInformation, originPath))
							.addKeywords(keywords)
							.build();
		}
	}

	MetaInformation updateDocuURL(MetaInformation source, Path filePath) {
		// path to file in gitlab
		// https://gitlab.com/group/repository/blob/version/folderxyz/filexyz.adoc
		// URL which Gitlab sends over HTTP: https://gitlab.com/group/repository/
		StringBuilder updatedURL = new StringBuilder(source.getVcsurl());
		String vcsurl = source.getVcsurl();

		if (vcsurl.equals("localhost")) {
			// if it does not come from an git server, it is not possible to update the url so that it points to the
			// source file
			return source;
		}

		final Path projectBaseDir = Paths.get(baseDirectoryService.getBaseDirectory(), source.getRepositoryname());

		Path relativeFromBaseDir = projectBaseDir.relativize(filePath);

		StringBuilder locationWithoutProjectBaseDir = new StringBuilder("/");
		relativeFromBaseDir.iterator().forEachRemaining((pathPart) -> {
			locationWithoutProjectBaseDir.append(pathPart).append("/");
		});

		updatedURL.append("/blob/");
		updatedURL.append(source.getVcsversion());
		updatedURL.append(locationWithoutProjectBaseDir.substring(0, locationWithoutProjectBaseDir.length() - 1));

		return MetaInformation	.newInstance()
								.projektname(source.getProjektname())
								.repositoryname(source.getRepositoryname())
								.vcsversion(source.getVcsversion())
								.vcsurl(updatedURL.toString())
								.build();
	}

	void generateExceptionHTMLPage(File origin, Path newFilePath, Throwable th) {
		try (InputStream resourceAsStream = getClass().getResourceAsStream("htmlerror_template.txt");
				Scanner scanner = new Scanner(resourceAsStream);) {
			StringBuilder newHtml = new StringBuilder();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (line.contains("ERROR_TITLE")) {
					line = line.replace("ERROR_TITLE", origin.getName());
				} else if (line.contains("ERROR_TEXT")) {
					line = line.replace("ERROR_TEXT", buildExceptionText(th));
				}
				newHtml.append(line).append(System.lineSeparator());
			}

			Files.write(newFilePath, newHtml.toString().getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			Logger.getLogger("asciidocgenerator-web").log(Level.SEVERE, "problem generating html", e);
		}
	}

	void generateExceptionHTML(File origin, Throwable th) {
		String fileName = origin.getName();
		String newFileName = fileName.replaceAll(".adoc", ".html");
		Path newFilePath = Paths.get(origin.getParent(), newFileName);

		generateExceptionHTMLPage(origin, newFilePath, th);

		documentRegister.newRenderedDocument()
						.title(fileName)
						.group("Problems")
						.mainNavigation("Defective Pages")
						.navigationPath("Problems/Defective Pages/" + fileName)
						.navigationKey(UUID.randomUUID().toString())
						.storageLocation(newFilePath.toString())
						.metainformation(metaInformation)
						.build();
	}

	String buildExceptionText(Throwable th) {
		StringBuilder sb = new StringBuilder("<b>" + th.getClass().toString() + "</b>");
		sb.append("<br><br>");
		Arrays.stream(th.getStackTrace()).forEach((element) -> {
			sb.append(element).append("<br>").append(System.lineSeparator());
		});
		return sb.toString();
	}

}
