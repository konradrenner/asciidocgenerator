package org.asciidocgenerator.domain.rendering;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.asciidoctor.Asciidoctor;

@ApplicationScoped
public class AsciidoctorProducer {

	@Produces
	@ApplicationScoped
	public Asciidoctor getAsciidoctor() {
		Asciidoctor asciidoc = Asciidoctor.Factory.create();
		asciidoc.requireLibrary("asciidoctor-diagram");
		return asciidoc;
	}
}
