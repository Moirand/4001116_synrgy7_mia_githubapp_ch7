package com.example.githubapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowingResponse(

	@field:SerializedName("FollowingResponse")
	val followingResponse: List<UserResponseItem?>? = null
)