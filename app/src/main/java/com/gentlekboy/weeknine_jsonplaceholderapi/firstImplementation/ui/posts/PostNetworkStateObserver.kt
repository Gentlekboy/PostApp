package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.utils.ConnectivityLiveData

//This function fetches posts and displays them on the UI
fun observeNetworkChanges(
    context: Context,
    application: Application,
    lifecycleOwner: LifecycleOwner,
    reloadMessage: TextView,
    connectionLostText: TextView,
    connectionLostImage: ImageView,
    progressBar: ProgressBar,
    loadingPosts: TextView,
    reversedListOfPosts: MutableList<PostItems>,
    appName: TextView,
    searchView: SearchView,
    nestedScrollView: NestedScrollView
){
    val connectivityLiveData = ConnectivityLiveData(application)
    connectivityLiveData.observe(lifecycleOwner, { isAvailable ->
        when(isAvailable){
            true -> {
                reloadMessage.visibility = View.GONE
                connectionLostImage.setColorFilter(context.resources.getColor(R.color.blue))
                connectionLostText.text = context.getString(R.string.connection_restored)

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    progressBar.visibility = View.VISIBLE
                    loadingPosts.visibility = View.VISIBLE
                    connectionLostImage.visibility = View.GONE
                    connectionLostText.visibility = View.GONE
                }, 800)

                handler.postDelayed({
                    if (reversedListOfPosts.isNotEmpty()){
                        progressBar.visibility = View.GONE
                        loadingPosts.visibility = View.GONE

                        appName.visibility = View.VISIBLE
                        searchView.visibility = View.VISIBLE
                        nestedScrollView.visibility = View.VISIBLE
                    }
                }, 1500)
            }
            false -> {
                connectionLostImage.setColorFilter(context.resources.getColor(R.color.gray))
                connectionLostText.text = context.getString(R.string.connection_lost)

                appName.visibility = View.GONE
                searchView.visibility = View.GONE
                nestedScrollView.visibility = View.GONE

                connectionLostImage.visibility = View.VISIBLE
                connectionLostText.visibility = View.VISIBLE
                reloadMessage.visibility = View.VISIBLE

                Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    })
}