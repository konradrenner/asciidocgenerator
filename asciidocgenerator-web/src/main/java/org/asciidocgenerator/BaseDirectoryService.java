/*
 * The MIT License
 *
 * Copyright 2017 Konrad Renner.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.asciidocgenerator;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;

/**
 * This service is just necessary to support the linux home shortcut ~
 *
 * @author Konrad Renner
 */
@ApplicationScoped
public class BaseDirectoryService {

	@Resource(lookup = "java:global/htmldirectory")
	private String baseDirectory;

	private String correctDir;

	@PostConstruct
	void init() {
		correctDir = System.getProperty("asciidocgenerator.baseDirectory");
		if (correctDir == null) {
			correctDir = baseDirectory;
			if (correctDir.startsWith("~")) {
				correctDir = correctDir.replace("~", System.getProperty("user.home"));
			}
		}
	}

	public String getBaseDirectory() {
		return correctDir;
	}
}
