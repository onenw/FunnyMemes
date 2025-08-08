package com.william.funnymemes.data

import com.william.funnymemes.model.AllMemeData
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
        @GET("get_memes")
        suspend fun getMemesList() : Response<AllMemeData>
    }
