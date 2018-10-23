package pecek.roman.interview.crawler.dao.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import pecek.roman.interview.crawler.dao.model.ArticleImage;
import pecek.roman.interview.crawler.dao.repository.ArticleImageRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ArticleImageRepositoryImplTest {

    @Test
    @DisplayName("Testing getting singleton instance")
    public void getInstanceTest() {
        SessionFactory sessionFactoryMock = Mockito.mock(SessionFactory.class);

        ArticleImageRepository firstInstance = ArticleImageRepositoryImpl.getInstance(sessionFactoryMock);
        ArticleImageRepository secondInstance = ArticleImageRepositoryImpl.getInstance(sessionFactoryMock);

        assertEquals(firstInstance, secondInstance);
    }

    @Test
    @DisplayName("Testing saving")
    public void saveValid() {
        SessionFactory sessionFactoryMock = mock(SessionFactory.class);
        Session sessionMock = mock(Session.class);
        Transaction transactionMock = mock(Transaction.class);
        ArticleImage articleImageMock = mock(ArticleImage.class);

        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);

        ArticleImageRepository articleRepository = ArticleImageRepositoryImpl.getInstance(sessionFactoryMock);

        articleRepository.save(articleImageMock);

        verify(sessionMock).persist(articleImageMock);
        verify(transactionMock).commit();
    }
}
