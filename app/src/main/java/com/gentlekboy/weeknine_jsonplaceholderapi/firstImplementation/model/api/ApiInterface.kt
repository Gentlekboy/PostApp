package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.api

import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.comments.Comments
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.posts.Posts
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface  {

    //Get request for posts
    @GET("posts")
    suspend fun getPost(): Response<Posts>

    //Get request for comments
    @GET("posts/{id}/comments")
    suspend fun getComment(
        @Path("id")
        id: String
    ): Response<Comments>

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