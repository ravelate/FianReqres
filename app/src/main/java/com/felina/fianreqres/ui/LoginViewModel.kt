package com.felina.fianreqres.ui

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.C22PS320.Akrab.preferences.SettingPreferences
import com.felina.fianreqres.adapter.Event
import com.felina.fianreqres.network.ApiConfig
import com.felina.fianreqres.network.AuthResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _login = MutableLiveData<AuthResponse>()
    val login: LiveData<AuthResponse> = _login

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun saveUserSession(token: String) {
        viewModelScope.launch {
            pref.SaveUserSession(token)
        }
    }
    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                _isLoading.value =false
                if (response.isSuccessful && response.code() == 200) {
                    _login.value = response.body()
                    _snackbarText.value = Event("Login Success: ${response.body()?.token.toString()}")
                } else if (response.code() == 400){
                    _snackbarText.value = Event("User not found")
                    Log.e(ContentValues.TAG, "onFailure but response: ${response.raw()}")
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}