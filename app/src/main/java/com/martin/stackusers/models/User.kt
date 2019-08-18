package com.martin.stackusers.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class User {
    @PrimaryKey
    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    var userId: Long = 0

    @SerializedName("about_me")
    @ColumnInfo(name = "about_me")
    var aboutMe: String = ""

    @SerializedName("location")
    @ColumnInfo(name = "location")
    var location: String = ""

    @SerializedName("reputation")
    @ColumnInfo(name = "reputation")
    var reputation: Int = 0

    @SerializedName("badge_counts")
    @ColumnInfo(name = "badge_counts")
    var badgeCounts: Badge = Badge()

    @SerializedName("profile_image")
    @ColumnInfo(name = "profile_image")
    var profileImage: String = ""

    @SerializedName("display_name")
    @ColumnInfo(name = "display_name")
    var displayName: String = ""

    @Transient
    var isFooter: Boolean = false
}