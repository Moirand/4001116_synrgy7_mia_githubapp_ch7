package com.example.githubapp.ui.utill

import android.content.Context
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

fun ShapeableImageView.loadImageUrl(context: Context, url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}