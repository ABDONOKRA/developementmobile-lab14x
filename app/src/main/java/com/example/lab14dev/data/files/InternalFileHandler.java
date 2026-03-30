package com.example.lab14dev.data.files;

import android.content.Context;

import com.example.lab14dev.data.model.UserRecord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère la lecture et l'écriture de fichiers dans le stockage interne de l'application.
 * Compatible API 24+.
 */
public final class InternalFileHandler {

    private InternalFileHandler() {}

    /**
     * Écrit du texte brut en UTF-8.
     */
    public static void writePlainText(Context context, String filename, String data) throws Exception {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Lit du texte brut (Compatible API 24).
     */
    public static String readPlainText(Context context, String filename) throws Exception {
        try (FileInputStream fis = context.openFileInput(filename)) {
            byte[] buffer = readAllBytesCompat(fis);
            return new String(buffer, StandardCharsets.UTF_8);
        }
    }

    /**
     * Alternative à InputStream.readAllBytes() pour API < 33.
     */
    private static byte[] readAllBytesCompat(InputStream is) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    /**
     * Sauvegarde une liste d'enregistrements au format JSON.
     */
    public static void saveUserRecords(Context context, String filename, List<UserRecord> records) throws Exception {
        JSONArray array = new JSONArray();
        for (UserRecord r : records) {
            JSONObject obj = new JSONObject();
            obj.put("id", r.getId());
            obj.put("name", r.getFullName());
            obj.put("age", r.getAgeValue());
            array.put(obj);
        }
        writePlainText(context, filename, array.toString());
    }

    /**
     * Charge une liste d'enregistrements depuis un fichier JSON.
     */
    public static List<UserRecord> loadUserRecords(Context context, String filename) {
        List<UserRecord> list = new ArrayList<>();
        try {
            String json = readPlainText(context, filename);
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(new UserRecord(
                        obj.getInt("id"),
                        obj.getString("name"),
                        obj.getInt("age")
                ));
            }
        } catch (Exception ignored) {
            // Retourne une liste vide si le fichier n'existe pas ou est corrompu
        }
        return list;
    }

    public static boolean deleteFile(Context context, String filename) {
        return context.deleteFile(filename);
    }
}
