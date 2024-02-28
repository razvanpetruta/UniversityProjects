package com.example.exam.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam.AppRoutes
import com.example.exam.ConnectionState
import com.example.exam.ErrorResponse
import com.example.exam.data.Listing
import com.example.exam.data.TemplateRepository
import com.example.exam.data.TemplateService
import com.example.exam.logd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(
    private val repository: TemplateRepository,
    private val service: TemplateService
) : ViewModel() {
    // ACTIVE TAB
    private val _activeTab = mutableStateOf<AppRoutes>(AppRoutes.MainScreen)
    val activeTab: State<AppRoutes> get() = _activeTab

    fun setActiveTab(route: AppRoutes) {
        _activeTab.value = route
    }
    //


    // LOADING
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
    //


    // MESSAGES
    private val _message = mutableStateOf<String>("")
    val message: State<String> get() = _message

    fun setMessage(message: String) {
        _message.value = message
    }
    //


    // CONNECTIVITY
    private val _connectivityState = mutableStateOf<ConnectionState>(ConnectionState.Unavailable)

    fun updateConnectionState(connectionState: ConnectionState) {
        _connectivityState.value = connectionState
        if (connectionState == ConnectionState.Available) {
            connectWebSocket()
            getItemsFromServer()
            _types.value = emptyList()
        } else {
            getLocalItems()
            setMessage("No internet connection, syncing with server will be done when connection is available")
        }
    }
    //


    // TESTING
    private val _listings = MutableStateFlow<List<Listing>>(emptyList())
    val listings = _listings.asStateFlow()

    private val _currentListing = mutableStateOf<Listing>(Listing(0, "", "", "", "", 0, "", false))
    val currentListing get() = _currentListing

    init {
        viewModelScope.launch {
            getLocalItems()
        }
    }

    fun getLocalItems() {
        viewModelScope.launch {
            repository.getListings().collect {
                _listings.value = it
            }
        }
    }

    fun addLocalItem(listing: Listing) {
        viewModelScope.launch {
            try {
                listing.needsSync = false
                repository.addListing(listing)
                getLocalItems()
            } catch (e: Exception) {
                setMessage(e.message!!)
            }
        }
    }

    fun addItem(listing: Listing) {
        viewModelScope.launch {
            if (_connectivityState.value == ConnectionState.Available) {
                addItemToServer(listing)
            } else {
                addLocalItemNeedingSync(listing)
            }
        }
    }

    fun getListingById(id: Int) {
        viewModelScope.launch {
            if (_connectivityState.value == ConnectionState.Available) {
                service.getListing(id).enqueue(object : Callback<Listing> {
                    override fun onResponse(call: Call<Listing>, response: Response<Listing>) {
                        if (response.isSuccessful) {
                            _currentListing.value = response.body()!!
                            addLocalItem(response.body()!!)
                            logd("getListingById: ${response.body()}")
                        } else {
                            val gson = Gson()
                            val errorResponse =
                                gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                            val errorMessage = errorResponse.text
                            setMessage(errorMessage)
                        }
                    }

                    override fun onFailure(call: Call<Listing>, t: Throwable) {
                        logd("getListingById: ${t.message}")
                    }
                })
            } else {
                repository.getListing(id).collect {
                    _currentListing.value = it
                }
            }
        }
    }

    fun addLocalItemNeedingSync(listing: Listing) {
        viewModelScope.launch {
            setLoading(true)
            try {
                listing.needsSync = true
                repository.addListing(listing)
                getLocalItems()
                setLoading(false)
                setMessage("Item added: ${listing.id}, ${listing.name}")
            } catch (e: Exception) {
                setMessage(e.message!!)
                setLoading(false)
            }
        }
    }

    fun addItemToServer(listing: Listing) {
        viewModelScope.launch {
            listing.needsSync = false
            setLoading(true)
            service.addListing(listing).enqueue(object : Callback<Listing> {
                override fun onResponse(call: Call<Listing>, response: Response<Listing>) {
                    if (response.isSuccessful) {
                        logd("addItemToServer: $listing")
                        _types.value = emptyList()
                        _monthlyViewersSum.value = emptyMap()
                        setLoading(false)
                    } else {
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                        val errorMessage = errorResponse.text
                        setMessage(errorMessage)
                        setLoading(false)
                    }
                }

                override fun onFailure(call: Call<Listing>, t: Throwable) {
                    logd("addItem: ${t.message}")
                    setLoading(false)
                }
            })
        }
    }

    fun addAllItemsNeedingSyncToServer() {
        getLocalItems()
        val itemsNeedingSync = _listings.value.filter { it.needsSync }
        logd("addAllItemsNeedingSyncToServer: $itemsNeedingSync")
        itemsNeedingSync.forEach { item ->
            service.addListing(item).enqueue(object : Callback<Listing> {
                override fun onResponse(call: Call<Listing>, response: Response<Listing>) {
                    if (response.isSuccessful) {
                        logd("addAllItemsNeedingSyncToServer: $item")
                    } else {
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                        val errorMessage = errorResponse.text
                        setMessage(errorMessage)
                    }
                }

                override fun onFailure(call: Call<Listing>, t: Throwable) {
                    logd("addItem: ${t.message}")
                }
            })
        }
    }

    fun getItemsFromServer() {
        viewModelScope.launch {
            addAllItemsNeedingSyncToServer()
            service.getListings().enqueue(object : Callback<List<Listing>> {
                override fun onResponse(
                    call: Call<List<Listing>>,
                    response: Response<List<Listing>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { items ->
                            viewModelScope.launch {
                                repository.deleteAllListings()
                                items.forEach { item ->
                                    addLocalItem(item)
                                }
                                logd("Items from server: $items")
                            }
                        }
                    } else {
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                        val errorMessage = errorResponse.text
                        setMessage(errorMessage)
                    }
                }

                override fun onFailure(call: Call<List<Listing>>, t: Throwable) {
                    logd("getDayData: ${t.message}")
                }
            })
        }
    }

    private val _types = MutableStateFlow<List<String>>(emptyList())
    val types = _types.asStateFlow()

    fun getTypes() {
        viewModelScope.launch {
            if (_connectivityState.value == ConnectionState.Available) {
                setLoading(true)
                service.getTypes().enqueue(object : Callback<List<String>> {
                    override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                        if (response.isSuccessful) {
                            _types.value = response.body()!!
                            logd("getTypes: ${response.body()}")
                            setLoading(false)
                        } else {
                            val gson = Gson()
                            val errorResponse =
                                gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                            val errorMessage = errorResponse.text
                            setMessage(errorMessage)
                            setLoading(false)
                        }
                    }

                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
                        logd("getTypes: ${t.message}")
                        setLoading(false)
                    }
                })
            }
        }
    }

    fun registerForType(type: String) {
        viewModelScope.launch {
            if (_connectivityState.value == ConnectionState.Available) {
                setLoading(true)
                service.registerType(type).enqueue(object : Callback<Listing> {
                    override fun onResponse(call: Call<Listing>, response: Response<Listing>) {
                        if (response.isSuccessful) {
                            logd("registerForType: ${response.body()}")
                            setMessage("Registered for entity: ${response.body()!!.id} ${response.body()!!.name}")
                            setLoading(false)
                        } else {
                            val gson = Gson()
                            val errorResponse =
                                gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                            val errorMessage = errorResponse.text
                            setMessage(errorMessage)
                            setLoading(false)
                        }
                    }

                    override fun onFailure(call: Call<Listing>, t: Throwable) {
                        logd("registerForType: ${t.message}")
                        setLoading(false)
                    }
                })
            }
        }
    }

    val _monthlyViewersSum = MutableStateFlow<Map<String, Int>>(emptyMap())
    val monthlyViewersSum = _monthlyViewersSum.asStateFlow()

    fun getInterest() {
        viewModelScope.launch {
            if (_connectivityState.value == ConnectionState.Available) {
                setLoading(true)
                service.getInterest().enqueue(object : Callback<List<Listing>> {
                    override fun onResponse(call: Call<List<Listing>>, response: Response<List<Listing>>) {
                        if (response.isSuccessful) {
                            logd("getInterest: ${response.body()}")

                            val groupedByMonth = response.body()!!.groupBy {
                                it.date.substring(0, 7)
                            }.mapValues { entry ->
                                entry.value.sumOf { it.viewers }
                            }
                            _monthlyViewersSum.value = groupedByMonth
                            setLoading(false)
                        } else {
                            val gson = Gson()
                            val errorResponse =
                                gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                            val errorMessage = errorResponse.text
                            setMessage(errorMessage)
                            setLoading(false)
                        }
                    }

                    override fun onFailure(call: Call<List<Listing>>, t: Throwable) {
                        logd("getInterest: ${t.message}")
                        setLoading(false)
                    }
                })
            }
        }
    }

    //


    // WEB SOCKET
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private fun connectWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://10.0.2.2:2407").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                logd("web socket open: $response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val messageType = object : TypeToken<Listing>() {}.type
                val item = gson.fromJson<Listing>(text, messageType)

                viewModelScope.launch {
                    if (_listings.value.find { it.id == item.id } == null) {
                        addLocalItem(item)
                        setMessage("New item added:\n${item.id}, ${item.name}")
                        logd("web socket received: $item")
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: okhttp3.Response?
            ) {
                super.onFailure(webSocket, t, response)
                logd("web socket failure: ${t.message}")
            }
        })

        client.dispatcher.executorService.shutdown()
    }

    private fun disconnectWebSocket() {
        webSocket?.close(1000, null)
        logd("disconnectWebSocket: WebSocket closed")
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }
    //
}