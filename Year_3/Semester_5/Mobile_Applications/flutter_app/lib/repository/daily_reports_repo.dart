import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_app/model/daily_report.dart';
import 'package:uuid/uuid.dart';

class DailyReportsRepository extends ChangeNotifier {
  final List<DailyReport> _reports = _getDailyReports();

  List<DailyReport> get reports => _reports.toList();

  DailyReport getById(String id) {
    return _reports.firstWhere((report) => report.id == id);
  }

  void addReport(DailyReport report) {
    _reports.add(report);
    _sortReports();
    notifyListeners();
  }

  void updateReport(DailyReport updatedReport) {
    final existingReport = _reports.firstWhere((r) => r.id == updatedReport.id);
    if (existingReport.id.isNotEmpty) {
      existingReport.title = updatedReport.title;
      existingReport.gratitude = updatedReport.gratitude;
      existingReport.accomplishments = updatedReport.accomplishments;
      existingReport.shortcomings = updatedReport.shortcomings;
      existingReport.improvementAreas = updatedReport.improvementAreas;
      existingReport.rating = updatedReport.rating;
      existingReport.date = updatedReport.date;
      _sortReports();
      notifyListeners();
    }
  }

  void removeReport(String reportId) {
    _reports.removeWhere((report) => report.id == reportId);
    _sortReports();
    notifyListeners();
  }

  static List<DailyReport> _getDailyReports() {
    final dailyReports = <DailyReport>[];
    final random = Random();
    final now = DateTime.now();

    final titles = ["Great Day", "Productive Morning", "Awesome Experience", "Learning New Things", "Challenging Day"];
    final gratitudes = ["Family Time", "Good Health", "Supportive Friends", "Beautiful Weather", "Tasty Food"];
    final accomplishments = ["Completed a Project", "Learned a New Skill", "Achieved a Milestone", "Helped Someone", "Got a Promotion"];
    final shortcomings = ["Missed a Deadline", "Made a Mistake", "Procrastinated", "Had a Miscommunication", "Lost Focus"];
    final improvementAreas = ["Time Management", "Communication Skills", "Stress Management", "Healthy Lifestyle", "Self-discipline"];

    for (int i = 0; i < 10; i++) {
      final report = DailyReport(
        id: Uuid().v4(),
        title: titles[random.nextInt(titles.length)],
        gratitude: gratitudes[random.nextInt(gratitudes.length)],
        accomplishments: accomplishments[random.nextInt(accomplishments.length)],
        shortcomings: shortcomings[random.nextInt(shortcomings.length)],
        improvementAreas: improvementAreas[random.nextInt(improvementAreas.length)],
        rating: random.nextInt(5) + 1,
        date: now.subtract(Duration(days: i)),
      );
      dailyReports.add(report);
    }

    return dailyReports;
  }

  void _sortReports() {
    _reports.sort((a, b) => b.date.compareTo(a.date));
  }
}