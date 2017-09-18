package org.asciidocgenerator.api;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("api/v1")
public class REStApplicationConfig
		extends Application {
	// nothing at the moment
}
