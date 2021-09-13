package com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.adapter

import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton

interface MvcOnclickPostItem {
    fun navigateToCommentsActivity(position: Int, id: Int, userId: Int)
    fun sharePost(position: Int, bodyOfPost: String)
    fun checkLikeButton(position: Int, id: Int, compoundButton: CompoundButton, likeCounter: TextView, likeIcon: ImageView, likeButton: ToggleButton)
}