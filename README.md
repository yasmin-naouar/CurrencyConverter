# Currency Converter — TP Android Kotlin

Application Android native de conversion de devises à partir de taux de change fixes, développée en **Kotlin** avec les interfaces **XML**, le **View Binding**, un `ConstraintLayout` principal et un `TableLayout` pour le clavier numérique virtuel.

## 📱 Fonctionnalités
- Zone supérieure turquoise (`#2BB8E8`) contenant les deux listes déroulantes (`Spinner`), l'affichage du montant, l'affichage du résultat formaté à deux décimales et le bouton d'inversion.
- Clavier numérique personnalisé en `TableLayout` (chiffres en `#1BA6C9`, touche C en `#F0727A`).
- Aucune apparition du clavier système : l'ensemble des entrées s'effectue via le clavier de l'application.
- Remplacement automatique du "0" initial, limitation à 10 caractères et restriction à un seul point décimal.
- Conversion basée sur l'Euro (EUR) comme devise pivot :
  - EUR : 1.00
  - USD : 1.17
  - TND : 3.40
  - GBP : 0.87
  - JPY : 172.50
  - CAD : 1.62
- Bouton d'inversion échangeant instantanément les devises et recalculant le montant.
- Gestion des erreurs : affichage d'un Toast *"Veuillez saisir un montant"* si le montant vaut 0.
- Orientation verrouillée en mode portrait (`screenOrientation="portrait"`).

---

## 📂 Structure du Répertoire
```
CurrencyConverter/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/currencyconverter/
│   │   │   └── MainActivity.kt
│   │   └── res/
│   │       ├── drawable/ (ic_swap.xml, ...)
│   │       ├── layout/activity_main.xml
│   │       └── values/ (strings.xml, colors.xml, themes.xml)
│   └── build.gradle.kts
├── screenshots/
│   ├── 0_demarrage.png
│   ├── 1_test_120_USD_EUR.png
│   ├── 2_test_100_EUR_TND.png
│   ├── 3_test_50_GBP_USD.png
│   ├── 4_test_10_EUR_EUR.png
│   ├── 5_inversion_devises.png
│   └── 6_toast_erreur_zero.png
├── compte-rendu/
│   ├── compte-rendu.md
│   └── compte-rendu.pdf
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🧪 Cas de Test Validés

| Montant | Source | Cible | Résultat Attendu & Obtenu |
| :---: | :---: | :---: | :---: |
| 120 | USD | EUR | **102.56** |
| 100 | EUR | TND | **340.00** |
| 50 | GBP | USD | **67.24** |
| 10 | EUR | EUR | **10.00** |

---

## 🚀 Compilation et Exécution

1. Ouvrir le dossier `CurrencyConverter` dans **Android Studio**.
2. Lancer la synchronisation Gradle.
3. Exécuter l'application sur émulateur ou appareil connecté :
   ```bash
   ./gradlew assembleDebug
   ```

---

## 👨‍💻 Auteur
- **Adem Ouerhani**
- Dépôt : [https://github.com/ADEM2005373/CurrencyConverter](https://github.com/ADEM2005373/CurrencyConverter)
