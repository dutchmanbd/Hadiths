package com.ticonsys.hadiths.data.network.responses

import com.google.gson.annotations.SerializedName

data class ResponseDTO<E>(
    @SerializedName("total")
    val total: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("data")
    val data: E?
)