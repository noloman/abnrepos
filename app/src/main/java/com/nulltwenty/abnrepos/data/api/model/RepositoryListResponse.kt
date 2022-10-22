package com.nulltwenty.abnrepos.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

typealias RepositoryListResponse = List<RepositoryListResponseElement>

@JsonClass(generateAdapter = true)
data class RepositoryListResponseElement(
    val id: Long,
    val nodeID: String? = null,
    val name: String,
    @Json(name = "full_name") val fullName: String,
    val private: Boolean,
    val owner: Owner,
    @Json(name = "html_url") val htmlURL: String,
    val description: String? = null,
    val fork: Boolean? = null,
    val url: String? = null,
    val forksURL: String? = null,
    val keysURL: String? = null,
    val collaboratorsURL: String? = null,
    val teamsURL: String? = null,
    val hooksURL: String? = null,
    val issueEventsURL: String? = null,
    val eventsURL: String? = null,
    val assigneesURL: String? = null,
    val branchesURL: String? = null,
    val tagsURL: String? = null,
    val blobsURL: String? = null,
    val gitTagsURL: String? = null,
    val gitRefsURL: String? = null,
    val treesURL: String? = null,
    val statusesURL: String? = null,
    val languagesURL: String? = null,
    val stargazersURL: String? = null,
    val contributorsURL: String? = null,
    val subscribersURL: String? = null,
    val subscriptionURL: String? = null,
    val commitsURL: String? = null,
    val gitCommitsURL: String? = null,
    val commentsURL: String? = null,
    val issueCommentURL: String? = null,
    val contentsURL: String? = null,
    val compareURL: String? = null,
    val mergesURL: String? = null,
    val archiveURL: String? = null,
    val downloadsURL: String? = null,
    val issuesURL: String? = null,
    val pullsURL: String? = null,
    val milestonesURL: String? = null,
    val notificationsURL: String? = null,
    val labelsURL: String? = null,
    val releasesURL: String? = null,
    val deploymentsURL: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val pushedAt: String? = null,
    val gitURL: String? = null,
    val sshURL: String? = null,
    val cloneURL: String? = null,
    val svnURL: String? = null,
    val homepage: String? = null,
    val size: Long? = null,
    val stargazersCount: Long? = null,
    val watchersCount: Long? = null,
    val language: String? = null,
    val hasIssues: Boolean? = null,
    val hasProjects: Boolean? = null,
    val hasDownloads: Boolean? = null,
    val hasWiki: Boolean? = null,
    val hasPages: Boolean? = null,
    val forksCount: Long? = null,
    val mirrorURL: Any? = null,
    val archived: Boolean? = null,
    val disabled: Boolean? = null,
    val openIssuesCount: Long? = null,
    val license: License? = null,
    val allowForking: Boolean? = null,
    val isTemplate: Boolean? = null,
    val webCommitSignoffRequired: Boolean? = null,
    val topics: List<Any?>? = null,
    val visibility: Visibility,
    val forks: Long? = null,
    val openIssues: Long? = null,
    val watchers: Long? = null,
    val defaultBranch: DefaultBranch? = null
)

enum class DefaultBranch {
    Develop, Main, Master
}

@JsonClass(generateAdapter = true)
data class License(
    val key: String? = null,
    val name: String? = null,
    val spdxID: String? = null,
    val url: String? = null,
    val nodeID: String? = null
)

@JsonClass(generateAdapter = true)
data class Owner(
    val login: Login? = null,
    val id: Long? = null,
    val nodeID: NodeID? = null,
    @Json(name = "avatar_url") val avatarURL: String? = null,
    val gravatarID: String? = null,
    val url: String? = null,
    val htmlURL: String? = null,
    val followersURL: String? = null,
    val followingURL: FollowingURL? = null,
    val gistsURL: GistsURL? = null,
    val starredURL: StarredURL? = null,
    val subscriptionsURL: String? = null,
    val organizationsURL: String? = null,
    val reposURL: String? = null,
    val eventsURL: EventsURL? = null,
    val receivedEventsURL: String? = null,
    val type: Type? = null,
    val siteAdmin: Boolean? = null
)

enum class EventsURL {
    HTTPSAPIGithubCOMUsersAbnamrocoesdEventsPrivacy
}

enum class FollowingURL {
    HTTPSAPIGithubCOMUsersAbnamrocoesdFollowingOtherUser
}

enum class GistsURL {
    HTTPSAPIGithubCOMUsersAbnamrocoesdGistsGistID
}

enum class Login {
    abnamrocoesd
}

enum class NodeID {
    MDEyOk9YZ2FuaXphdGlvbjE1ODc2Mzk3
}

enum class StarredURL {
    HTTPSAPIGithubCOMUsersAbnamrocoesdStarredOwnerRepo
}

enum class Type {
    Organization
}

enum class Visibility {
    public
}
