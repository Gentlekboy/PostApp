package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.api

import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import retrofit2.Response
import retrofit2.http.GET

interface SimpleApi  {
    @GET("posts")
    suspend fun getPost(): Response<Posts>
}