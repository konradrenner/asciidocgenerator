package org.asciidocgenerator.ui.admin;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.asciidocgenerator.Logged;
import org.asciidocgenerator.domain.ReorganisationService;

@RequestScoped
@Logged
public class ReorgController {

	@Inject
	private ReorganisationService service;
	

	public void reorg(@Observes ManipulateSettingCommand command) {
		if (command.getSetting().equals("reorg")) {
			service.removeNonExistingData();
		}
	}
}
