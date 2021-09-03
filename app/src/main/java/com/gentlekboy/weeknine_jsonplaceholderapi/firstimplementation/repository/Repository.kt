package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository

import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.api.RetrofitInstance
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.comments.Comments
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import retrofit2.Response

class Repository {
    suspend fun getPost(): Response<Posts> {
        return RetrofitInstance.api.getPost()
    }

    suspend fun getComment(id: String): Response<Comments>{
        return RetrofitInstance.api.getComment(id)
    }

    suspend fun pushPost(post: PostItems): Response<PostItems>{
        return RetrofitInstance.api.pushPost(post)
    }

    suspend fun pushPost2(userId: Int, id: Int, title: String, body: String): Response<PostItems>{
        return RetrofitInstance.api.pushPost2(userId, id, title, body)
    }
}