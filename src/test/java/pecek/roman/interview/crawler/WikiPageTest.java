package pecek.roman.interview.crawler;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WikiPageTest {

    @Test
    @DisplayName("Testing constructor all valid")
    public void constructAllValid() throws IOException {
        Connection connectionMock = mock(Connection.class);
        Document documentMock = mock(Document.class);

        Element bodyElementMock = mock(Element.class);
        Element firstHeadingMock = mock(Element.class);
        Element firstRowMock = mock(Element.class);

        Elements infoBoxMock = mock(Elements.class);
        Elements rowsMock = mock(Elements.class);
        Elements imgMock = mock(Elements.class);

        String expectedName = "Google";
        String srcAttr = "/address";

        when(bodyElementMock.getElementsByClass(anyString())).thenReturn(infoBoxMock);
        when(infoBoxMock.isEmpty()).thenReturn(false);

        when(infoBoxMock.select(anyString())).thenReturn(rowsMock);
        when(rowsMock.isEmpty()).thenReturn(false);
        when(rowsMock.first()).thenReturn(firstRowMock);

        when(firstRowMock.select(anyString())).thenReturn(imgMock);
        when(imgMock.isEmpty()).thenReturn(false);

        when(imgMock.hasAttr(anyString())).thenReturn(true);
        when(imgMock.attr(anyString())).thenReturn(srcAttr);

        when(firstHeadingMock.text()).thenReturn(expectedName);
        when(bodyElementMock.getElementById(anyString())).thenReturn(firstHeadingMock);
        when(documentMock.body()).thenReturn(bodyElementMock);
        when(connectionMock.get()).thenReturn(documentMock);

        WikiPage wikiPage = new WikiPage(connectionMock);

        assertEquals(expectedName, wikiPage.getName());
    }

    @Test
    @DisplayName("Testing constructor invalid name")
    public void constructInvalidName() throws IOException {
        Connection connectionMock = mock(Connection.class);
        Document documentMock = mock(Document.class);

        Element bodyElementMock = mock(Element.class);
        Element firstRowMock = mock(Element.class);

        Elements infoBoxMock = mock(Elements.class);
        Elements rowsMock = mock(Elements.class);
        Elements imgMock = mock(Elements.class);

        String srcAttr = "/address";

        when(bodyElementMock.getElementsByClass(anyString())).thenReturn(infoBoxMock);
        when(infoBoxMock.isEmpty()).thenReturn(false);

        when(infoBoxMock.select(anyString())).thenReturn(rowsMock);
        when(rowsMock.isEmpty()).thenReturn(false);
        when(rowsMock.first()).thenReturn(firstRowMock);

        when(firstRowMock.select(anyString())).thenReturn(imgMock);
        when(imgMock.isEmpty()).thenReturn(false);

        when(imgMock.hasAttr(anyString())).thenReturn(true);
        when(imgMock.attr(anyString())).thenReturn(srcAttr);

        when(bodyElementMock.getElementById(anyString())).thenReturn(null);
        when(documentMock.body()).thenReturn(bodyElementMock);
        when(connectionMock.get()).thenReturn(documentMock);

        WikiPage wikiPage = new WikiPage(connectionMock);

        assertEquals(StringUtils.EMPTY, wikiPage.getName());
    }
}
