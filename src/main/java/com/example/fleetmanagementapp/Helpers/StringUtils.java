package com.example.fleetmanagementapp.Helpers;

public class StringUtils {

    // Return the first and last name from a full name string considering the received separator
    public static String getFirstAndLastName(String fullName, String separatorToJoin) {

        // If full name is null or empty, return an empty string
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }

        // Create a text array by splitting the full name with the separator
        String[] textArray = fullName.split(" ");

        // If the array has more than one element, perform operations, otherwise return the first and only element
        if (textArray.length > 1) {
            // If the first element is equal to the last one, just return the first one
            if (textArray[0].equals(textArray[textArray.length - 1])) {
                return textArray[0];
            } else { // As the first and last are different, return a concatenation of both
                return textArray[0] + separatorToJoin + textArray[textArray.length - 1];
            }
        } else {
            return textArray[0];
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
