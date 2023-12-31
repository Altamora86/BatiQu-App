package com.latihan.capstoneproject.api

import com.latihan.capstoneproject.response.LoginResponse
import com.latihan.capstoneproject.response.RegisterResponse
import com.latihan.capstoneproject.response.ResponseSearchDetail
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name : String?,
        @Field("email") email : String?,
        @Field("password") password : String?
    ): Call<RegisterResponse>


    @GET("favourite/{name}")
    fun getnameDetail(
        @Path("name") name: String
    ): Call<ResponseSearchDetail>

}