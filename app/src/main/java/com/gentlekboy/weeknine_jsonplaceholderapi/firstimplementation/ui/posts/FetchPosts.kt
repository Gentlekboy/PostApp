package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui.posts

import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.PostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems

object FetchPosts {
    //This function filters posts based on user's query in the search view
    fun filterPosts(
        searchView: SearchView,
        inputMethodManager: InputMethodManager,
        listOfPosts: MutableList<PostItems>,
        copyOfListOfPosts: MutableList<PostItems>,
        postAdapter: PostAdapter
    ){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
                return true
            }

            //Filters posts as user types in the search view
            override fun onQueryTextChange(newText: String?): Boolean {
                listOfPosts.clear()
                val searchText = newText?.lowercase()?.trim()

                if (searchText != null) {
                    if (searchText.isNotEmpty()){
                        copyOfListOfPosts.forEach {
                            if (it.body.lowercase().contains(searchText)){
                                listOfPosts.add(it)
                            }
                        }

                        postAdapter.notifyDataSetChanged()
                    }else{
                        listOfPosts.clear()
                        listOfPosts.addAll(copyOfListOfPosts)
                        postAdapter.notifyDataSetChanged()
                    }
                }
                return true
            }
        })
    }
}