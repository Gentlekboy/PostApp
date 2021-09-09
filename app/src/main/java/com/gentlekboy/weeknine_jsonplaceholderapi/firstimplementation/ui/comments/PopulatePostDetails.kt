package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui.comments

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.gentlekboy.weeknine_jsonplaceholderapi.R

object PopulatePostDetails {
    //This function populates details of post from post activity
    fun populatePostDetailsInCommentActivity(
        postBodyTextView: TextView,
        postBody: String,
        userId: Int,
        profileImage:ImageView,
        profileName: TextView,
        profileBio: TextView,
        context: Context
    ){
        postBodyTextView.text = postBody

        when(userId){
            1 -> {
                profileImage.setImageResource(R.drawable.user_1)
                profileName.text = context.getString(R.string.peter_akam)
                profileBio.text = context.getString(R.string.peter_bio)
            }
            2 -> {
                profileImage.setImageResource(R.drawable.user_2)
                profileName.text = context.getString(R.string.benjamin_obetta)
                profileBio.text = context.getString(R.string.benjamin_bio)
            }
            3 -> {
                profileImage.setImageResource(R.drawable.user_3)
                profileName.text = context.getString(R.string.anthony_idoko)
                profileBio.text = context.getString(R.string.anthony_bio)
            }
            4 -> {
                profileImage.setImageResource(R.drawable.user_4)
                profileName.text = context.getString(R.string.johnson_oyesina)
                profileBio.text = context.getString(R.string.johnson_bio)
            }
            5 -> {
                profileImage.setImageResource(R.drawable.user_5)
                profileName.text = context.getString(R.string.emmanuel_oruminighen)
                profileBio.text = context.getString(R.string.emmanuel_bio)
            }
            6 -> {
                profileImage.setImageResource(R.drawable.user_6)
                profileName.text = context.getString(R.string.john_doe)
                profileBio.text = context.getString(R.string.john_bio)
            }
            7 -> {
                profileImage.setImageResource(R.drawable.user_7)
                profileName.text = context.getString(R.string.chinenye)
                profileBio.text = context.getString(R.string.chinenye_bio)
            }
            8 -> {
                profileImage.setImageResource(R.drawable.user_8)
                profileName.text = context.getString(R.string.jennifer_santos)
                profileBio.text = context.getString(R.string.jennifer_bio)
            }
            9 -> {
                profileImage.setImageResource(R.drawable.user_9)
                profileName.text = context.getString(R.string.joe_davids)
                profileBio.text = context.getString(R.string.john_davids_bio)
            }
            10 -> {
                profileImage.setImageResource(R.drawable.user_10)
                profileName.text = context.getString(R.string.cassidy_banks)
                profileBio.text = context.getString(R.string.cassidey_bio)
            }
            else -> {
                profileImage.setImageResource(R.drawable.my_image)
                profileName.text = context.getString(R.string.kufre_udoh)
                profileBio.text = context.getString(R.string.software_engineer_at_google)
            }
        }
    }
}