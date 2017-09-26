package org.asciidocgenerator.api.generator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URL;
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

@Api(value = "generator")
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
	@ApiOperation(value = "Generates documentation from a local file source")
	public Response generateLocalFiles(LocalFilePath pathToFolder) {
		try {
			String name = pathToFolder.getName();
			java.nio.file.Path path = pathToFolder.getPath();
			String version = pathToFolder.getVersion();

			FilesDownloadedEvent event = new FilesDownloadedEvent("Local", name, path, "localhost", version);

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
	@ApiOperation(	value = "Accepts GitLab Tag Push Events, downloads and generates documentation with the information of the tag push event",
					notes = "Please take a look at https://docs.gitlab.com/ce/user/project/integrations/webhooks.html for details of the GitLab JSON format")
	public Response gitlabTagPushed(@ApiParam(	value = "Type of the GitLab Event",
												required = true) @HeaderParam("X-Gitlab-Event") String gitlabEvent,
									JsonObject object) {
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