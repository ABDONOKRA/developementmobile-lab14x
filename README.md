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


