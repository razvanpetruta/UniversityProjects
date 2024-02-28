package com.example.androiddb.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddb.domain.model.DailyReport
import com.example.androiddb.domain.repository.DailyReportRepository
import com.example.androiddb.utils.DataOperationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class DailyReportsViewModel @Inject constructor(
    private val repository: DailyReportRepository
) : ViewModel() {
    private val _isSaving = mutableStateOf<Boolean>(false)
    val isSaving: State<Boolean> get() = _isSaving

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> get() = _errorState

    val reportsList = repository.getAllDailyReports()

    fun getReportById(reportId: UUID) = repository.getDailyReportById(reportId)

    fun addReport(dailyReport: DailyReport) = viewModelScope.launch {
        try {
            _isSaving.value = true
            repository.addDailyReport(dailyReport)
            _errorState.value = null
        } catch (e: DataOperationException) {
            _errorState.value = e.message
        } finally {
            _isSaving.value = false
        }
    }

    fun updateReport(dailyReport: DailyReport) = viewModelScope.launch {
        try {
            _isSaving.value = true
            repository.updateDailyReport(dailyReport)
            _errorState.value = null
        } catch (e: DataOperationException) {
            _errorState.value = e.message
        } finally {
            _isSaving.value = false
        }
    }

    fun removeReport(dailyReport: DailyReport) = viewModelScope.launch {
        try {
            _isSaving.value = true
            repository.deleteDailyReport(dailyReport)
            _errorState.value = null
        } catch (e: DataOperationException) {
            _errorState.value = e.message
        } finally {
            _isSaving.value = false
        }
    }

    fun clearErrorState() {
        _errorState.value = null
    }
}