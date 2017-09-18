package org.asciidocgenerator.domain.content;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContentServiceTest {

	@Mock
    private ContentRepository repo;

    @Mock
    private CategorieRepository categoriesRepo;

	private ContentService underTest;

	@Before
	public void setUp() {
            underTest = new ContentService(repo, categoriesRepo);
	}

	@Test
	public void newArticleInserted() {
		Article mockArticle = mock(Article.class);
		when(repo.findArticle(mockArticle.getKey())).thenReturn(Optional.empty());

		underTest.applyChanges(mockArticle);

		verify(repo).insert(mockArticle);
	}

	@Test
	public void articleUpdated() {
		Article mockArticle = mock(Article.class);
		when(repo.findArticle(mockArticle.getKey())).thenReturn(Optional.ofNullable(mockArticle));

		underTest.applyChanges(mockArticle);

		verify(repo).update(mockArticle);
	}

}
