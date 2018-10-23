package pecek.roman.interview.crawler.dao.repository.impl;

import lombok.EqualsAndHashCode;
import org.hibernate.SessionFactory;
import pecek.roman.interview.crawler.dao.model.ArticleImage;
import pecek.roman.interview.crawler.dao.repository.ArticleImageRepository;

/**
 * Implementation of {@link ArticleImageRepository}. This repository is used for saving {@link ArticleImage} to database.
 */
@EqualsAndHashCode
public final class ArticleImageRepositoryImpl extends AbstractRepository<ArticleImage> implements ArticleImageRepository {

    /**
     * Singleton instance
     */
    private static ArticleImageRepository INSTANCE;

    /**
     * Private constructor preventing creation using new keyword
     * @param sessionFactory session factory for creating sessions
     */
    private ArticleImageRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Get instance of this repository
     * @param sessionFactory session factory for creating sessions
     * @return instance of {@link ArticleImageRepository}
     */
    public static ArticleImageRepository getInstance(SessionFactory sessionFactory) {
        if (INSTANCE == null) {
            INSTANCE = new ArticleImageRepositoryImpl(sessionFactory);
        }

        return INSTANCE;
    }
}
