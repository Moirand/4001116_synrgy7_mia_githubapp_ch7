package com.example.githubapp.ui

import android.content.Context
import android.net.Uri
import com.bumptech.glide.Glide
import com.example.domain.model.ApiDetailUser
import com.example.domain.model.ApiUser
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale

fun ShapeableImageView.loadImageUrl(context: Context, uri: Uri?) {
    uri?.let {
        Glide.with(context)
            .load(uri)
            .into(this)
    }
}
fun String.toDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    return outputFormat.format(inputFormat.parse(this))
}
fun ApiDetailUser.toApiUser(): ApiUser {
    return ApiUser(
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