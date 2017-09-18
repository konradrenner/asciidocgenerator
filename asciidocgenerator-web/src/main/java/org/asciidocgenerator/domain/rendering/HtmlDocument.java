package org.asciidocgenerator.domain.rendering;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class HtmlDocument {

	private File theFile;

	public HtmlDocument(String filePath) {

		this.theFile = new File(filePath);
	}

	public boolean exists() {
		return theFile.exists();
	}

	public String readContent() throws IOException {
		
		List<String> readAllLines = Files.readAllLines(theFile.toPath());
		StringBuilder builder = new StringBuilder();
		for (String line : readAllLines) {
			builder.append(line).append(System.lineSeparator());
		}
		InputStream inputStream = new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));
		return manipulateContent(inputStream);

	}

	String manipulateContent(InputStream inputStream) {

		StringBuilder builder = new StringBuilder();
		Scanner scanner = new Scanner(inputStream);
		String body = scanner.findWithinHorizon("<body", Integer.MAX_VALUE);
		if (body != null) {
			builder.append("<div");


			String line = scanner.nextLine();
			while (scanner.hasNextLine() && !line.contains("</body>")) {
				if (line.contains("h1")) {
					line = line.replace("<h1", "<div class=\"sect0\"><h1");
					line = line.replaceAll("</h1>", "</h1></div>");
				}
				if (line.contains("<link") | line.contains("<meta") | line.contains("<title>")) {
					line = "";
				}

				builder.append(line).append(System.lineSeparator());
				line = scanner.nextLine();
			}
		}

		scanner.close();
		return builder.toString();

	}
}
