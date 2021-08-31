package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.api

import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.comments.Comments
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SimpleApi  {
    @GET("posts")
    suspend fun getPost(): Response<Posts>

    @GET("posts/{id}/comments")
    suspend fun getComment(
        @Path("id")
        id: String
    ): Response<Comments>
}