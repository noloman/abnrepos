package com.nulltwenty.abnrepos

import com.nulltwenty.abnrepos.data.api.model.Owner
import com.nulltwenty.abnrepos.data.api.model.RepositoryListResponseElement
import com.nulltwenty.abnrepos.data.api.model.Visibility
import com.nulltwenty.abnrepos.data.db.Repository
import kotlin.random.Random

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

fun getFakeRepositoryList(number: Int) = mutableListOf<Repository>().apply {
    for (i in 0 until number) {
        add(
            getFakeRepository(
                id = Random.nextLong(),
                name = "fakeName${i}",
                fullName = "fakeFullName${i}",
                description = "fakeDescription${i}",
                htmlUrl = "fakeHtmlUrl${i}",
                avatarUrl = "fakeAvatarUrl${i}",
                visibility = "fakeVisibility${i}"
            )
        )
    }
}

fun getFakeRepository(
    id: Long = 1L,
    name: String = "fakeName",
    fullName: String = "fakeFullName",
    description: String = "fakeDescription",
    htmlUrl: String = "fakeHtmlUrl",
    avatarUrl: String = "fakeAvatarUrl",
    visibility: String = "fakeVisibility"
) = Repository(
    id, name, fullName, description, htmlUrl, avatarUrl, visibility
)