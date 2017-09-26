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
package org.asciidocgenerator.api.generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Konrad Renner
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(description = "Path to a local folder with asciidoc files and other information for generating documentation")
public class LocalFilePath {

	@ApiModelProperty(value = "A logical name for the group of files, typically the folder name", required = true)
	@XmlElement(required = true)
	private String name;
	@ApiModelProperty(	value = "Operating system depending path to the folder which contains the asciidoc files",
						required = true)
	@XmlElement(name = "localPath", required = true)
	private String path;
	@ApiModelProperty(value = "Version name or number of the generation", required = true)
	@XmlElement(required = true)
	private String version;

	LocalFilePath() {
		// just for rest resource
	}

	public String getName() {
		return name;
	}

	public Path getPath() {
		return Paths.get(path);
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "LocalFilePath{" + "name=" + name + ", path=" + path + ", version=" + version + '}';
	}

}
