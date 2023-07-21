package com.pragma.powerup.domain.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilNumbers {
    
    private UtilNumbers() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean onlyNumbers(String text) {
        Pattern patron = Pattern.compile("^\\d+$");
        Matcher matcher = patron.matcher(text);
        return matcher.matches();
    }
}
