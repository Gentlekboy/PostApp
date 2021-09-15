package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.gentlekboy.weeknine_jsonplaceholderapi.R

fun clickLikeButtonActions(
    id: Int,
    compoundButton: CompoundButton,
    likeCounter: TextView,
    likeIcon: ImageView,
    likeButton: ToggleButton,
    context: Context
){
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
            likeCounter.visibility = View.VISIBLE
            likeIcon.visibility = View.VISIBLE
        }

        likeCounter.text = numberOfLikes.toString()
        likeIcon.setColorFilter(context.resources.getColor(R.color.blue))
        likeButton.setTextColor(context.resources.getColor(R.color.blue))
    }else{

        if (id >100){
            likeCounter.visibility = View.INVISIBLE
            likeIcon.visibility = View.INVISIBLE
        }

        likeCounter.text = numberOfLikes.toString()
        likeIcon.setColorFilter(context.resources.getColor(R.color.black))
        likeButton.setTextColor(context.resources.getColor(R.color.black))
    }
}