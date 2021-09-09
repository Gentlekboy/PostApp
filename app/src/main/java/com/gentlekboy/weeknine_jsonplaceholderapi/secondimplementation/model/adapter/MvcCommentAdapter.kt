package com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.data.comments.MvcCommentsItem

class MvcCommentAdapter (
    private var listOfComments: ArrayList<MvcCommentsItem>,
    private val context: Context): RecyclerView.Adapter<MvcCommentAdapter.MvcCommentViewHolder>() {

    inner class MvcCommentViewHolder (viewItems: View): RecyclerView.ViewHolder(viewItems){
        val comentBody = viewItems.findViewById<TextView>(R.id.comment_body)
        val commentImage = viewItems.findViewById<ImageView>(R.id.comment_picture)
        val commentName = viewItems.findViewById<TextView>(R.id.comment_name)
        val commentBio = viewItems.findViewById<TextView>(R.id.comment_bio)
        val commentTime = viewItems.findViewById<TextView>(R.id.comment_time)
    }

    //Add comments to list
    fun addComments(comments: List<MvcCommentsItem>?) {
        if (comments != null) {
            listOfComments.clear()
            listOfComments.addAll(comments)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MvcCommentViewHolder {
        val views = LayoutInflater.from(parent.context).inflate(R.layout.mvc_comments_view_holder, parent, false)
        return MvcCommentViewHolder(views)
    }

    override fun onBindViewHolder(holder: MvcCommentViewHolder, position: Int) {
        val commentPosition = listOfComments[position]
        val id = commentPosition.id

        holder.comentBody.text = commentPosition.body

        if (position > 5){
            holder.commentTime.text = context.getString(R.string.few_moments_ago)
        }

        if (id % 2 == 0){
            holder.commentImage.setImageResource(R.drawable.user_11)
            holder.commentName.text = context.getString(R.string.chinenye)
            holder.commentBio.text = context.getString(R.string.chinenye_bio)
        }else if (id % 3 ==0){
            holder.commentImage.setImageResource(R.drawable.user_12)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }else if (id % 5 ==0){
            holder.commentImage.setImageResource(R.drawable.user_13)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }else if (id % 7 ==0){
            holder.commentImage.setImageResource(R.drawable.user_14)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }else if (id % 11 ==0){
            holder.commentImage.setImageResource(R.drawable.my_image)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }else if (id % 13 ==0){
            holder.commentImage.setImageResource(R.drawable.user_16)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }else if (id % 17 ==0){
            holder.commentImage.setImageResource(R.drawable.user_17)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }else if (id % 19 ==0 ){
            holder.commentImage.setImageResource(R.drawable.user_18)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        } else {
            holder.commentImage.setImageResource(R.drawable.user_15)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }    }

    override fun getItemCount(): Int {
        return listOfComments.size
    }
}