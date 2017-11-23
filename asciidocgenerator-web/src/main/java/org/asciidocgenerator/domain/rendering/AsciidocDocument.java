package org.asciidocgenerator.domain.rendering;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.DocumentHeader;

public class AsciidocDocument {

	enum BACKEND {
					PDF {

						@Override
						Attributes getAttributes() {
							return AttributesBuilder.attributes()
													.icons("font")
													.tableOfContents(true)
													.sectionNumbers(true)
													.sourceHighlighter("coderay")
													.get();
						}

					},
					HTML {

						@Override
						Attributes getAttributes() {
							return AttributesBuilder.attributes()
													.tableOfContents(true)
													.sectionNumbers(true)
													.sourceHighlighter("coderay")
													.get();
						}

					};

		abstract Attributes getAttributes();

		OptionsBuilder getOptions(File baseDir) {
			return OptionsBuilder	.options()
									.safe(SafeMode.SAFE)
									.baseDir(baseDir)
									.backend(this.toString().toLowerCase())
									.headerFooter(true)
									.attributes(this.getAttributes());
		}
	}

	private Asciidoctor asciidoc;
	private final Path sourceFile;
	private DocumentHeader documentHeader;
	private List<String> rawContent;
	private ContentEditor editor;

	public AsciidocDocument(Path sourceFile, Asciidoctor asciidoc) {
		this.sourceFile = sourceFile;
		this.asciidoc = asciidoc;
		rawContent = new ArrayList<>();
	}

	public Path getSourceFile() {
		return this.sourceFile;
	}

	AsciidocDocument loadDocumentHeader() {
		try (Reader reader = new InputStreamReader(new FileInputStream(sourceFile.toFile()), StandardCharsets.UTF_8)) {
			this.documentHeader = this.asciidoc.readDocumentHeader(reader);
		} catch (IOException e) {
			Logger.getLogger("asciidocgenerator-web").log(Level.WARNING, "couldn't load document header", e);
		}
		return this;
	}

	AsciidocDocument addAttributesToHeader(Map<String, Object> attribsNew) {

		Map<String, Object> attribs = documentHeader.getAttributes();
		attribs.putAll(attribsNew);

		documentHeader = DocumentHeader.createDocumentHeader(	documentHeader.getDocumentTitle(),
																documentHeader.getPageTitle(),
																attribs);
		return this;
	}

	AsciidocDocument loadContent() throws IOException {
		Files.readAllLines(sourceFile).stream().map(x -> x + System.lineSeparator()).forEach(rawContent::add);
		return this;
	}

	private String contentAsString(List<String> content) {
		StringBuilder builder = new StringBuilder();
		content.stream().forEach(builder::append);
		return builder.toString();
	}

	public String getKeywords() {
		return getAttribute("keywords");
	}

	public String getNavigation() {
		return getAttribute("navigation");
	}

	public Map<String, Object> getHeaderAttributes() {
		return this.documentHeader.getAttributes();
	}

	public String getMainDocumentTitle() {
		return this.documentHeader.getDocumentTitle().getMain();
	}

	private String getAttribute(Object key) {
		Object attrib = this.documentHeader.getAttributes().get(key);
		return (attrib == null) ? null : attrib.toString();
	}

	public List<String> getContent() {
		return rawContent;
	}

	public DocumentHeader getDocumentHeader() {
		return documentHeader;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AsciidocDocument other = (AsciidocDocument) obj;
		if (this.sourceFile.equals(other.getSourceFile())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceFile);
	}

	@Override
	public String toString() {
		return "AsciidocDocument [sourceFile=" + sourceFile + ", content=" + rawContent + "]";
	}

	public Render newRender() {
		return new Render(this);
	}

	public static class Render {

		private AsciidocDocument doku;
		private boolean editContent;

		Render(AsciidocDocument doku) {
			this.doku = doku;
		}

		@SuppressWarnings("unused")
		private Render() {
			throw new IllegalAccessError();
		}

		@SuppressWarnings("synthetic-access")
		public Render setContentEditor(ContentEditor editor) {
			this.doku.editor = editor;
			return this;
		}

		public Render enableContentEditing() {
			this.editContent = true;
			return this;
		}

		@SuppressWarnings("synthetic-access")
		public void convertToHtml(Path destinationFile) {
			String contentString;
			if (this.editContent) {
				List<String> newContent = this.doku.editor.editedContent(this.doku.rawContent);
				contentString = this.doku.contentAsString(newContent);
			} else {
				contentString = this.doku.contentAsString(this.doku.rawContent);
			}

			try (Reader reader = new StringReader(contentString);
                                Writer writer = new OutputStreamWriter(new FileOutputStream(destinationFile.toFile()), StandardCharsets.UTF_8)) {
				convertTo(reader, writer, "html");
			} catch (IOException e) {
				Logger.getLogger("AsciidocDocument").log(Level.SEVERE, "converting to html failed", e);
			}

		}

		public void convertToPdf(OutputStream stream) throws IOException {
			// keine ahnung warum, aber fuer die pdf generierung funktioniert convert nicht, convertFile schon...
			String sourceFilePath = this.doku.sourceFile.toString();
			String destinationFilePath = sourceFilePath.substring(0, sourceFilePath.indexOf(".html"));
			Path documentToRender = Paths.get(destinationFilePath + ".adoc");
			Path renderedDocument = Paths.get(destinationFilePath + ".pdf");

			if (Files.notExists(renderedDocument)) {
				doku.asciidoc.convertFile(documentToRender.toFile(), BACKEND.PDF.getOptions(getBaseDirForConvert()));
			}

			try (InputStream in = Files.newInputStream(renderedDocument); OutputStream out = stream;) {
				byte[] buffer = new byte[1024];
				int count;
				while ((count = in.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}
			}
		}

		private File getBaseDirForConvert() {
			return this.doku.sourceFile.getParent().toFile();
		}

		@SuppressWarnings("synthetic-access")
		private void convertTo(Reader source, Writer destination, String backend) throws IOException {
			doku.asciidoc.convert(	source,
									destination,
									BACKEND.valueOf(backend.toUpperCase()).getOptions(getBaseDirForConvert()));
		}

	}

}
