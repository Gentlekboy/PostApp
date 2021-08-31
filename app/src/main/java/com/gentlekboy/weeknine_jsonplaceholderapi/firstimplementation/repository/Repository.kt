package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository

import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.api.RetrofitInstance
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.Posts
import retrofit2.Response

class Repository {
    suspend fun getPost(): Response<Posts> {
        return RetrofitInstance.api.getPost()
    }
}