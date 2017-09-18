package org.asciidocgenerator.domain.rendering;

import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class HtmlDocumentTest {

	private HtmlDocument underTest;

	@Before
	public void setUp() {
		underTest = new HtmlDocument("");
	}

	@Test
	public void leeresFile() {
		InputStream resourceAsStream = getClass().getResourceAsStream("emptyFile.html");

		String result = underTest.manipulateContent(resourceAsStream);
		try {
			resourceAsStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(new StringBuilder().toString(), result);

	}

	@Test
	public void bodyLessFile() {
		InputStream resourceAsStream = getClass().getResourceAsStream("bodyLessFile.html");

		String result = underTest.manipulateContent(resourceAsStream);
		try {
			resourceAsStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(new StringBuilder().toString(), result);

	}

	@Test
	public void completeFile() {
		InputStream resourceAsStream = getClass().getResourceAsStream("completeFile.html");

		String result = underTest.manipulateContent(resourceAsStream);
		try {
			resourceAsStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            StringBuilder expectedContent = new StringBuilder().append("<div>").append(System.lineSeparator()).append("<div>").append(System.lineSeparator())
                    .append("Hallo Welt!!!").append(System.lineSeparator())
                    .append("</div>").append(System.lineSeparator());

		assertEquals(expectedContent.toString(), result.replace("	", ""));

	}

}
