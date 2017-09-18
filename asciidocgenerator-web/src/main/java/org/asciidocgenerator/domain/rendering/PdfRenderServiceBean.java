package org.asciidocgenerator.domain.rendering;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.Logged;
import org.asciidoctor.Asciidoctor;

@Stateless
@Logged
public class PdfRenderServiceBean {

	@Inject
	private Asciidoctor asciidoctor;

	public void streamPdf(Path asciidocfile, OutputStream stream) {
		try {
			AsciidocDocument doku = new AsciidocDocument(asciidocfile, asciidoctor).loadDocumentHeader().loadContent();
			doku.newRender().convertToPdf(stream);
		} catch (IOException e) {
			Logger.getLogger(getClass().toString()).log(Level.SEVERE, "unable to generate PDF", e);
			throw new DokuGeneratorException(DokuGeneratorException.ErrorCode.UNABLE_TO_GENERATE_PDF, e);
		}
	}

}
