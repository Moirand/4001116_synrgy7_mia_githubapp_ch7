package com.example.data

import com.example.data.datasource.local.room.UserEntity
import com.example.data.datasource.remote.response.DetailUserResponse
import com.example.data.datasource.remote.response.SearchResponse
import com.example.data.datasource.remote.response.UserResponseItem
import com.example.domain.model.ApiDetailUser
import com.example.domain.model.ApiSearchUser
import com.example.domain.model.ApiUser
import com.example.domain.model.User

fun User.toUserEntity(): UserEntity =
    UserEntity(
        userId = userId,
        username = username,
        email = email,
        password = password,
        favoriteList = favoriteList
    )

fun UserEntity.toUser(): User =
    User(
        userId = userId,
        username = username,
        email = email,
        password = password,
        favoriteList = favoriteList
    )

fun UserResponseItem.toApiUser(): ApiUser =
    ApiUser(
        gistsUrl = gistsUrl,
        reposUrl = reposUrl,
        followingUrl = followingUrl,
        starredUrl = starredUrl,
        login = login,
        followersUrl = followersUrl,
        type = type,
        url = url,
        subscriptionsUrl = subscriptionsUrl,
        receivedEventsUrl = receivedEventsUrl,
        avatarUrl = avatarUrl,
        eventsUrl = eventsUrl,
        htmlUrl = htmlUrl,
        siteAdmin = siteAdmin,
        id = id,
        gravatarId = gravatarId,
        nodeId = nodeId,
        organizationsUrl = organizationsUrl
    )

fun DetailUserResponse.toApiDetailUser(): ApiDetailUser =
    ApiDetailUser(
        gistsUrl = gistsUrl,
        reposUrl = reposUrl,
        followingUrl = followingUrl,
        twitterUsername = twitterUsername,
        bio = bio,
        createdAt = createdAt,
        login = login,
        type = type,
        blog = blog,
        subscriptionsUrl = subscriptionsUrl,
        updatedAt = updatedAt,
        siteAdmin = siteAdmin,
        company = company,
        id = id,
        publicRepos = publicRepos,
        gravatarId = gravatarId,
        email = email,
        organizationsUrl = organizationsUrl,
        hireable = hireable,
        starredUrl = starredUrl,
        followersUrl = followersUrl,
        publicGists = publicGists,
        url = url,
        receivedEventsUrl = receivedEventsUrl,
        followers = followers,
        avatarUrl = avatarUrl,
        eventsUrl = eventsUrl,
        htmlUrl = htmlUrl,
        following = following,
        name = name,
        location = location,
        nodeId = nodeId
    )

fun SearchResponse.toApiSearchUser(): ApiSearchUser =
    ApiSearchUser(
        totalCount = totalCount,
        incompleteResults = incompleteResults,
        items = items?.map { it.toApiUser() }
    )