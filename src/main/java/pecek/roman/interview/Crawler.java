package pecek.roman.interview;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jsoup.Jsoup;
import pecek.roman.interview.crawler.WikiPage;
import pecek.roman.interview.crawler.dao.model.ArticleImage;
import pecek.roman.interview.crawler.dao.repository.ArticleImageRepository;
import pecek.roman.interview.crawler.dao.repository.impl.ArticleImageRepositoryImpl;

import java.io.IOException;

/**
 * Crawler for parsing name and image from wiki page
 */
public class Crawler {

    /**
     * Error code returned when something goes wrong
     */
    private static final int ERROR_STATUS = 1;

    /**
     * Expected parameters count
     */
    private static final int PARAMETERS_COUNT = 1;

    /**
     * Index of wiki page url parameter
     */
    private static final int URL_PARAM = 0;

    /**
     * Message returned when wrong number of parameters if provided
     */
    private static final String WRONT_PARAMETERS_COUNT = "Url argument is missing!";


    public static void main(String[] args) throws IOException {

        // check if parameters are alright
        if(args.length != PARAMETERS_COUNT) {
            // wrong parameters count, lets exit
            System.out.println(WRONT_PARAMETERS_COUNT);
            System.exit(ERROR_STATUS);
        }

        // parameter count is ok, lets try to crawle
        WikiPage page = new WikiPage(Jsoup.connect(args[URL_PARAM]));

        // lets download the image
        String imagePath = page.downloadImage();

        // if image was successfully downloaded lets store it to database
        if (StringUtils.isNotEmpty(imagePath)) {
            SessionFactory sessionFactory = initialiseSessionFactory();

            ArticleImageRepository articleImageRepository = ArticleImageRepositoryImpl.getInstance(sessionFactory);
            articleImageRepository.save(new ArticleImage(page.getName(), imagePath));

            // clean up
            sessionFactory.close();
        }
    }

    /**
     * Initialise session factory to access database
     * @return {@link SessionFactory} initialised session factory
     */
    private static SessionFactory initialiseSessionFactory() {
        // build session factory
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();

        SessionFactory sessionFactory = null;

        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            System.exit(ERROR_STATUS);
        }

        return sessionFactory;
    }
}
