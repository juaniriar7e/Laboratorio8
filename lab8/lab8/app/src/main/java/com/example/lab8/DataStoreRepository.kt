package com.example.lab8

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "business_card_prefs")

class DataStoreRepository(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val NAME = stringPreferencesKey("name")
        val POSITION = stringPreferencesKey("position")
        val PHONE = stringPreferencesKey("phone")
        val EMAIL = stringPreferencesKey("email")
        val PROFILE_IMAGE = stringPreferencesKey("profile_image")
    }

    suspend fun saveCard(card: BusinessCard) {
        dataStore.edit { prefs ->
            prefs[NAME] = card.name
            prefs[POSITION] = card.position
            prefs[PHONE] = card.phone
            prefs[EMAIL] = card.email
            prefs[PROFILE_IMAGE] = card.profileImageUri
        }
    }

    val getCard: Flow<BusinessCard> = dataStore.data.map { prefs ->
        BusinessCard(
            name = prefs[NAME] ?: "",
            position = prefs[POSITION] ?: "",
            phone = prefs[PHONE] ?: "",
            email = prefs[EMAIL] ?: "",
            profileImageUri = prefs[PROFILE_IMAGE] ?: ""
        )
    }
}