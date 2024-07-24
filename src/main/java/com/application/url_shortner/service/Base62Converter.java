package com.application.url_shortner.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class Base62Converter {
    private static String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        BigInteger value = new BigInteger(1, bytes);
        StringBuilder result = new StringBuilder();
        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = value.divideAndRemainder(BigInteger.valueOf(62));
            result.insert(0, BASE62.charAt(divmod[1].intValue()));
            value = divmod[0];
        }
        return result.length() > 0 ? result.toString() : "0";
    }
}
