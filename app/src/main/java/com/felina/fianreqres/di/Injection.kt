package com.felina.fianreqres.di

import android.content.Context
import com.felina.fianreqres.data.UserRepository
import com.felina.fianreqres.database.UserDatabase
import com.felina.fianreqres.network.ApiConfig
import com.felina.fianreqres.network.ApiService

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = UserDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return UserRepository(database, apiService)
    }
    fun provideApiService(context: Context): ApiService {
        return ApiConfig.getApiService()
    }
}