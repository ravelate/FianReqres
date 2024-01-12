package com.felina.fianreqres.network

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
