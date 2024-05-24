package com.example.githubapp.ui.utill

import android.content.Context
import com.bumptech.glide.Glide
import com.example.githubapp.data.remote.response.DetailUserResponse
import com.example.githubapp.data.remote.response.UserResponseItem
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

fun DetailUserResponse.toUserResponseItem(): UserResponseItem{
    return UserResponseItem(
        gistsUrl = this.gistsUrl,
        reposUrl = this.reposUrl,
        followingUrl = this.followingUrl,
        starredUrl = this.starredUrl,
        login = this.login,
        followersUrl = this.followersUrl,
        type = this.type,
        url = this.url,
        subscriptionsUrl = this.subscriptionsUrl,
        receivedEventsUrl = this.receivedEventsUrl,
        avatarUrl = this.avatarUrl,
        eventsUrl = this.eventsUrl,
        htmlUrl = this.htmlUrl,
        siteAdmin = this.siteAdmin,
        id = this.id,
        gravatarId = this.gravatarId,
        nodeId = this.nodeId,
        organizationsUrl = this.organizationsUrl
    )
}