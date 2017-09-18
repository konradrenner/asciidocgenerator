package org.asciidocgenerator.api.generator;

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

import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.Logged;
import org.asciidocgenerator.PushToRepositoryOccuredEvent;
import org.asciidocgenerator.DokuGeneratorException.ErrorCode;
import org.asciidocgenerator.PushToRepositoryOccuredEvent.ObjectKind;

@Path("generator")
@Logged
public class GeneratorResource {

	@Inject
	private Event<PushToRepositoryOccuredEvent> pushEvent;

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
			throw new DokuGeneratorException(ErrorCode.MALFORMED_GITLAB_JSON);
		}
		return Response.ok().build();
	}

	private boolean isNotGitlabTagPushedEvent(String gitlabEvent) {
		return !"Tag Push Hook".equalsIgnoreCase(gitlabEvent);
	}
}