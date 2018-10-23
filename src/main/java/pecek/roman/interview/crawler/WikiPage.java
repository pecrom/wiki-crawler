package pecek.roman.interview.crawler;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * WikiPage represents desired wiki page
 */
public class WikiPage {

    /**
     * ID of the html element with name
     */
    private static final String NAME_ID = "firstHeading";


    /**
     * Class of the html element with image
     */
    private static final String INFOBOX_CLASS = "infobox vcard";

    /**
     * Child tr tag of infobox
     */
    private static final String TR_TAG = "tr";

    /**
     * Img element with image
     */
    private static final String IMG_TAG = "img";

    /**
     * Src attribute with image url
     */
    private static final String SRC_ATTR = "src";

    /**
     * Default path where images will be stored
     */
    private static final String DEFAULT_STORE_PATH = "images";

    /**
     * If not set, default path is used
     */
    private String storePath = DEFAULT_STORE_PATH;

    /**
     * Loaded html wiki page
     */
    private final Document loadedPage;

    /**
     * Name of the page
     */
    @Getter
    private final String name;

    /**
     * Url of the image
     */
    private final String imageUrl;

    /**
     * Constructor loading wiki page
     * @param connection connection to get html from
     * @throws IOException can not get html page then {@link IOException} is thrown
     */
    public WikiPage(Connection connection) throws IOException {
        loadedPage = connection.get();
        name = parseName();
        imageUrl = parseImage();
    }

    /**
     * Constructo loading wiki page and changes default image store path
     * @param storePath path where the images will be stored
     * @param connection connection to get html from
     * @throws IOException when the provided url is not reachable then {@link IOException} is thrown
     */
    public WikiPage(String storePath, Connection connection) throws IOException {
        this(connection);
        this.storePath = storePath;
    }

    /**
     * Parse name of the wiki page
     * @return name of the wiki page
     */
    private String parseName() {
        Element nameElement = loadedPage.body().getElementById(NAME_ID);

        // when wiki page does not contain element with NAME_ID, lets return empty name
        return nameElement != null ? nameElement.text() : StringUtils.EMPTY;
    }

    /**
     * Parse image url from the wiki page
     * @return url of the image
     */
    private String parseImage() {

        // lets get body element, if not found, then return empty image url
        Element body = loadedPage.body();
        if (body == null) {
            return StringUtils.EMPTY;
        }

        // lets get infobox, if not found, then return empty image url
        Elements infoBox = body.getElementsByClass(INFOBOX_CLASS);
        if (infoBox.isEmpty()) {
            return StringUtils.EMPTY;
        }

        // lets get rows from info box table, if not found, then return empty image url
        Elements rows = infoBox.select(TR_TAG);
        if (rows.isEmpty()) {
            return StringUtils.EMPTY;
        }

        // lets get image elements from the first row of info box table, if not found, then return empty url
        Elements image = rows.first().select(IMG_TAG);
        if (image.isEmpty()) {
            return StringUtils.EMPTY;
        }

        // get src attribute of img element, if found then add protocol else return empty url
        return image.hasAttr(SRC_ATTR) ? withProtocol(image.attr(SRC_ATTR)) : StringUtils.EMPTY;
    }


    /**
     * Add protocol to image url
     * @param srcAttr src attribute value
     * @return url with protocol
     */
    private String withProtocol(String srcAttr) {
        return "https:" + srcAttr;
    }

    /**
     * Download image to the store path and return store path as {@link String}
     * @return path of the downloaded image
     * @throws IOException when is it now possible to store image to the desired path, then {@link IOException} is thrown
     */
    public String downloadImage() throws IOException {
        // check if image urls is present
        if (StringUtils.isNotEmpty(imageUrl)) {

            // create image dir path
            String imageDirPath = createImageDirPath();

            // create whole path of the downloaded image in file system
            String imagePath = createDownloadedFilePath(imageDirPath);

            // create the path in file system where the image is going to be stored
            createStorePath(imageDirPath);

            // lets download the image
            URL downloadUrl = new URL(imageUrl);
            ReadableByteChannel imageInput = Channels.newChannel(downloadUrl.openStream());

            FileOutputStream imageOutputStream = new FileOutputStream(imagePath);
            FileChannel imageOutput = imageOutputStream.getChannel();

            // copy input stream to output stream
            imageOutput.transferFrom(imageInput, 0, Long.MAX_VALUE);


            return imagePath;
        }

        // if not present then return empty store path
        return StringUtils.EMPTY;
    }

    /**
     * Create path with store path and wiki page name
     * @return path as a {@link String}
     */
    private String createImageDirPath() {
        return storePath + File.separator + name;
    }

    /**
     * Create whole path to downloaded image as a {@link String}
     * @param storeDir store directory without final image name, just the path
     * @return whole downloaded image path include file name as a {@link String}
     */
    private String createDownloadedFilePath(String storeDir) {
        return storeDir + File.separator + name + "." + StringUtils.substringAfterLast(imageUrl, ".");
    }

    /**
     * Create store path in file system if not exists
     * @param imageStorePath store path
     */
    private void createStorePath(String imageStorePath) {
        File storePathDir = new File(imageStorePath);
        if (!storePathDir.exists()) {
            storePathDir.mkdirs();
        }
    }
}
