package com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts

import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.adapter.MvcPostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.posts.MvcPostItems

object MvcSearchViewInPost {
    //This function filters posts based on user's query in the search view
    fun mvcFilterPostsWithSearchView(
        searchView: SearchView,
        inputMethodManager: InputMethodManager,
        listOfPosts: MutableList<MvcPostItems>,
        copyOfListOfPosts: MutableList<MvcPostItems>,
        postAdapter: MvcPostAdapter
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