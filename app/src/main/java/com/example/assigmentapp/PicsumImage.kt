package com.example.assigmentapp // Your package name

import com.google.gson.annotations.SerializedName

data class PicsumImage(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("url") // The URL to the image page
    val pageUrl: String,
    @SerializedName("download_url") // The URL to download/display the image
    val downloadUrl: String
)
