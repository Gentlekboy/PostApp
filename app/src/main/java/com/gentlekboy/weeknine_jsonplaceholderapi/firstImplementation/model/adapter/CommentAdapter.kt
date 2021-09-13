package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.comments.CommentItems

class CommentAdapter(private var listOfComments: ArrayList<CommentItems>, private val context: Context): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    //View Holder
    inner class CommentViewHolder(viewItems: View): RecyclerView.ViewHolder(viewItems){
        val comentBody = viewItems.findViewById<TextView>(R.id.comment_body)
        val commentImage = viewItems.findViewById<ImageView>(R.id.comment_picture)
        val commentName = viewItems.findViewById<TextView>(R.id.comment_name)
        val commentBio = viewItems.findViewById<TextView>(R.id.comment_bio)
        val commentTime = viewItems.findViewById<TextView>(R.id.comment_time)
    }

    //Add comments to list
    fun addComments(comments: List<CommentItems>?) {
        if (comments != null) {
            listOfComments.clear()
            listOfComments.addAll(comments)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val views = LayoutInflater.from(parent.context).inflate(R.layout.comments_view_holder, parent, false)
        return CommentViewHolder(views)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentPosition = listOfComments[position]
        val id = commentPosition.id

        holder.comentBody.text = commentPosition.body
        holder.commentBio.text = commentPosition.email

        if (position > 5){
            holder.commentTime.text = context.getString(R.string.few_moments_ago)
        }

        if (id % 2 == 0){
            holder.commentImage.setImageResource(R.drawable.user_11)
            holder.commentName.text = context.getString(R.string.ruth_roberts)
        }else if (id % 3 ==0){
            holder.commentImage.setImageResource(R.drawable.user_12)
            holder.commentName.text = context.getString(R.string.jude_jonathan)
        }else if (id % 5 ==0){
            holder.commentImage.setImageResource(R.drawable.user_13)
            holder.commentName.text = context.getString(R.string.tolu_longe)
        }else if (id % 7 ==0){
            holder.commentImage.setImageResource(R.drawable.user_14)
            holder.commentName.text = context.getString(R.string.mohammad_obaalasaki)
        }else if (id % 11 ==0){
            holder.commentImage.setImageResource(R.drawable.my_image)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
        }else if (id % 13 ==0){
            holder.commentImage.setImageResource(R.drawable.user_16)
            holder.commentName.text = context.getString(R.string.joseph_okezie)
        }else if (id % 17 ==0){
            holder.commentImage.setImageResource(R.drawable.user_17)
            holder.commentName.text = context.getString(R.string.samuel_ochuba)
        }else if (id % 19 ==0 ){
            holder.commentImage.setImageResource(R.drawable.user_18)
            holder.commentName.text = context.getString(R.string.osehiase_ehilen)
        } else {
            holder.commentImage.setImageResource(R.drawable.user_15)
            holder.commentName.text = context.getString(R.string.godday_okoduwa)
        }
    }

    override fun getItemCount(): Int {
        return listOfComments.size
    }
}