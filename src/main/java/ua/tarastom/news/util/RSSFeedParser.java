package ua.tarastom.news.util;

import ua.tarastom.news.model.Article;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RSSFeedParser {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String LANGUAGE = "language";
    static final String LINK = "link";
    static final String IMAGE_URL = "url";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String LAST_BUILD_DATE = "lastBuildDate";
    static final String GUID = "guid";
    static final String ENCLOSURE = "enclosure";
    static final String CATEGORY = "category";
    static final String FULL_TEXT = "full-text";
    static final String FULL_TEXT2 = "fulltext";
    static final String FULL_TEXT3 = "encoded";
    private final String country;
    List<Article> articles = new ArrayList<>();
    final URL url;

    public RSSFeedParser(String feedUrl, String country) {
        this.country = country;
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Article> readFeed() {
        try {
            boolean isFeedHeader = true;
            // Set header values initial to the empty string
            String description = "";
            String title = "";
            String titleChannel = "";
            String channelPubDate = "";
            String imageUrl = "";
            String link = "";
            String language = "";
            String pubdate = "";
            String guid = "";
            String full_text = "";
            List<String> enclosure = new ArrayList<>();
            List<String> categories = new ArrayList<>();

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    switch (localPart) {
                        case ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                titleChannel = title;
                                channelPubDate = pubdate;
                            }
                            eventReader.nextEvent();
                            break;
                        case TITLE:
                            title = getCharacterData(eventReader).trim();
                            break;
                        case DESCRIPTION:
                            description = getCharacterData(eventReader).trim();
                            break;
                        case LINK:
                            link = getCharacterData(eventReader);
                            break;
                        case GUID:
                            guid = getCharacterData(eventReader).trim();
                            break;
                        case LANGUAGE:
                            language = getCharacterData(eventReader);
                            break;
                        case LAST_BUILD_DATE:
                        case PUB_DATE:
                            pubdate = getCharacterData(eventReader);
                            break;
                        case ENCLOSURE:
                            Iterator<Attribute> attr = event.asStartElement().getAttributes();
                            while (attr.hasNext()) {
                                Attribute myAttribute = attr.next();
                                if (myAttribute.getName().toString().equals("url")) {
                                    enclosure.add(myAttribute.getValue());
                                }
                            }
                            break;
                        case IMAGE_URL:
                            imageUrl = getCharacterData(eventReader);
                            break;
                        case CATEGORY:
                            String categoryTemp = getCharacterData(eventReader).trim();
                            categories.add(formatCategory(categoryTemp));
                            break;
                        case FULL_TEXT:
                        case FULL_TEXT2:
                        case FULL_TEXT3:
                            full_text = getCharacterData(eventReader).trim();
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {

                        String source;
                        if (isValidUrl(link)) {
                            URL url = new URL(link);
                            if (url.getHost().startsWith("www.")) {
                                source = url.getHost().substring(4);
                            } else {
                                source = url.getHost();
                            }
                        } else {
                            //якщо link - не дійсна адреса, то об'єкт Article не створюється
//                            System.out.println(link);
                            continue;
                        }
                        Article article = new Article();
                        article.setTitleChannel(formatTitleChannel(titleChannel));
                        article.setSource(source);
                        article.setPubDateChannel(Utils.getDate(channelPubDate));
                        article.setLogo(imageUrl);
                        article.setLanguage(language);
                        article.setTitle(title);
                        article.setLink(link);

                        article.setDescription(description);

                        if (enclosure.isEmpty()) {
                            enclosure.add(imageUrl);
                            article.setEnclosure(enclosure);
                        } else {
                            article.setEnclosure(enclosure);
                        }

                        article.setGuid(guid);
                        article.setPubDate(Utils.getDate(pubdate));
                        article.setCategory(categories);
                        article.setFull_text(full_text);
                        article.setCountry(country);
                        articles.add(article);
                        eventReader.nextEvent();
                        categories = new ArrayList<>();
                        enclosure = new ArrayList<>();
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        System.out.println(Thread.currentThread().getName());
        return articles;
    }

    private String getCharacterData(XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        XMLEvent event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            System.out.println("error: " + url);
            throw new RuntimeException(e);
        }
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    private String formatTitleChannel(String title) {
        if (title.contains("Українські Національні Новини")) {
            title = "Українські Національні Новини";
        } else if (title.contains("Корреспондент.net")) {
            title = "Корреспондент.net";
        } else if (title.contains("Deutsche Welle")) {
            title = "Deutsche Welle";
        } else if (title.contains("Радіо Свобода")) {
            title = "Радіо Свобода";
        } else if (title.contains("Новини на tsn.ua")) {
            title = "TCH";
        } else if (title.contains("Гордон")) {
            title = "Гордон";
        } else if (title.contains("Экономические Новости")) {
            title = "Экономические Новости";
        }else if (title.contains("ТЕЛЕГРАФ")) {
            title = "ТЕЛЕГРАФ";
        }
        return title;
    }

    private String formatCategory(String category) {
        if (category.startsWith("Новини | ")) {
            category = category.replace("Новини | ", "");
        } else if (category.startsWith("Новости | ")) {
            category = category.replace("Новости | ", "");
        }
        return category;
    }
}
