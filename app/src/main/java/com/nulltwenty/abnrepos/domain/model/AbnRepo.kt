package com.nulltwenty.abnrepos.domain.model

import data.api.model.Visibility

data class AbnRepo(
    val name: String, val avatarUrl: String?, val visibility: Visibility, val isPrivate: Boolean
)