package com.martin.stackusers.networking.models

import com.google.gson.annotations.SerializedName
import com.martin.stackusers.models.User

class UsersResponse(
    val items: List<User>,
    @SerializedName("has_more")
    val hasMore: Boolean)