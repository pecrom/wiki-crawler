package pecek.roman.interview.crawler.dao.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pecek.roman.interview.crawler.dao.repository.Repository;

/**
 * Abstract repository class
 */
public abstract class AbstractRepository<Entity> implements Repository<Entity> {

    /**
     * Session factory for creating sessions
     */
    protected SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    public Entity save(Entity entity) {

        Transaction tx = null;
        try(Session openedSession = sessionFactory.openSession()) {
            // start transaction
            tx = openedSession.beginTransaction();

            openedSession.persist(entity);
            // commit
            tx.commit();
        }
        catch (Exception e) {
            tx.rollback();
        }

        return entity;
    }

    protected AbstractRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
