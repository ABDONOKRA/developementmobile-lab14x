package com.example.lab14dev.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Gère le stockage sécurisé des jetons (tokens).
 */
public final class SecureVault {

    private static final String SECURE_FILE = "sensitive_data_vault";
    private static final String KEY_AUTH_TOKEN = "auth_token_secret";

    private SecureVault() {}

    private static SharedPreferences getSecurePrefs(Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                SECURE_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void storeToken(Context context, String token) throws GeneralSecurityException, IOException {
        // Sécurité : Ne jamais logger le contenu du token
        getSecurePrefs(context).edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    public static String retrieveToken(Context context) throws GeneralSecurityException, IOException {
        return getSecurePrefs(context).getString(KEY_AUTH_TOKEN, "");
    }

    public static void clearVault(Context context) throws GeneralSecurityException, IOException {
        getSecurePrefs(context).edit().clear().apply();
    }
}
