package com.application.url_shortner.service;
import java.util.Random;

public class Base62Converter {
    private static final String BASE_URL = "http://short.url/";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private static Random random = new Random();

    public static String generateShortUrl(String url) {
        StringBuilder shortUrl = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            shortUrl.append(CHARACTERS.charAt(index));
        }
        return shortUrl.toString();
    }
}
