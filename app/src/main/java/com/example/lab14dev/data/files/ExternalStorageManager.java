package com.example.lab14dev.data.files;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Gère l'exportation de fichiers vers le stockage externe spécifique à l'application.
 */
public final class ExternalStorageManager {

    private ExternalStorageManager() {}

    /**
     * Exporte des données vers un fichier externe (app-specific).
     * @return Le chemin absolu du fichier créé ou null.
     */
    public static String exportData(Context context, String filename, String content) throws IOException {
        File externalDir = context.getExternalFilesDir(null);
        if (externalDir == null) return null;

        File targetFile = new File(externalDir, filename);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.write(targetFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        } else {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(targetFile);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }
        return targetFile.getAbsolutePath();
    }

    public static String readExportedData(Context context, String filename) throws IOException {
        File externalDir = context.getExternalFilesDir(null);
        if (externalDir == null) return null;

        File targetFile = new File(externalDir, filename);
        if (!targetFile.exists()) return null;

        byte[] bytes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bytes = Files.readAllBytes(targetFile.toPath());
        } else {
            java.io.FileInputStream fis = new java.io.FileInputStream(targetFile);
            bytes = new byte[(int) targetFile.length()];
            fis.read(bytes);
            fis.close();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static boolean deleteExport(Context context, String filename) {
        File externalDir = context.getExternalFilesDir(null);
        if (externalDir == null) return false;

        File targetFile = new File(externalDir, filename);
        return targetFile.delete();
    }
}
