package com.nulltwenty.abnrepos.ui

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NetworkMonitorViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var sut: NetworkMonitorViewModel
    private val mockedNetworkRequest: NetworkRequest = mockk()
    private lateinit var mockedNetworkCapabilities: NetworkCapabilities
    private lateinit var mockedNetworkConnectivityManager: ConnectivityManager

    private fun initNetwork(connectionAvailable: Boolean) {
        mockedNetworkCapabilities = mockk {
            coEvery { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns connectionAvailable
        }
        mockedNetworkConnectivityManager = mockk(relaxed = true) {
            coEvery { getNetworkCapabilities(any()) } returns mockedNetworkCapabilities
            coEvery { registerNetworkCallback(any(), any(), any()) } coAnswers {}
        }
    }

    @Test
    fun `given ConnectivityManager and NetworkRequest with Internet when observing the connection LiveData it should emit true`() =
        runTest {
            initNetwork(true)
            sut = NetworkMonitorViewModel(
                mockedNetworkConnectivityManager, testDispatcher, mockedNetworkRequest
            )
            sut.connectionLiveData.observeForever { networkAvailable ->
                assertTrue(networkAvailable)
            }
        }

    @Test
    fun `given ConnectivityManager and NetworkRequest without Internet when observing the connection LiveData it should emit false`() =
        runTest {
            initNetwork(false)
            sut = NetworkMonitorViewModel(
                mockedNetworkConnectivityManager, testDispatcher, mockedNetworkRequest
            )
            sut.connectionLiveData.observeForever { networkAvailable ->
                assertTrue(networkAvailable)
            }
        }
}