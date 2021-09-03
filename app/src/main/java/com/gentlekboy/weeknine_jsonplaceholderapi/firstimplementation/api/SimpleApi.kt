package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.api

import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.comments.Comments
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi  {
    @GET("posts")
    suspend fun getPost(): Response<Posts>

    @GET("posts/{id}/comments")
    suspend fun getComment(
        @Path("id")
        id: String
    ): Response<Comments>

    //Make post request as JSON format
    @POST("posts")
    suspend fun pushPost(
        @Body
        post: PostItems
    ): Response<PostItems>

    //Make post request as form URL encoded format
    @FormUrlEncoded
    @POST("posts")
    suspend fun pushPost2(
        @Field("userId")
        userId: Int,
        @Field("id")
        id: Int,
        @Field("title")
        title: String,
        @Field("body")
        body: String,
    ): Response<PostItems>
}