package com.example.githubapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowersResponse(

	@field:SerializedName("FollowersResponse")
	val followersResponse: List<UserResponseItem?>? = null
)
