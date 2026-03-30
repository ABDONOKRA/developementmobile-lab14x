package com.example.lab14dev.data.files;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Gère les fichiers temporaires dans le répertoire de cache.
 */
public final class CacheManager {

    private CacheManager() {}

    /**
     * Enregistre un contenu temporaire dans le cache.
     */
    public static void saveTemporaryData(Context context, String filename, String content) throws IOException {
        File cacheFile = new File(context.getCacheDir(), filename);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Files.write(cacheFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        } else {
            // Fallback pour versions plus anciennes si nécessaire, mais minSdk est 24
            java.io.FileOutputStream fos = new java.io.FileOutputStream(cacheFile);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }
    }

    /**
     * Lit un fichier depuis le cache.
     */
    public static String readFromCache(Context context, String filename) throws IOException {
        File cacheFile = new File(context.getCacheDir(), filename);
        if (!cacheFile.exists()) return null;
        
        byte[] encoded = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) 
                ? Files.readAllBytes(cacheFile.toPath())
                : readBytesLegacy(cacheFile);
                
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private static byte[] readBytesLegacy(File file) throws IOException {
        java.io.FileInputStream fis = new java.io.FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return data;
    }

    /**
     * Supprime tous les fichiers du cache.
     */
    public static int clearCache(Context context) {
        File[] files = context.getCacheDir().listFiles();
        if (files == null) return 0;
        int count = 0;
        for (File f : files) {
            if (f.delete()) count++;
        }
        return count;
    }
}
