package com.application.url_shortner.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static boolean validateURl(String originalUrl){
        String regex = "((http|https)://)(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                + "{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%"
                + "._\\+~#?&//=]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(originalUrl);
        return matcher.matches();
    }
}
