package org.asciidocgenerator.api.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.asciidocgenerator.FilesDownloadedEvent;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.PushToRepositoryOccuredEvent;
import org.asciidocgenerator.PushToRepositoryOccuredEvent.ObjectKind;

@Path("generator")
@Logged
public class GeneratorResource {

	@Inject
	private Event<PushToRepositoryOccuredEvent> pushEvent;

	@Inject
	private Event<FilesDownloadedEvent> downloadedEvent;

	@Path("generatelocalFiles")
	@POST
	@Consumes("application/json")
	public Response generateLocalFiles(JsonObject object) {
		try {
			String name = object.getString("name");
			String path = object.getString("localPath");
			String version = object.getString("version");

			FilesDownloadedEvent event = new FilesDownloadedEvent("Local", name, Paths.get(path), "localhost", version);

			downloadedEvent.fire(event);

		} catch (Exception e) {
			Logger.getLogger(getClass().toString()).log(Level.SEVERE, "problem processing event", e);
			return Response.serverError().build();
		}
		return Response.ok().build();
	}

	@Path("gitlabtagpushed")
	@POST
	@Consumes("application/json")
	public Response gitlabTagPushed(@HeaderParam("X-Gitlab-Event") String gitlabEvent, JsonObject object) {
		if (isNotGitlabTagPushedEvent(gitlabEvent)) {
			return Response.notModified().build();
		}

		try {
			ObjectKind objectKind = ObjectKind.valueOf(object.getString("object_kind").toUpperCase());
			String reference = object.getString("ref");
			String projectName = object.getJsonObject("project").getString("name");
			String url = object.getJsonObject("project").getString("web_url");
			String repositoryName = object.getJsonObject("repository").getString("name");

			PushToRepositoryOccuredEvent event = PushToRepositoryOccuredEvent	.newInstance()
																				.repositoryName(repositoryName)
																				.projectName(projectName)
																				.url(new URL(url))
																				.reference(reference)
																				.objectKind(objectKind)
																				.build();

			pushEvent.fire(event);
		} catch (Exception e) {
			Logger.getLogger(getClass().toString()).log(Level.SEVERE, "problem processing event", e);
			return Response.serverError().build();
		}
		return Response.ok().build();
	}

	private boolean isNotGitlabTagPushedEvent(String gitlabEvent) {
		return !"Tag Push Hook".equalsIgnoreCase(gitlabEvent);
	}
}