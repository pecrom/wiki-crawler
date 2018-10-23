package pecek.roman.interview.crawler.dao.repository;


public interface Repository<T> {

    /**
     * Save entity to database
     * @param entity entity to be saved
     * @return saved entity
     */
    T save(T entity);
}
