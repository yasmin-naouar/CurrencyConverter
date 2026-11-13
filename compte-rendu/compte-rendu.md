# Compte Rendu — TP Android Kotlin : Convertisseur de Devises

**Auteur / Étudiant :** Adem Ouerhani  
**Module :** Développement d'Applications Mobiles Android (Kotlin & XML)  
**Projet :** Currency Converter (Convertisseur de Devises)  
**Dépôt GitHub :** https://github.com/ADEM2005373/CurrencyConverter  

---

## 1. Introduction et Objectifs

L'objectif de ce travail pratique est de concevoir et développer une application mobile native sous Android intitulée **« Currency Converter »** en langage Kotlin avec les interfaces déclaratives XML. L'application assure la conversion bidirectionnelle de montants monétaires entre devises internationales majeures à partir d'une grille de taux de change fixes basée sur l'Euro comme devise pivot.

### Compétences et notions mises en œuvre :
* Structuration d'une interface responsive et équilibrée combinant un `ConstraintLayout` principal et un `TableLayout` pour le clavier tactile.
* Exploitation moderne du **View Binding** pour manipuler les vues de manière sûre et typée (`buildFeatures { viewBinding = true }`).
* Alimentation dynamique de listes déroulantes (`Spinner`) via `ArrayAdapter` et ressources XML (`R.array.devises`).
* Implémentation d'un clavier numérique virtuel sur-mesure évitant l'apparition du clavier système.
* Application rigoureuse des règles de gestion financière et formatage d'arrondi à deux décimales (`String.format("%.2f", ...)`).
* Gestion des exceptions et des erreurs de saisie avec messages contextuels `Toast`.
* Verrouillage de l'orientation en mode portrait (`android:screenOrientation="portrait"`).

---

## 2. Choix d'Interface et Maquettage

L'interface utilisateur (`activity_main.xml`) respecte fidèlement les spécifications ergonomiques de la maquette :

```
+------------------------------------------+
|            Currency Converter            |
|                                          |
|   USD v                            120   |
|                                  ------- |
|    ⇅                                     |
|                                          |
|   EUR v                         102.56   |
|                                  ------- |
|               [ CONVERTIR ]              |
+------------------------------------------+
|       7            8            9        |
|       4            5            6        |
|       1            2            3        |
|       C            0            .        |
+------------------------------------------+
```

### 2.1 Zone supérieure (~45% de l'écran)
* **Arrière-plan :** Fond bleu turquoise uni (`#2BB8E8`).
* **Titre :** `tvTitre` centré, texte blanc, 20sp, en gras.
* **Sélecteurs de devises :** Deux composants `Spinner` (`spDeviseSource` et `spDeviseCible`) pré-positionnés respectivement sur **USD** et **EUR**.
* **Montant saisi (`tvMontant`) :** Aligné à droite, texte 48sp, avec trait de soulignement (`ligneMontant`).
* **Bouton d'inversion (`btnInverser`) :** Bouton icône discret permettant de permuter instantanément la source et la cible.
* **Résultat (`tvResultat`) :** Aligné à droite, texte 48sp, souligné, affichant la valeur convertie.
* **Bouton d'action (`btnConvertir`) :** Bouton arrondi (`app:cornerRadius="24dp"`) sur fond bleu clair (`#90CAF9`) avec le libellé contrasté `CONVERTIR`.

### 2.2 Zone inférieure (~55% de l'écran)
* **Disposition :** `TableLayout` de 4 lignes (`TableRow`) par 3 colonnes configuré avec `android:stretchColumns="*"`.
* **Touches numériques (0 à 9) et point :** Boutons sans bordure (`?android:attr/borderlessButtonStyle`) en typographie turquoise `#1BA6C9` de 32sp.
* **Touche d'effacement « C » :** Mise en évidence en rouge clair (`#F0727A`).

---

## 3. Logique Métier et Règles de Gestion

### 3.1 Taux de change et algorithme de conversion
Les taux fixes sont indexés par rapport à 1 Euro (EUR) :
* Euro (EUR) : `1.00`
* Dollar américain (USD) : `1.17`
* Dinar tunisien (TND) : `3.40`
* Livre sterling (GBP) : `0.87`
* Yen japonais (JPY) : `172.50`
* Dollar canadien (CAD) : `1.62`

```kotlin
private val taux = mapOf(
    "EUR" to 1.0, "USD" to 1.17, "TND" to 3.40,
    "GBP" to 0.87, "JPY" to 172.50, "CAD" to 1.62
)
```

La conversion repose sur le pivot EUR :
$$\text{Montant}_{\text{EUR}} = \frac{\text{Montant}}{\text{Taux}(\text{Source})}$$
$$\text{Résultat} = \text{Montant}_{\text{EUR}} \times \text{Taux}(\text{Cible})$$

Si la source et la cible sont identiques, l'application renvoie directement le montant sans recalcul.

### 3.2 Contrôle de la saisie
* **Montant initial :** Initialisé à `"0"`. La frappe d'un premier chiffre (hors point) remplace immédiatement le zéro.
* **Point décimal unique :** Si la saisie contient déjà un point, tout appui supplémentaire sur la touche `.` est ignoré.
* **Longueur maximale :** La chaîne saisie est bornée à 10 caractères pour éviter tout débordement d'affichage.
* **Touche « C » :** Réinitialise le montant à `"0"` et efface la zone de résultat.
* **Validation zéro :** Si le montant est nul (`0` ou vide), le clic sur `CONVERTIR` affiche le Toast : *« Veuillez saisir un montant »*.

---

## 4. Tests et Résultats de Validation

Les 4 cas de test obligatoires de l'énoncé ont été validés avec exactitude :

| Cas | Montant | Devise Source | Devise Cible | Calcul Théorique | Résultat Attendu | Résultat Obtenu | Statut |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **1** | 120 | USD | EUR | $(120 / 1.17) \times 1.0$ | **102.56** | 102.56 | Conforme |
| **2** | 100 | EUR | TND | $(100 / 1.0) \times 3.40$ | **340.00** | 340.00 | Conforme |
| **3** | 50 | GBP | USD | $(50 / 0.87) \times 1.17$ | **67.24** | 67.24 | Conforme |
| **4** | 10 | EUR | EUR | $10 \times 1.0$ | **10.00** | 10.00 | Conforme |

### Tests des fonctionnalités complémentaires :
* **Bouton Inverser :** La permutation de USD $\leftrightarrow$ EUR inverse les sélections et recalcul immédiatement la valeur correspondante.
* **Erreur montant zéro :** Affichage du Toast attendu sans exception.

---

## 5. Difficultés Rencontrées et Solutions

1. **Intégration du clavier sans bordure dans TableLayout** :
   * *Difficulté :* Ajuster les proportions des boutons pour occuper équitablement la hauteur de chaque ligne.
   * *Solution :* Utilisation de `android:layout_weight="1"` sur chaque `TableRow` et `android:stretchColumns="*"`.
2. **Synchronisation dynamique de l'inversion** :
   * *Difficulté :* Éviter de déclencher des boucles infinies lors de la modification programmatique des sélections de `Spinner`.
   * *Solution :* Distinction entre la réinitialisation manuelle et le déclenchement contrôlé de `lancerConversion`.

---

## 6. Captures d'écran

Toutes les captures d'écran de l'application sont classées dans le dossier `screenshots/` (et dupliquées dans `captures/`) :
* `0_demarrage.png` : Écran initial au lancement de l'application.
* `1_test_120_USD_EUR.png` : Cas 1 (120 USD $\rightarrow$ EUR = 102.56).
* `2_test_100_EUR_TND.png` : Cas 2 (100 EUR $\rightarrow$ TND = 340.00).
* `3_test_50_GBP_USD.png` : Cas 3 (50 GBP $\rightarrow$ USD = 67.24).
* `4_test_10_EUR_EUR.png` : Cas 4 (10 EUR $\rightarrow$ EUR = 10.00).
* `5_inversion_devises.png` : Inversion des devises avec conversion instantanée.
* `6_toast_erreur_zero.png` : Gestion de l'erreur lorsque le montant vaut 0.
