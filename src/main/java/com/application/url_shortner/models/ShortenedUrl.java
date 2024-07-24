package com.application.url_shortner.models;

public class ShortenedUrl {
    private int id;
    private String originalUrl;
    private String shortenedUrl;
    private int accessCount;

    public ShortenedUrl(String originalUrl, String shortenedUrl) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void increaseAccessCount(){
        accessCount ++ ;
    }

    public ShortenedUrl() {
    }

    public ShortenedUrl(String originalUrl, int accessCount) {
        this.originalUrl = originalUrl;
        this.accessCount = accessCount;
    }
}
