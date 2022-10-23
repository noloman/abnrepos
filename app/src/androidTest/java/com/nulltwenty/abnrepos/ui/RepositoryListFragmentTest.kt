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
        with(onView(withId(com.nulltwenty.abnrepos.R.id.repositoryListRecyclerView))) {
            check(matches(hasDescendant(withText("fakeName0"))))
            check(matches(hasDescendant(withText("fakeName1"))))
            check(matches(hasDescendant(withText("fakeName2"))))
            check(matches(not(hasDescendant(withText("fakeFullName0")))))
            check(matches(not(hasDescendant(withText("fakeFullName1")))))
            check(matches(not(hasDescendant(withText("fakeFullName2")))))
            check(matches(not(hasDescendant(withText("fakeDescription0")))))
            check(matches(not(hasDescendant(withText("fakeDescription1")))))
            check(matches(not(hasDescendant(withText("fakeDescription2")))))
            check(matches(not(hasDescendant(withText("fakeHtmlUrl0")))))
            check(matches(not(hasDescendant(withText("fakeHtmlUrl1")))))
            check(matches(not(hasDescendant(withText("fakeHtmlUrl2")))))
            check(matches(hasDescendant(withText("fakeVisibility0"))))
            check(matches(hasDescendant(withText("fakeVisibility1"))))
            check(matches(hasDescendant(withText("fakeVisibility2"))))
        }
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