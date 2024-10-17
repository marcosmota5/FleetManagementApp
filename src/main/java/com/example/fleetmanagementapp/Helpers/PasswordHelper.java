package com.example.fleetmanagementapp.Helpers;

import java.util.regex.Pattern;

public class PasswordHelper {

    public static boolean isStrong(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Check if password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check if password contains at least one digit
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            return false;
        }

        // Check if password contains both lowercase and uppercase letters
        if (!(Pattern.compile("[a-z]").matcher(password).find() &&
                Pattern.compile("[A-Z]").matcher(password).find())) {
            return false;
        }

        // Check if password contains at least one special character
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            return false;
        }

        // If all criteria are met, the password is strong
        return true;
    }

}
