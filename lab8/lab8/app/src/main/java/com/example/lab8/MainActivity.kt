package com.example.lab8

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvPosition: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivProfile: ImageView

    private val viewModel: CardViewModel by viewModels {
        CardViewModelFactory(DataStoreRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        tvName = findViewById(R.id.tvName)
        tvPosition = findViewById(R.id.tvPosition)
        tvPhone = findViewById(R.id.tvPhone)
        tvEmail = findViewById(R.id.tvEmail)
        ivProfile = findViewById(R.id.ivProfile)

        // Cargar imagen de perfil fija
        ivProfile.setImageResource(R.drawable.m12_c26_a7_p23)

        // Configurar observador de datos
        setupObservers()

        // Configurar botón de edición
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            showEditDialog()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.businessCard.collectLatest { card ->
                tvName.text = card.name
                tvPosition.text = card.position
                tvPhone.text = card.phone
                tvEmail.text = card.email

                // Cargar imagen fija (siempre la misma)
                ivProfile.setImageResource(R.drawable.m12_c26_a7_p23)
            }
        }
    }

    private fun showEditDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit, null)

        val etName = dialogView.findViewById<TextInputEditText>(R.id.etName)
        val etPosition = dialogView.findViewById<TextInputEditText>(R.id.etPosition)
        val etPhone = dialogView.findViewById<TextInputEditText>(R.id.etPhone)
        val etEmail = dialogView.findViewById<TextInputEditText>(R.id.etEmail)

        val currentCard = viewModel.businessCard.value
        etName.setText(currentCard.name)
        etPosition.setText(currentCard.position)
        etPhone.setText(currentCard.phone)
        etEmail.setText(currentCard.email)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                viewModel.saveCard(
                    BusinessCard(
                        name = etName.text.toString(),
                        position = etPosition.text.toString(),
                        phone = etPhone.text.toString(),
                        email = etEmail.text.toString(),
                        profileImageUri = "" // Mantenemos vacío porque es imagen fija
                    )
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

// Factory para ViewModel
class CardViewModelFactory(
    private val repository: DataStoreRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}