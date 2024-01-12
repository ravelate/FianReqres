package com.C22PS320.Akrab.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val TOKEN = stringPreferencesKey("token")

    suspend fun getUserToken(): String? {
        return dataStore.data.map {
            it[TOKEN]
        }.first()
    }



    suspend fun SaveUserSession(token: String) {
        dataStore.edit {
            it[TOKEN] = token
        }
    }
    suspend fun deleteAllData(){
        dataStore.edit {
            it.clear()
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}