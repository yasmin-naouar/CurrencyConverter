package com.example.currencyconverter

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // View Binding pour accéder facilement à tous les composants
    private lateinit var binding: ActivityMainBinding

    // Taux de change fixes exprimés pour 1 EUR (Pivot)
    private val taux = mapOf(
        "EUR" to 1.0,
        "USD" to 1.17,
        "TND" to 3.40,
        "GBP" to 0.87,
        "JPY" to 172.50,
        "CAD" to 1.62
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Initialisation des Spinners avec la liste des devises
        configurerSpinners()

        // 2. Configuration du clavier numérique personnalisé
        configurerClavier()

        // 3. Gestionnaire du bouton CONVERTIR
        binding.btnConvertir.setOnClickListener {
            lancerConversion()
        }

        // 4. Gestionnaire du bouton Inverser
        binding.btnInverser.setOnClickListener {
            inverserDevises()
        }
    }

    /**
     * Remplit les deux Spinner à partir du tableau R.array.devises
     * et définit USD comme source et EUR comme cible par défaut.
     */
    private fun configurerSpinners() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.devises,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spDeviseSource.adapter = adapter
        binding.spDeviseCible.adapter = adapter

        // Sélection par défaut : USD en source et EUR en cible
        val devisesArray = resources.getStringArray(R.array.devises)
        val indexUSD = devisesArray.indexOf("USD")
        val indexEUR = devisesArray.indexOf("EUR")

        if (indexUSD != -1) binding.spDeviseSource.setSelection(indexUSD)
        if (indexEUR != -1) binding.spDeviseCible.setSelection(indexEUR)

        // Bonus : actualisation automatique si une conversion est déjà affichée
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (binding.tvResultat.text.isNotEmpty()) {
                    lancerConversion(afficherToastErreur = false)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spDeviseSource.onItemSelectedListener = spinnerListener
        binding.spDeviseCible.onItemSelectedListener = spinnerListener
    }

    /**
     * Attache les écouteurs de clic sur toutes les touches du TableLayout.
     */
    private fun configurerClavier() {
        binding.btn0.setOnClickListener { ajouterCaractere("0") }
        binding.btn1.setOnClickListener { ajouterCaractere("1") }
        binding.btn2.setOnClickListener { ajouterCaractere("2") }
        binding.btn3.setOnClickListener { ajouterCaractere("3") }
        binding.btn4.setOnClickListener { ajouterCaractere("4") }
        binding.btn5.setOnClickListener { ajouterCaractere("5") }
        binding.btn6.setOnClickListener { ajouterCaractere("6") }
        binding.btn7.setOnClickListener { ajouterCaractere("7") }
        binding.btn8.setOnClickListener { ajouterCaractere("8") }
        binding.btn9.setOnClickListener { ajouterCaractere("9") }
        binding.btnPoint.setOnClickListener { ajouterCaractere(".") }

        // Touche C : réinitialise à 0 et vide le résultat
        binding.btnC.setOnClickListener {
            binding.tvMontant.text = getString(R.string.valeur_initiale_montant)
            binding.tvResultat.text = ""
        }
    }

    /**
     * Ajoute un caractère à la saisie en appliquant les règles de gestion :
     * - Remplacement du 0 initial par le premier chiffre tapé
     * - Un seul point décimal autorisé
     * - Longueur maximale limitée à 10 caractères
     */
    private fun ajouterCaractere(c: String) {
        val texteActuel = binding.tvMontant.text.toString()

        // Contrainte : longueur max 10 caractères
        if (texteActuel.length >= 10 && texteActuel != "0") {
            return
        }

        // Contrainte : un seul point décimal
        if (c == "." && texteActuel.contains(".")) {
            return
        }

        val nouveauTexte = if (texteActuel == "0" && c != ".") {
            c
        } else {
            texteActuel + c
        }

        binding.tvMontant.text = nouveauTexte

        // Si un résultat était déjà calculé, actualisation dynamique
        if (binding.tvResultat.text.isNotEmpty()) {
            lancerConversion(afficherToastErreur = false)
        }
    }

    /**
     * Exécute le calcul de conversion et formate le résultat à 2 décimales.
     */
    private fun lancerConversion(afficherToastErreur: Boolean = true) {
        val texteMontant = binding.tvMontant.text.toString()
        val montant = texteMontant.toDoubleOrNull() ?: 0.0

        // Règle de gestion : si le montant vaut 0, Toast d'avertissement
        if (montant == 0.0) {
            if (afficherToastErreur) {
                Toast.makeText(this, getString(R.string.msg_erreur_montant_zero), Toast.LENGTH_SHORT).show()
            }
            binding.tvResultat.text = ""
            return
        }

        val source = binding.spDeviseSource.selectedItem.toString()
        val cible = binding.spDeviseCible.selectedItem.toString()

        val resultat = convertir(montant, source, cible)
        binding.tvResultat.text = String.format(Locale.US, "%.2f", resultat)
    }

    /**
     * Calcule la conversion en passant par l'Euro comme devise pivot :
     * - montant en EUR = montant / taux(source)
     * - résultat = montant en EUR * taux(cible)
     */
    private fun convertir(montant: Double, source: String, cible: String): Double {
        if (source == cible) return montant

        val tauxSource = taux[source] ?: 1.0
        val tauxCible = taux[cible] ?: 1.0

        val montantEnEUR = montant / tauxSource
        return montantEnEUR * tauxCible
    }

    /**
     * Échange les sélections des deux Spinner et relance la conversion si un résultat est visible.
     */
    private fun inverserDevises() {
        val posSource = binding.spDeviseSource.selectedItemPosition
        val posCible = binding.spDeviseCible.selectedItemPosition

        binding.spDeviseSource.setSelection(posCible)
        binding.spDeviseCible.setSelection(posSource)

        if (binding.tvResultat.text.isNotEmpty()) {
            lancerConversion(afficherToastErreur = false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_MONTANT, binding.tvMontant.text.toString())
        outState.putString(KEY_RESULTAT, binding.tvResultat.text.toString())
        outState.putInt(KEY_SPINNER_SOURCE, binding.spDeviseSource.selectedItemPosition)
        outState.putInt(KEY_SPINNER_CIBLE, binding.spDeviseCible.selectedItemPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.tvMontant.text = savedInstanceState.getString(KEY_MONTANT, "0")
        binding.tvResultat.text = savedInstanceState.getString(KEY_RESULTAT, "")
        binding.spDeviseSource.setSelection(savedInstanceState.getInt(KEY_SPINNER_SOURCE, 0))
        binding.spDeviseCible.setSelection(savedInstanceState.getInt(KEY_SPINNER_CIBLE, 1))
    }

    companion object {
        private const val KEY_MONTANT = "key_montant"
        private const val KEY_RESULTAT = "key_resultat"
        private const val KEY_SPINNER_SOURCE = "key_spinner_source"
        private const val KEY_SPINNER_CIBLE = "key_spinner_cible"
    }
}
