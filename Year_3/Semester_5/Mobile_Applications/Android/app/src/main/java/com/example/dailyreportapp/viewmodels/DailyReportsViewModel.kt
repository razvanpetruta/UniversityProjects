package com.example.dailyreportapp.viewmodels

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.dailyreportapp.data.DailyReport
import java.util.UUID
import java.util.Random
import java.util.Calendar

private fun getDailyReports(): List<DailyReport> {
    val dailyReports = mutableListOf<DailyReport>()
    val random = Random()
    val calendar = Calendar.getInstance()

    val titles = arrayOf("Great Day", "Productive Morning", "Awesome Experience", "Learning New Things", "Challenging Day")
    val gratitudes = arrayOf("Family Time", "Good Health", "Supportive Friends", "Beautiful Weather", "Tasty Food")
    val accomplishments = arrayOf("Completed a Project", "Learned a New Skill", "Achieved a Milestone", "Helped Someone", "Got a Promotion")
    val shortcomings = arrayOf("Missed a Deadline", "Made a Mistake", "Procrastinated", "Had a Miscommunication", "Lost Focus")
    val improvementAreas = arrayOf("Time Management", "Communication Skills", "Stress Management", "Healthy Lifestyle", "Self-discipline")

    for (i in 1..10) {
        val report = DailyReport(
            id = UUID.randomUUID(),
            title = titles[random.nextInt(titles.size)],
            gratitude = gratitudes[random.nextInt(gratitudes.size)],
            accomplishments = accomplishments[random.nextInt(accomplishments.size)],
            shortcomings = shortcomings[random.nextInt(shortcomings.size)],
            improvementAreas = improvementAreas[random.nextInt(improvementAreas.size)],
            rating = random.nextInt(5) + 1,
            date = calendar.time
        )
        dailyReports.add(report)

        calendar.add(Calendar.DAY_OF_MONTH, -1)
    }

    return dailyReports
}


class DailyReportsViewModel : ViewModel() {
    private val _reports = getDailyReports().toMutableStateList()

    val reports: List<DailyReport>
        get() = _reports.sortedByDescending { it.date }

    fun getReportById(reportId: UUID): DailyReport? {
        return _reports.find { it.id == reportId }
    }

    fun addReport(report: DailyReport) {
        _reports.add(report)
    }

    fun updateReport(updatedReport: DailyReport) {
        val existingReport = _reports.find { it.id == updatedReport.id }
        if (existingReport != null) {
            existingReport.title = updatedReport.title
            existingReport.gratitude = updatedReport.gratitude
            existingReport.accomplishments = updatedReport.accomplishments
            existingReport.shortcomings = updatedReport.shortcomings
            existingReport.improvementAreas = updatedReport.improvementAreas
            existingReport.rating = updatedReport.rating
            existingReport.date = updatedReport.date
        }
    }

    fun removeReport(reportId: UUID) {
        _reports.removeAll { it.id == reportId }
    }
}