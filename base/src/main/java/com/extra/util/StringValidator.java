package com.extra.util;

import android.util.Patterns;

public class StringValidator {

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 5;
    }

    public static boolean isLoginPasswordValid(String password) {
        return password.length() >= 2;
    }

    public static boolean isCellPhoneValid(String cellNumber) {
        return cellNumber.matches("^9\\d{9}$");
    }

    public static boolean isPostalCodeValid(String postalCode) {
        return postalCode.length() == 10;
    }

    public static boolean isWebsiteValid(String website) {
        return Patterns.WEB_URL.matcher(website).matches();
    }

}

