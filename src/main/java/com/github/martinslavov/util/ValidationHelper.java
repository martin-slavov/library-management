package com.github.martinslavov.util;

import java.util.regex.Pattern;

public class ValidationHelper {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9\\s-]{7,14}$");

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
}