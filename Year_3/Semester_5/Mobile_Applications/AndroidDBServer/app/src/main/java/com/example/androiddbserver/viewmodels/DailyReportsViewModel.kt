package com.example.androiddbserver.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddbserver.data.network.ConnectionState
import com.example.androiddbserver.data.network.ConnectivityRepository
import com.example.androiddbserver.data.network.ReportsService
import com.example.androiddbserver.domain.model.DailyReport
import com.example.androiddbserver.domain.repository.DailyReportRepository
import com.example.androiddbserver.utils.DataOperationException
import com.example.androiddbserver.utils.logd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltViewModel
class DailyReportsViewModel @Inject constructor(
    private val repository: DailyReportRepository,
    private val reportsService: ReportsService,
    private val connectivityRepository: ConnectivityRepository,
) : ViewModel() {
    init {
        if (connectivityRepository.currentConnectivityState == ConnectionState.Available) {
            viewModelScope.launch {
                connectWebSocket()
                syncLocalDbWithServer()
            }
        } else {
            getAllReportsFromLocal()
        }
    }

    // flags
    private val _isSaving = mutableStateOf<Boolean>(false)
    val isSaving: State<Boolean> get() = _isSaving
    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> get() = _errorState
    private val _socketMessage = mutableStateOf<String?>(null)
    val socketMessage: State<String?> get() = _socketMessage
    // end flags

    private val _reportsList = MutableStateFlow<List<DailyReport>>(emptyList())
    val reportsList = _reportsList.asStateFlow()

    private suspend fun syncLocalDbWithServer() {
        val reportsNeedingSync = getReportsNeedingSync()
        repository.deleteAllReports()
        getAllReportsFromServer()
        addReportsNeedingSyncToTheServer(reportsNeedingSync)
    }

    private suspend fun getReportsNeedingSync(): List<DailyReport> {
        return withContext(Dispatchers.IO) {
            repository.getAllDailyReports().firstOrNull()?.filter { it.needsSync } ?: emptyList()
        }
    }

    private suspend fun addReportsNeedingSyncToTheServer(reportsNeedingSync: List<DailyReport>) {
        withContext(Dispatchers.IO) {
            reportsNeedingSync.forEach { report ->
                if (repository.countReportById(report.id) == 0) {
                    report.needsSync = false
                    addReportToServer(report)
                }
            }
        }
    }

    fun getAllReports() = viewModelScope.launch {
        if (connectivityRepository.currentConnectivityState == ConnectionState.Available) {
            getAllReportsFromServer()
        } else {
            getAllReportsFromLocal()
        }
    }

    fun getReportById(reportId: Int): Flow<DailyReport?> = flow {
        if (connectivityRepository.currentConnectivityState == ConnectionState.Available) {
            emit(getReportByIdFromServer(reportId))
        } else {
            emit(getReportByIdFromLocal(reportId))
        }
    }

    fun addReport(dailyReport: DailyReport) = viewModelScope.launch {
        _isSaving.value = true
        if (connectivityRepository.currentConnectivityState == ConnectionState.Available) {
            addReportToServer(dailyReport)
        } else {
            addReportToLocalDB(dailyReport)
        }
    }

    fun updateReport(dailyReport: DailyReport) = viewModelScope.launch {
        _isSaving.value = true
        if (connectivityRepository.currentConnectivityState == ConnectionState.Available) {
            updateReportOnServer(dailyReport)
        } else {
            _errorState.value = "No internet connection"
            _isSaving.value = false
        }
    }

    fun removeReport(dailyReport: DailyReport) = viewModelScope.launch {
        _isSaving.value = true
        if (connectivityRepository.currentConnectivityState == ConnectionState.Available) {
            removeReportFromServer(dailyReport)
        } else {
            _errorState.value = "No internet connection"
            _isSaving.value = false
        }
    }

    fun clearErrorState() {
        _errorState.value = null
    }

    private fun getAllReportsFromServer() {
        reportsService.getReports().enqueue(object : Callback<List<DailyReport>> {
            override fun onResponse(call: Call<List<DailyReport>>, response: Response<List<DailyReport>>) {
                if (response.isSuccessful) {
                    response.body()?.let { reports ->
                        logd("getAllReports: successfully retrieved reports from server")
                        viewModelScope.launch {
                            saveReportsToLocalDatabase(reports)
                        }
                    }
                } else {
                    logd("getAllReports: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<DailyReport>>, t: Throwable) {
                logd("getAllReports: ${t.message}")
            }
        })
    }

    private suspend fun saveReportsToLocalDatabase(reports: List<DailyReport>) {
        withContext(Dispatchers.IO) {
            reports.forEach { report ->
                report.needsSync = false
                repository.addDailyReport(report)
            }
            _reportsList.value = reports
        }
    }

    private fun getAllReportsFromLocal() = viewModelScope.launch {
        repository.getAllDailyReports().collect { reports ->
            _reportsList.value = reports
        }
    }

    private suspend fun getReportByIdFromServer(reportId: Int): DailyReport? {
        return suspendCoroutine { continuation ->
            reportsService.getReportById(reportId).enqueue(object : Callback<DailyReport> {
                override fun onResponse(call: Call<DailyReport>, response: Response<DailyReport>) {
                    if (response.isSuccessful) {
                        logd("getReportByIdFromServer: successfully retrieved report from server")
                        continuation.resume(response.body())
                    } else {
                        logd("getReportByIdFromServer: ${response.errorBody()}")
                        continuation.resume(null)
                    }
                }

                override fun onFailure(call: Call<DailyReport>, t: Throwable) {
                    logd("getReportByIdFromServer: ${t.message}")
                    continuation.resume(null)
                }
            })
        }
    }

    private suspend fun getReportByIdFromLocal(reportId: Int): DailyReport {
        return repository.getDailyReportById(reportId).firstOrNull()!!
    }

    private suspend fun addReportToServer(dailyReport: DailyReport) {
        suspendCoroutine<Unit> { continuation ->
            reportsService.addReport(dailyReport).enqueue(object : Callback<DailyReport> {
                override fun onResponse(call: Call<DailyReport>, response: Response<DailyReport>) {
                    if (response.isSuccessful) {
                        logd("addReport: successfully added report to server")
                    } else {
                        logd("addReport: ${response.errorBody()}")
                        _errorState.value = "Error adding report"
                    }
                    _isSaving.value = false
                    continuation.resume(Unit)
                }

                override fun onFailure(call: Call<DailyReport>, t: Throwable) {
                    logd("addReport: ${t.message}")
                    _errorState.value = "Network error: ${t.message}"
                    _isSaving.value = false
                    continuation.resume(Unit)
                }
            })
        }
    }

    private fun addReportToLocalDB(dailyReport: DailyReport) = viewModelScope.launch {
        try {
            dailyReport.id = _reportsList.value.size + 1
            repository.addDailyReport(dailyReport)
            addReportToLocalList(dailyReport)
        } catch (e: DataOperationException) {
            _errorState.value = e.message
        } finally {
            _isSaving.value = false
        }
    }

    private suspend fun updateReportOnServer(dailyReport: DailyReport) {
        suspendCoroutine<Unit> { continuation ->
            reportsService.updateReport(dailyReport.id, dailyReport).enqueue(object : Callback<DailyReport> {
                override fun onResponse(call: Call<DailyReport>, response: Response<DailyReport>) {
                    if (response.isSuccessful) {
                        logd("updateReport: successfully updated report")
                    } else {
                        logd("updateReport: ${response.errorBody()}")
                        _errorState.value = "Error updating report"
                    }
                    _isSaving.value = false
                    continuation.resume(Unit)
                }

                override fun onFailure(call: Call<DailyReport>, t: Throwable) {
                    logd("updateReport: ${t.message}")
                    _errorState.value = "Network error: ${t.message}"
                    _isSaving.value = false
                    continuation.resume(Unit)
                }
            })
        }
    }

    private suspend fun removeReportFromServer(dailyReport: DailyReport) {
        suspendCoroutine<Unit> { continuation ->
            reportsService.deleteReport(dailyReport.id).enqueue(object : Callback<DailyReport> {
                override fun onResponse(call: Call<DailyReport>, response: Response<DailyReport>) {
                    if (response.isSuccessful) {
                        logd("removeReport: successfully deleted report")
                    } else {
                        logd("removeReport: ${response.errorBody()}")
                        _errorState.value = "Error deleting report"
                    }
                    _isSaving.value = false
                    continuation.resume(Unit)
                }

                override fun onFailure(call: Call<DailyReport>, t: Throwable) {
                    logd("removeReport: ${t.message}")
                    _errorState.value = "Network error: ${t.message}"
                    _isSaving.value = false
                    continuation.resume(Unit)
                }
            })
        }
    }


    private var _previousConnectionState: ConnectionState? = null
    val previousConnectionState: ConnectionState? get() = _previousConnectionState

    fun updateConnectionState(newConnectionState: ConnectionState) {
        if (_previousConnectionState == ConnectionState.Unavailable && newConnectionState == ConnectionState.Available) {
            viewModelScope.launch {
                connectWebSocket()
                syncLocalDbWithServer()
            }
        }
        if (_previousConnectionState == ConnectionState.Available && newConnectionState == ConnectionState.Unavailable) {
            disconnectWebSocket()
        }
        if (_previousConnectionState != newConnectionState) {
            _previousConnectionState = newConnectionState
        }
    }

    fun clearSocketMessage() {
        _socketMessage.value = null
    }

    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private fun connectWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://10.0.2.2:3000").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                super.onOpen(webSocket, response)
                logd("onOpen: $response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val messageType = object : TypeToken<Map<String, Any>>() {}.type
                val message = gson.fromJson<Map<String, Any>>(text, messageType)

                // Handle the message based on its content
                when (message["action"]) {
                    "create" -> {
                        _socketMessage.value = "CREATED"
                        val reportJson = gson.toJson(message["report"])
                        val report = gson.fromJson(reportJson, DailyReport::class.java)
                        logd("Received new report: $report")
                        viewModelScope.launch {
                            addReportIfNotExists(report)
                        }
                    }

                    "update" -> {
                        _socketMessage.value = "UPDATED"
                        val reportJson = gson.toJson(message["report"])
                        val report = gson.fromJson(reportJson, DailyReport::class.java)
                        logd("Received updated report: $report")
                        viewModelScope.launch {
                            updateReportIfExists(report)
                        }
                    }

                    "delete" -> {
                        _socketMessage.value = "DELETED"
                        val reportJson = gson.toJson(message["report"])
                        val report = gson.fromJson(reportJson, DailyReport::class.java)
                        logd("Received deleted report: $report")
                        viewModelScope.launch {
                            deleteReportIfExists(report.id)
                        }
                    }

                    else -> logd("Received unknown message: $message")
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                super.onFailure(webSocket, t, response)
                logd("onFailure: ${t.message}")
            }
        })

        // Ensure the client doesn't shut down when the WebSocket closes
        client.dispatcher.executorService.shutdown()
    }

    private fun disconnectWebSocket() {
        webSocket?.close(1000, null) // Normal closure
        logd("disconnectWebSocket: WebSocket closed")
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }

    private suspend fun addReportIfNotExists(report: DailyReport) {
        withContext(Dispatchers.IO) {
            if (repository.countReportById(report.id) == 0) {
                repository.addDailyReport(report)
                addReportToLocalList(report)
            }
        }
    }

    private fun addReportToLocalList(report: DailyReport) {
        val currentReports = _reportsList.value.toMutableList()
        currentReports.add(report)
        _reportsList.value = currentReports
    }

    private suspend fun updateReportIfExists(report: DailyReport) {
        withContext(Dispatchers.IO) {
            if (repository.countReportById(report.id) > 0) {
                repository.updateDailyReport(report)
                updateLocalReportsList(report)
            }
        }
    }

    private fun updateLocalReportsList(updatedReport: DailyReport) {
        val currentReports = _reportsList.value.toMutableList()
        val reportIndex = currentReports.indexOfFirst { it.id == updatedReport.id }
        if (reportIndex != -1) {
            currentReports[reportIndex] = updatedReport
            _reportsList.value = currentReports
        }
    }

    private suspend fun deleteReportIfExists(reportId: Int) {
        withContext(Dispatchers.IO) {
            if (repository.countReportById(reportId) > 0) {
                repository.deleteDailyReportById(reportId)
                removeReportFromLocalList(reportId)
            }
        }
    }

    private fun removeReportFromLocalList(reportId: Int) {
        val currentReports = _reportsList.value.filter { it.id != reportId }
        _reportsList.value = currentReports
    }
}