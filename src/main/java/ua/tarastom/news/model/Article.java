package ua.tarastom.news.model;

import ua.tarastom.news.util.Utils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Article implements Comparable<Article>{

    private @Id @GeneratedValue Long id;
    private String country;
    private String titleChannel;
    private String source;
    private String pubDateChannel;
    private String logo;
    private String language;
    private String title;
    private String link;
    @Lob
    @Column(length = 1000000)
    private String description;
    private String guid;
    private String pubDate;

    @Lob
    @Column(length = 1000000)
    private String full_text;

    @ElementCollection
    private List<String> enclosure;
    @ElementCollection
    private List<String> category;

    public Article() {
    }

    public Article(String title, String link) {
        this.title = title;
        this.link = link;
        category = new ArrayList<>();
        enclosure = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitleChannel() {
        return titleChannel;
    }

    public void setTitleChannel(String titleChannel) {
        this.titleChannel = titleChannel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(List<String> enclosure) {
        this.enclosure = enclosure;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPubDateChannel() {
        return pubDateChannel;
    }

    public void setPubDateChannel(String pubDateChannel) {
        this.pubDateChannel = pubDateChannel;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int compareTo(Article article) {
        Date date1 = Utils.getDate(this.getPubDate());
        Date date2 = Utils.getDate(article.getPubDate());
        return date1.compareTo(date2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return link.equals(article.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", titleChannel='" + titleChannel + '\'' +
                ", source='" + source + '\'' +
                ", pubDateChannel='" + pubDateChannel + '\'' +
                ", logo='" + logo + '\'' +
                ", language='" + language + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", guid='" + guid + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", enclosure=" + enclosure +
                ", category=" + category +
                '}';
    }
}
