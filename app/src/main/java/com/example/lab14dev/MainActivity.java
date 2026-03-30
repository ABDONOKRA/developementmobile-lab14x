package com.example.lab14dev;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab14dev.data.files.CacheManager;
import com.example.lab14dev.data.files.ExternalStorageManager;
import com.example.lab14dev.data.files.InternalFileHandler;
import com.example.lab14dev.data.model.UserRecord;
import com.example.lab14dev.data.prefs.AppPreferencesManager;
import com.example.lab14dev.data.prefs.SecureVault;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "StoreApp_Main";
    private final List<String> availableLanguages = Arrays.asList("Français", "English", "Español", "Deutsch");

    private TextInputEditText inputName;
    private TextInputEditText inputToken;
    private Spinner spinnerLanguage;
    private SwitchMaterial switchTheme;
    private TextView labelStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        inputName = findViewById(R.id.inputName);
        inputToken = findViewById(R.id.inputToken);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        switchTheme = findViewById(R.id.switchTheme);
        labelStatus = findViewById(R.id.labelStatus);

        setupLanguageOptions();

        MaterialButton btnSave = findViewById(R.id.btnSaveAllSettings);
        MaterialButton btnLoad = findViewById(R.id.btnLoadAllSettings);
        MaterialButton btnExport = findViewById(R.id.btnExportJson);
        MaterialButton btnImport = findViewById(R.id.btnImportJson);
        MaterialButton btnReset = findViewById(R.id.btnResetData);

        btnSave.setOnClickListener(v -> performSaveProcess());
        btnLoad.setOnClickListener(v -> performLoadProcess());
        btnExport.setOnClickListener(v -> performExportProcess());
        btnImport.setOnClickListener(v -> performImportProcess());
        btnReset.setOnClickListener(v -> performFullReset());

        // Chargement initial
        performLoadProcess();
    }

    private void setupLanguageOptions() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_dropdown_item, availableLanguages);
        spinnerLanguage.setAdapter(adapter);
    }

    private void performSaveProcess() {
        String nameValue = inputName.getText() != null ? inputName.getText().toString().trim() : "";
        String langValue = availableLanguages.get(spinnerLanguage.getSelectedItemPosition());
        String themeValue = switchTheme.isChecked() ? "dark" : "light";
        String tokenValue = inputToken.getText() != null ? inputToken.getText().toString() : "";

        // 1. SharedPreferences Classiques
        AppPreferencesManager.persistSettings(this, nameValue, langValue, themeValue);

        // 2. EncryptedSharedPreferences
        if (!tokenValue.isEmpty()) {
            try {
                SecureVault.storeToken(this, tokenValue);
            } catch (Exception e) {
                updateStatus("Erreur Sécurité: " + e.getMessage());
                return;
            }
        }

        // 3. Cache (Données temporaires)
        try {
            CacheManager.saveTemporaryData(this, "session_checkpoint.txt", "User: " + nameValue);
        } catch (Exception ignored) {}

        Log.i(LOG_TAG, "Sauvegarde réussie (Données sensibles masquées)");
        updateStatus("Configuration enregistrée avec succès.\nToken sécurisé.");
    }

    private void performLoadProcess() {
        AppPreferencesManager.UserSettings settings = AppPreferencesManager.fetchSettings(this);
        
        inputName.setText(settings.name);
        switchTheme.setChecked("dark".equals(settings.theme));
        
        int langIndex = availableLanguages.indexOf(settings.language);
        spinnerLanguage.setSelection(langIndex >= 0 ? langIndex : 0);

        int secretLen = 0;
        try {
            String secret = SecureVault.retrieveToken(this);
            secretLen = secret.length();
        } catch (Exception ignored) {}

        StringBuilder sb = new StringBuilder();
        sb.append("Données chargées :\n");
        sb.append(" - Nom: ").append(settings.name).append("\n");
        sb.append(" - Langue: ").append(settings.language).append("\n");
        sb.append(" - Thème: ").append(settings.theme).append("\n");
        sb.append(" - Secret stocké : ").append(secretLen > 0 ? "[PRÉSENT (" + secretLen + " chars)]" : "[AUCUN]");
        
        updateStatus(sb.toString());
    }

    private void performExportProcess() {
        List<UserRecord> dummyData = Arrays.asList(
                new UserRecord(101, "Alice Cooper", 28),
                new UserRecord(102, "Bob Marley", 35)
        );

        try {
            // Stockage interne JSON
            InternalFileHandler.saveUserRecords(this, "data_export.json", dummyData);
            // Stockage externe (App specific)
            String path = ExternalStorageManager.exportData(this, "public_export.txt", "Export du " + System.currentTimeMillis());
            
            updateStatus("Données JSON exportées en interne.\nChemin externe: " + (path != null ? path : "N/A"));
        } catch (Exception e) {
            updateStatus("Erreur export: " + e.getMessage());
        }
    }

    private void performImportProcess() {
        List<UserRecord> records = InternalFileHandler.loadUserRecords(this, "data_export.json");
        
        StringBuilder sb = new StringBuilder("Résultat Import JSON :\n");
        if (records.isEmpty()) {
            sb.append("Aucun enregistrement trouvé.");
        } else {
            for (UserRecord r : records) {
                sb.append(" • ").append(r.toString()).append("\n");
            }
        }
        updateStatus(sb.toString());
    }

    private void performFullReset() {
        try {
            AppPreferencesManager.clearAll(this);
            SecureVault.clearVault(this);
            InternalFileHandler.deleteFile(this, "data_export.json");
            ExternalStorageManager.deleteExport(this, "public_export.txt");
            int cacheFiles = CacheManager.clearCache(this);

            inputName.setText("");
            inputToken.setText("");
            switchTheme.setChecked(false);
            spinnerLanguage.setSelection(0);
            
            updateStatus("Nettoyage complet effectué.\nFichiers cache supprimés: " + cacheFiles);
            Toast.makeText(this, "Toutes les données ont été effacées", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            updateStatus("Erreur lors du nettoyage: " + e.getMessage());
        }
    }

    private void updateStatus(String text) {
        labelStatus.setText("Statut :\n" + text);
    }
}
