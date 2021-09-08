package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems

class PostAdapter(private var listOfPosts: MutableList<PostItems>, private var context: Context, private var onclickPostItem: OnclickPostItem): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    //View Holder
    inner class PostViewHolder(viewItems: View): RecyclerView.ViewHolder(viewItems){
        val postBody: TextView = viewItems.findViewById(R.id.post_body)
        val profileImage: ImageView = viewItems.findViewById(R.id.profile_image)
        val profileName: TextView = viewItems.findViewById(R.id.profile_name)
        val likeCounter: TextView = viewItems.findViewById(R.id.number_of_likes)
        val commentText: TextView = viewItems.findViewById(R.id.comment_text)
        val numberOfComments: TextView = viewItems.findViewById(R.id.number_of_comments)
        val likeIcon: ImageView = viewItems.findViewById(R.id.like_icon)
        val postTime: TextView = viewItems.findViewById(R.id.time_of_post)
        val likeButton: ToggleButton = viewItems.findViewById(R.id.like_button)
        val commentButton: Button = viewItems.findViewById(R.id.comment_button)
        val shareButton: Button = viewItems.findViewById(R.id.share_button)
        val bio: TextView = viewItems.findViewById(R.id.profile_bio)
    }

    //Add posts to list
    fun addPosts(posts: List<PostItems>?) {
        if (posts != null) {
//            listOfPosts.clear()
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
        val bodyOfPost = postPosition.body

        holder.postBody.text = bodyOfPost

        if (id >100){
            holder.numberOfComments.visibility = View.INVISIBLE
            holder.commentText.visibility = View.INVISIBLE
        }

        if (id < 101 && id % 2 == 0){
            holder.likeCounter.text = "6"
        } else if (id < 101 && id % 3 == 0){
            holder.likeCounter.text = "12"
        } else if (id < 101 && id % 5 == 0){
            holder.likeCounter.text = "8"
        } else if (id < 101 && id % 7 == 0){
            holder.likeCounter.text = "14"
        } else if (id < 101 && id % 11 == 0){
            holder.likeCounter.text = "2"
        } else if (id < 101 && id % 13 == 0){
            holder.likeCounter.text = "13"
        } else if (id < 101 && id % 17 == 0){
            holder.likeCounter.text = "3"
        } else if (id < 101 && id % 19 == 0){
            holder.likeCounter.text = "1"
        } else if (id > 100){
            holder.likeCounter.visibility = View.INVISIBLE
            holder.likeIcon.visibility = View.INVISIBLE
        } else{
            holder.likeCounter.text = "36"
        }

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

        //Set on click listener on the share button to share post
        holder.shareButton.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, bodyOfPost)
                type = "text/plain"
            }

            startActivity(context, Intent.createChooser(intent, "Share via"), bundleOf())
        }

        //Set onclick listener on item view
        holder.itemView.setOnClickListener {
            onclickPostItem.navigateToCommentsActivity(position, id, userId)
        }

        //Set onclick listener on item view
        holder.commentButton.setOnClickListener {
            onclickPostItem.navigateToCommentsActivity(position, id, userId)
        }

        //Set a click listener on the like button
        holder.likeButton.setOnCheckedChangeListener { compoundButton, _ ->
            var numberOfLikes: Int

            if (id < 101 && id % 2 == 0){
                numberOfLikes = 6
            } else if (id < 101 && id % 3 == 0){
                numberOfLikes = 12
            } else if (id < 101 && id % 5 == 0){
                numberOfLikes = 8
            } else if (id < 101 && id % 7 == 0){
                numberOfLikes = 14
            } else if (id < 101 && id % 11 == 0){
                numberOfLikes = 2
            } else if (id < 101 && id % 13 == 0){
                numberOfLikes = 13
            } else if (id < 101 && id % 17 == 0){
                numberOfLikes = 3
            } else if (id < 101 && id % 19 == 0){
                numberOfLikes = 1
            } else if (id > 100){
                numberOfLikes = 0
            } else{
                numberOfLikes = 36
            }

            if (compoundButton.isChecked){
                numberOfLikes++

                if (id >100){
                    holder.likeCounter.visibility = View.VISIBLE
                    holder.likeIcon.visibility = View.VISIBLE
                }

                holder.likeCounter.text = numberOfLikes.toString()
                holder.likeIcon.setColorFilter(context.resources.getColor(R.color.blue))
                holder.likeButton.setTextColor(context.resources.getColor(R.color.blue))
            }else{

                if (id >100){
                    holder.likeCounter.visibility = View.INVISIBLE
                    holder.likeIcon.visibility = View.INVISIBLE
                }

                holder.likeCounter.text = numberOfLikes.toString()
                holder.likeIcon.setColorFilter(context.resources.getColor(R.color.black))
                holder.likeButton.setTextColor(context.resources.getColor(R.color.black))
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }
}