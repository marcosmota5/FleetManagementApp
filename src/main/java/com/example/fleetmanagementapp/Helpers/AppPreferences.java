package com.example.fleetmanagementapp.Helpers;

import java.util.prefs.Preferences;

public class AppPreferences {

    // Define keys for the preferences
    private static final String PREF_KEY_LOGIN = "savedLogin";
    private static final String PREF_KEY_REMEMBER_ME = "rememberMe";

    // Get preferences for this application node
    private static Preferences preferences = Preferences.userNodeForPackage(Preferences.class);

    // Static method to save login and "Remember Me" state
    public static void savePreferences(String login, boolean rememberMe) {
        preferences.put(PREF_KEY_LOGIN, login);
        preferences.putBoolean(PREF_KEY_REMEMBER_ME, rememberMe);
    }

    // Static method to load login and "Remember Me" state
    public static String[] loadPreferences() {
        String savedLogin = preferences.get(PREF_KEY_LOGIN, "");
        boolean rememberMe = preferences.getBoolean(PREF_KEY_REMEMBER_ME, false);
        return new String[] { savedLogin, Boolean.toString(rememberMe) };
    }

    // Static method to clear the saved preferences
    public static void clearPreferences() {
        preferences.remove(PREF_KEY_LOGIN);
        preferences.remove(PREF_KEY_REMEMBER_ME);
    }
}