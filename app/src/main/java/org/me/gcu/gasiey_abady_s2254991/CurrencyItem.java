package org.me.gcu.gasiey_abady_s2254991;

import java.io.Serializable;

public class CurrencyItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String baseName;
    private String baseCode;
    private String foreignName;
    private String foreignCode;


    public CurrencyItem() {
        title = "";
        description = "";
        link = "";
        pubDate = "";
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public String getBaseName() { return baseName; }
    public void setBaseName(String s) { baseName = s; }

    public String getBaseCode() { return baseCode; }
    public void setBaseCode(String s) { baseCode = s; }

    public String getForeignName() { return foreignName; }
    public void setForeignName(String s) { foreignName = s; }

    public String getForeignCode() { return foreignCode; }
    public void setForeignCode(String s) { foreignCode = s; }


    @Override
    public String toString() {
        return title + "\n" + description + "\n" + pubDate + "\n";
    }
}
