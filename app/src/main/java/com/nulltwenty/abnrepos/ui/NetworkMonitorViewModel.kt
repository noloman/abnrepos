package com.nulltwenty.abnrepos.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.inject.Inject
import javax.net.SocketFactory

@HiltViewModel
class NetworkMonitorViewModel @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    @IoCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val networkRequest: NetworkRequest
) : ViewModel() {
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val validNetworks: MutableSet<Network> = HashSet()
    private val _connectionLiveData = MutableLiveData<Boolean>()
    val connectionLiveData: LiveData<Boolean> = _connectionLiveData

    private fun checkValidNetworks() {
        _connectionLiveData.postValue(validNetworks.size > 0)
    }

    init {
        startMonitoringNetworkStatus()
    }

    @VisibleForTesting
    fun startMonitoringNetworkStatus() {
        networkCallback = createNetworkCallback()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onCleared() {
        super.onCleared()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasInternetCapability =
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            if (hasInternetCapability == true) {
                // Check if this network actually has internet
                viewModelScope.launch(coroutineDispatcher) {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        validNetworks.add(network)
                        checkValidNetworks()
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    object DoesNetworkHaveInternet {
        suspend fun execute(socketFactory: SocketFactory): Boolean = withContext(Dispatchers.IO) {
            try {
                val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                socket.close()
                true
            } catch (e: IOException) {
                false
            }
        }
    }
}