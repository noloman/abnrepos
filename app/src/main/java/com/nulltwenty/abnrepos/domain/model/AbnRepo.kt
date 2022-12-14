package com.nulltwenty.abnrepos.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class AbnRepo(
    val id: Long,
    val name: String,
    val fullName: String,
    val avatarUrl: String?,
    val description: String?,
    val htmlUrl: String,
    val visibility: String
) : Parcelable