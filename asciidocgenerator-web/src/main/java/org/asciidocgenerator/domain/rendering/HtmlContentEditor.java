package org.asciidocgenerator.domain.rendering;

import java.util.ArrayList;
import java.util.List;

public class HtmlContentEditor
		implements ContentEditor {

	@Override
	public List<String> editedContent(List<String> content) {
		List<String> newContent = new ArrayList<>();
		for (String line : content) {
			if (line.contains(":navigation:")) {
				newContent.add(line);
				newContent.add(":data-uri:" + System.lineSeparator());
				continue;
			}
			newContent.add(line);
		}
		return newContent;
	}
}
