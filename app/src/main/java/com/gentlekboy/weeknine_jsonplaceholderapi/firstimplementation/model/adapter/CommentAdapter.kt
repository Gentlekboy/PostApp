package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.comments.CommentItems

class CommentAdapter(private var listOfComments: ArrayList<CommentItems>, private val context: Context): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
//    private var listOfComments: ArrayList<CommentItems> = ArrayList()

    //View Holder
    inner class CommentViewHolder(viewItems: View): RecyclerView.ViewHolder(viewItems){
        val comentBody = viewItems.findViewById<TextView>(R.id.comment_body)
        val commentImage = viewItems.findViewById<ImageView>(R.id.comment_picture)
        val commentName = viewItems.findViewById<TextView>(R.id.comment_name)
        val commentBio = viewItems.findViewById<TextView>(R.id.comment_bio)
    }

    //Add posts to list
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

        holder.comentBody.text = commentPosition.body

        if (commentPosition.id % 2 == 0){
            holder.commentImage.setImageResource(R.drawable.user_7)
            holder.commentName.text = context.getString(R.string.chinenye)
            holder.commentBio.text = context.getString(R.string.chinenye_bio)
        }else{
            holder.commentImage.setImageResource(R.drawable.my_image)
            holder.commentName.text = context.getString(R.string.kufre_udoh)
            holder.commentBio.text = context.getString(R.string.software_engineer_at_google)
        }
    }

    override fun getItemCount(): Int {
        return listOfComments.size
    }
}