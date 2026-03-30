package com.example.lab14dev.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Gère les préférences utilisateur non sensibles.
 */
public final class AppPreferencesManager {

    private static final String PREF_FILE_NAME = "user_settings_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LANGUAGE_CODE = "lang_code";
    private static final String KEY_THEME_MODE = "ui_theme_mode";

    private AppPreferencesManager() {}

    /**
     * Sauvegarde les paramètres de base.
     */
    public static void persistSettings(@NonNull Context context, String name, String lang, String theme) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_LANGUAGE_CODE, lang);
        editor.putString(KEY_THEME_MODE, theme);
        editor.apply(); // Asynchrone pour la performance UI
    }

    /**
     * Charge les paramètres actuels.
     */
    public static UserSettings fetchSettings(@NonNull Context context) {
        SharedPreferences prefs = getPrefs(context);
        return new UserSettings(
                prefs.getString(KEY_USERNAME, ""),
                prefs.getString(KEY_LANGUAGE_CODE, "fr"),
                prefs.getString(KEY_THEME_MODE, "light")
        );
    }

    public static void clearAll(@NonNull Context context) {
        getPrefs(context).edit().clear().apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Modèle de données pour les réglages.
     */
    public static final class UserSettings {
        public final String name;
        public final String language;
        public final String theme;

        public UserSettings(String name, String language, String theme) {
            this.name = name;
            this.language = language;
            this.theme = theme;
        }
    }
}
