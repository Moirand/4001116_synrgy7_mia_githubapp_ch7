package com.example.githubapp.ui.utill

import android.content.Context
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale

fun ShapeableImageView.loadImageUrl(context: Context, url: String?) {
    url?.let {
        Glide.with(context)
            .load(url)
            .into(this)
    }
}
fun String.toDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    return outputFormat.format(inputFormat.parse(this))
}

fun generateToken(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..20).map { allowedChars.random() }.joinToString("")
}