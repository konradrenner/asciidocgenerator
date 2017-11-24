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
package org.asciidocgenerator.domain.rendering;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.asciidocgenerator.domain.MetaInformation;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Konrad Renner
 */
public class HtmlRenderServiceBeanTest {

	private HtmlRenderServiceBean underTest;

	@Before
	public void setUp() {
		underTest = new HtmlRenderServiceBean();
	}

	@Test
	public void testUpdateDocURL() {
		Path path = Paths.get("tmp", "group", "repo", "folder1", "folder2", "file.adoc");
		MetaInformation metainfo = MetaInformation	.newInstance()
													.projektname("project")
													.repositoryname("repo")
													.vcsversion("1.0.0")
													.vcsurl("http://www.gitlab.com/group/repo")
													.build();

		MetaInformation updateDocuURL = underTest.updateDocuURL(metainfo, path);

		assertThat(updateDocuURL.getProjektname(), is(updateDocuURL.getProjektname()));
		assertThat(updateDocuURL.getRepositoryname(), is(updateDocuURL.getRepositoryname()));
		assertThat(	updateDocuURL.getVcsurl(),
					is("http://www.gitlab.com/group/repo/blob/1.0.0/folder1/folder2/file.adoc"));
		assertThat(updateDocuURL.getVcsversion(), is(updateDocuURL.getVcsversion()));
	}

	@Test
	public void testUpdateDocURLGroupNameSameAsRepoName() {
		Path path = Paths.get("tmp", "repo", "repo", "folder1", "folder2", "file.adoc");
		MetaInformation metainfo = MetaInformation	.newInstance()
													.projektname("project")
													.repositoryname("repo")
													.vcsversion("1.0.0")
													.vcsurl("http://www.gitlab.com/repo/repo")
													.build();

		MetaInformation updateDocuURL = underTest.updateDocuURL(metainfo, path);

		assertThat(updateDocuURL.getProjektname(), is(updateDocuURL.getProjektname()));
		assertThat(updateDocuURL.getRepositoryname(), is(updateDocuURL.getRepositoryname()));
		assertThat(	updateDocuURL.getVcsurl(),
					is("http://www.gitlab.com/repo/repo/blob/1.0.0/folder1/folder2/file.adoc"));
		assertThat(updateDocuURL.getVcsversion(), is(updateDocuURL.getVcsversion()));
	}

	@Test
	public void testUpdateDocURLGroupLocalhost() {
		Path path = Paths.get("tmp", "repo", "repo", "folder1", "folder2", "file.adoc");
		MetaInformation metainfo = MetaInformation	.newInstance()
													.projektname("project")
													.repositoryname("repo")
													.vcsversion("1.0.0")
													.vcsurl("localhost")
													.build();

		MetaInformation updateDocuURL = underTest.updateDocuURL(metainfo, path);

		assertThat(updateDocuURL.getProjektname(), is(updateDocuURL.getProjektname()));
		assertThat(updateDocuURL.getRepositoryname(), is(updateDocuURL.getRepositoryname()));
		assertThat(updateDocuURL.getVcsurl(), is(metainfo.getVcsurl()));
		assertThat(updateDocuURL.getVcsversion(), is(updateDocuURL.getVcsversion()));
	}
}
