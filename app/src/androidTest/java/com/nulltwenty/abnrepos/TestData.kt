package com.nulltwenty.abnrepos

import com.nulltwenty.abnrepos.data.api.model.Owner
import com.nulltwenty.abnrepos.data.api.model.RepositoryListResponseElement
import com.nulltwenty.abnrepos.data.api.model.Visibility
import com.nulltwenty.abnrepos.data.db.Repository

val fakeRepositoryListResponseElement: RepositoryListResponseElement =
    RepositoryListResponseElement(
        id = 1L,
        fullName = "fullName",
        description = "description",
        owner = Owner(avatarURL = "avatarUrl"),
        htmlURL = "htmlUrl",
        name = "name",
        private = false,
        visibility = Visibility.public
    )

val fakeRepository = Repository(
    1L,
    "fakeName",
    "fakeFullName",
    "fakeDescription",
    "fakeHtmlUrl",
    "fakeAvatarUrl",
    "fakeVisibility"
)