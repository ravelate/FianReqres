package com.felina.fianreqres.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
   @GET("users")
   suspend fun getUsers(
           @Query("page") page: Int,
           @Query("per_page") size: Int
   ): UserResponse
   @FormUrlEncoded
   @POST("login")
   fun loginUser(
       @Field("email") email: String,
       @Field("password") password: String,
   ): Call<AuthResponse>
}