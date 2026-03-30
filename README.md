# LAB 14 : Sauvegarde des données – SharedPreferences et fichiers (avec bonnes pratiques de sécurité)

## Objectifs d’apprentissage  

Écrire et lire des préférences via SharedPreferences (apply vs commit).  

Stocker un secret (token) chiffré via EncryptedSharedPreferences + MasterKey.  

Écrire et lire des fichiers internes (texte UTF-8, JSON simple).  

  
Utiliser un cache temporaire (cacheDir) et le purger.  

Exporter un fichier vers l’externe app-specific et comprendre les permissions.  

Appliquer une checklist sécurité (logs, MODE_PRIVATE, nettoyage, rotation conceptuelle de token).  

# RESULTAT FINAL:  

<img width="409" height="857" alt="image" src="https://github.com/user-attachments/assets/c2d01c7e-b483-4f0c-b27f-0a220bc182b8" />    

## Architecture cible  

Packages recommandés :  

`com.example.securestoragejava.ui  `

`com.example.securestoragejava.prefs  `

`com.example.securestoragejava.files  `

`com.example.securestoragejava.cache  `

`com.example.securestoragejava.external  `

  
`com.example.securestoragejava.model`

















# SecureStorageLabJava - Persistance et Sécurité Android

Ce projet est un laboratoire pratique sur la mise en œuvre de diverses méthodes de persistance de données locales sous Android en utilisant le langage Java. L'accent est mis sur la sécurité des données sensibles et la gestion professionnelle des fichiers.

## 📺 Démonstration Vidéo
> **Note :** Si vous consultez ce projet sur GitHub, vous pouvez visionner la démonstration de l'application ici :
> [**Cliquez ici pour voir la vidéo de démonstration**](media/demo_lab.mp4)

*(Si la vidéo ne s'ouvre pas directement, elle se trouve dans le dossier `/media` du dépôt)*.

## 🎯 Objectifs d'apprentissage
*   Maîtriser les **SharedPreferences** pour les réglages utilisateur.
*   Sécuriser les secrets (tokens) via **EncryptedSharedPreferences** et **MasterKey**.
*   Manipuler le **Stockage Interne** pour des fichiers texte et JSON (encodage UTF-8).
*   Gérer le **Cache** de l'application pour les données temporaires.
*   Utiliser le **Stockage Externe (App-Specific)** pour l'exportation contrôlée.
*   Appliquer les meilleures pratiques de sécurité (logs contrôlés, masquage de données).

## 📂 Architecture du Projet
Le projet est structuré de manière modulaire pour séparer les responsabilités :

*   `data.model` : Contient les entités de données (`UserRecord`).
*   `data.prefs` : Gestionnaires de préférences (`AppPreferencesManager`, `SecureVault`).
*   `data.files` : Classes utilitaires pour les fichiers (`InternalFileHandler`, `CacheManager`, `ExternalStorageManager`).
*   `ui` : Activité principale et interface utilisateur.

## 💾 Emplacements de Stockage (Livrables)
Voici les chemins exacts pour vérifier les données via le **Device File Explorer** :

| Type de Données | Fichier / Dossier | Chemin Absolu |
| :--- | :--- | :--- |
| **Préférences Standard** | `user_settings_prefs.xml` | `/data/data/com.example.lab14dev/shared_prefs/` |
| **Préférences Chiffrées** | `sensitive_data_vault.xml` | `/data/data/com.example.lab14dev/shared_prefs/` |
| **Données JSON (Interne)** | `data_export.json` | `/data/data/com.example.lab14dev/files/` |
| **Cache Temporaire** | `session_checkpoint.txt` | `/data/data/com.example.lab14dev/cache/` |
| **Export Externe** | `public_export.txt` | `/storage/emulated/0/Android/data/com.example.lab14dev/files/` |

## 🛡️ Checklist Sécurité (Implémentée)
1.  ✅ **Aucun secret dans Logcat** : Seule la longueur ou la présence du token est logguée.
2.  ✅ **Chiffrement AES-256** : Utilisation de `MasterKey` pour les données sensibles.
3.  ✅ **MODE_PRIVATE** : Tous les fichiers internes sont créés avec un accès restreint.
4.  ✅ **Input Masking** : Le champ Token utilise `textPassword` pour ne pas être lu par-dessus l'épaule.
5.  ✅ **Nettoyage Complet** : Une fonction "Tout Effacer" purge toutes les sources de données.
6.  ✅ **Encodage Robuste** : Utilisation systématique de `StandardCharsets.UTF_8`.

## 🚀 Installation et Test
1.  Cloner le projet ou l'ouvrir dans **Android Studio**.
2.  Synchroniser Gradle (nécessite `androidx.security:security-crypto`).
3.  Lancer l'application sur un appareil **API 24+**.
4.  Utiliser les boutons :
    *   **Enregistrer** : Stocke vos réglages et le token secret.
    *   **Charger** : Restaure les données après un redémarrage.
    *   **Exporter/Importer JSON** : Teste la manipulation de fichiers structurés.
    *   **Tout Effacer** : Réinitialise l'application proprement.

---
*Réalisé dans le cadre du Lab de Développement Mobile - Persistance Locale.*



