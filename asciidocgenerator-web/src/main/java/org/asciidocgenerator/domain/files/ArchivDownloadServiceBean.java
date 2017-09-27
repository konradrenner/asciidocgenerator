package org.asciidocgenerator.domain.files;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.DokuGeneratorException.ErrorCode;
import org.asciidocgenerator.FilesDownloadedEvent;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.PushToRepositoryOccuredEvent;
import org.asciidocgenerator.TokenService;

@Stateless
@Logged
public class ArchivDownloadServiceBean {

	@Inject
	private TokenService token;

	@Inject
	private ExtractArchiveService extractService;

	@Inject
	private Event<FilesDownloadedEvent> downloadedEvent;

	@Asynchronous
	public void downloadArchive(@Observes PushToRepositoryOccuredEvent event) {
		URL downloadURL = buildDownloadURL(event.getUrl(), event.getReference());
		try {

			Path tempFile = Files.createTempFile("gitlabdownload", ".zip");

			download(downloadURL, tempFile);

			Path destinationFolder = extractService.extract(tempFile, event.getProjectName());

			Files.delete(tempFile);
			downloadedEvent.fire(new FilesDownloadedEvent(	event.getRepositoryName(),
															event.getProjectName(),
															destinationFolder,
															event.getUrl().toString(),
															removeWebhookPrefix(event.getReference())));
		} catch (IOException e) {
			Logger.getLogger(getClass().toString()).log(Level.SEVERE, "unable to download project", e);
			throw new DokuGeneratorException(ErrorCode.UNABLE_TO_DOWNLOAD_PROJECT, e);
		}
	}

	private void download(URL downloadURL, Path tempFile) throws IOException {
		try (ReadableByteChannel source = Channels.newChannel(downloadURL.openStream());
				FileChannel destination = FileChannel.open(	tempFile,
															StandardOpenOption.WRITE,
															StandardOpenOption.TRUNCATE_EXISTING)) {

			destination.transferFrom(source, 0, Long.MAX_VALUE);
		}
	}

	private URL buildDownloadURL(URL base, String ref) {
		StringBuilder newUrl = new StringBuilder(base.toString());
		newUrl.append("/repository/archive.zip?private_token=");
		newUrl.append(token.getToken());
		newUrl.append("&ref=");
		newUrl.append(removeWebhookPrefix(ref));
		try {
			return new URL(newUrl.toString());
		} catch (MalformedURLException e) {
			Logger.getLogger(getClass().toString()).log(Level.SEVERE, "url not correct", e);
			throw new DokuGeneratorException(ErrorCode.MALFORMED_REQUESTED_DOWNLOAD_URL, e);
		}
	}

	private String removeWebhookPrefix(String ref) {
		return ref.substring(ref.lastIndexOf('/') + 1);
	}
}
