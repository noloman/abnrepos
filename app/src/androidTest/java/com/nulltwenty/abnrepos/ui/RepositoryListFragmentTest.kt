package com.nulltwenty.abnrepos.ui

import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RepositoriesDatabase
import com.nulltwenty.abnrepos.data.db.Repository
import com.nulltwenty.abnrepos.data.di.DataModule
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import com.nulltwenty.abnrepos.data.repository.GithubRemoteMediator
import com.nulltwenty.abnrepos.data.repository.RepositoriesRepository
import com.nulltwenty.abnrepos.getFakeRepositoryList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DataModule::class)
class RepositoryListFragmentTest {
    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun givenAListOfRepositories_whenTheFragmentIsLaunched_itShouldShowTheProperDataInTheList() {
        checkPresenceOf("fakeName0")
        checkPresenceOf("fakeName1")
        checkPresenceOf("fakeName2")
        checkAbsenceOf("fakeFullName0")
        checkAbsenceOf("fakeFullName1")
        checkAbsenceOf("fakeFullName2")
        checkAbsenceOf("fakeDescription0")
        checkAbsenceOf("fakeDescription1")
        checkAbsenceOf("fakeDescription2")
        checkAbsenceOf("fakeHtmlUrl0")
        checkAbsenceOf("fakeHtmlUrl1")
        checkAbsenceOf("fakeHtmlUrl2")
        checkPresenceOf("fakeVisibility0")
        checkPresenceOf("fakeVisibility1")
        checkPresenceOf("fakeVisibility2")
    }

    private fun checkPresenceOf(text: String) {
        onView(withId(com.nulltwenty.abnrepos.R.id.repositoryListRecyclerView)).check(
            matches(
                hasDescendant(withText(text))
            )
        )
    }

    private fun checkAbsenceOf(text: String) {
        onView(withId(com.nulltwenty.abnrepos.R.id.repositoryListRecyclerView)).check(
            matches(
                not(
                    hasDescendant(withText(text))
                )
            )
        )
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object TestDataModule {
        @Provides
        fun githubRepositoriesRepository(): RepositoriesRepository =
            object : RepositoriesRepository {
                override suspend fun fetchRepositories(): Flow<PagingData<Repository>> = flowOf(
                    PagingData.from(getFakeRepositoryList(3))
                )
            }

        @Provides
        fun provideGithubRemoteMediator(
            @IoCoroutineDispatcher ioCoroutineDispatcher: CoroutineDispatcher,
            service: GithubService,
            database: RepositoriesDatabase
        ) = GithubRemoteMediator(ioCoroutineDispatcher, service, database)
    }
}