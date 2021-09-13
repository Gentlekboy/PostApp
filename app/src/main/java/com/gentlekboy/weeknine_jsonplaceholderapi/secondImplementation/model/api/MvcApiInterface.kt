package com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.api

import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.comments.MvcComments
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.posts.MvcPostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.posts.MvcPosts
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MvcApiInterface {
    //Get request for posts
    @GET("posts")
    fun getPost(): Call<MvcPosts>

    //Get request for comments
    @GET("posts/{id}/comments")
    fun getComments(
        @Path("id")
        id: String
    ): Call<MvcComments>

    //Make post request as JSON format
    @POST("posts")
    fun makeAPost(
        @Body
        post: MvcPostItems
    ): Call<MvcPostItems>
}