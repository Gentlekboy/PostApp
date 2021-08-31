package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems

class PostAdapter(private var context: Context, private var onclickPostItem: OnclickPostItem): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var listOfPosts: ArrayList<PostItems> = ArrayList()

    //View Holder
    inner class PostViewHolder(viewItems: View): RecyclerView.ViewHolder(viewItems){
        val postBody = viewItems.findViewById<TextView>(R.id.post_body)
        val profileImage = viewItems.findViewById<ImageView>(R.id.profile_image)
        val profileName = viewItems.findViewById<TextView>(R.id.profile_name)
        val bio = viewItems.findViewById<TextView>(R.id.profile_bio)
    }

    //Add posts to list
    fun addPosts(posts: List<PostItems>?) {
        if (posts != null) {
            listOfPosts.clear()
            listOfPosts.addAll(posts)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val views = LayoutInflater.from(parent.context).inflate(R.layout.posts_view_holder, parent, false)
        return PostViewHolder(views)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postPosition = listOfPosts[position]
        val id = postPosition.id
        val userId = postPosition.userId

        holder.postBody.text = postPosition.body

        if (postPosition.userId % 2 == 0 || postPosition.userId % 7 == 0){
            holder.profileImage.setImageResource(R.drawable.chinenye)
            holder.profileName.text = context.getString(R.string.chinenye)
            holder.bio.text = context.getString(R.string.chinenye_bio)
        }else{
            holder.profileImage.setImageResource(R.drawable.my_image)
            holder.profileName.text = context.getString(R.string.kufre_udoh)
            holder.bio.text = context.getString(R.string.software_engineer_at_google)
        }

        //Set onclick listener on item view
        holder.itemView.setOnClickListener {
            onclickPostItem.clickPostItem(position, id, userId)
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }
}