package com.martin.stackusers.models

import com.google.gson.annotations.SerializedName

class Badge {
    @SerializedName("bronze")
    var bronze: Int = 0

    @SerializedName("gold")
    var gold: Int = 0

    @SerializedName("silver")
    var silver: Int = 0
}