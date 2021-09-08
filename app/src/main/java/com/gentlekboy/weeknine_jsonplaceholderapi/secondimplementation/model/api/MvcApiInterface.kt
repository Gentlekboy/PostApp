package com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.api

import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.data.posts.MvcPosts
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MvcApiInterface {
    @GET("posts")
    fun getPost(): Call<MvcPosts>
}