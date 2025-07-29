package com.example.lab8

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(private val repository: DataStoreRepository) : ViewModel() {

    val businessCard = repository.getCard
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            BusinessCard()
        )

    fun saveCard(card: BusinessCard) {
        viewModelScope.launch {
            repository.saveCard(card)
        }
    }
}