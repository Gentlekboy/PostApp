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

class PostAdapter(private var listOfPosts: MutableList<PostItems>, private var context: Context, private var onclickPostItem: OnclickPostItem): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

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

        when(userId){
            1 -> {
                holder.profileImage.setImageResource(R.drawable.user_1)
                holder.profileName.text = "Peter Akam"
                holder.bio.text = "CEO at Facebook"
            }
            2 -> {
                holder.profileImage.setImageResource(R.drawable.user_2)
                holder.profileName.text = "Benjamin Obetta"
                holder.bio.text = "JavaScript Developer"
            }
            3 -> {
                holder.profileImage.setImageResource(R.drawable.user_3)
                holder.profileName.text = "Anthony Idoko"
                holder.bio.text = "Android Developer at Pako.com"
            }
            4 -> {
                holder.profileImage.setImageResource(R.drawable.user_4)
                holder.profileName.text = "Johnson Oyesina"
                holder.bio.text = "DevOps Engineer at LinkedIn"
            }
            5 -> {
                holder.profileImage.setImageResource(R.drawable.user_5)
                holder.profileName.text = "Emmanuel Oruminighen"
                holder.bio.text = "Lead Software Engineer at Google"
            }
            6 -> {
                holder.profileImage.setImageResource(R.drawable.user_6)
                holder.profileName.text = "John Doe"
                holder.bio.text = "AWS Engineer at Microsoft"
            }
            7 -> {
                holder.profileImage.setImageResource(R.drawable.user_7)
                holder.profileName.text = context.getString(R.string.chinenye)
                holder.bio.text = context.getString(R.string.chinenye_bio)
            }
            8 -> {
                holder.profileImage.setImageResource(R.drawable.user_8)
                holder.profileName.text = "Jennifer Santos"
                holder.bio.text = "Junior Web Developer at Tesla"
            }
            9 -> {
                holder.profileImage.setImageResource(R.drawable.user_9)
                holder.profileName.text = "Joe Davids"
                holder.bio.text = "Software Developer | React Native"
            }
            10 -> {
                holder.profileImage.setImageResource(R.drawable.user_10)
                holder.profileName.text = "Cassidy Banks"
                holder.bio.text = "UI/UX Engineer at Flutterwave"
            }
            else -> {
                holder.profileImage.setImageResource(R.drawable.my_image)
                holder.profileName.text = context.getString(R.string.kufre_udoh)
                holder.bio.text = context.getString(R.string.software_engineer_at_google)
            }
        }

        //Set onclick listener on item view
        holder.itemView.setOnClickListener {
            onclickPostItem.navigateToCommentsActivity(position, id, userId)
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }
}