import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_app/components/daily_reports_list.dart';
import 'package:flutter_app/repository/daily_reports_repo.dart';

class MainPage extends StatelessWidget {
  const MainPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text("Daily Reports"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(10.0),
        child: Consumer<DailyReportsRepository>(
          builder: (context, dailyReportsRepository, child) {
            return DailyReportList(
              onDeleteReport: (report) {
                dailyReportsRepository.removeReport(report.id);
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text("Successfully deleted"),
                    duration: Duration(seconds: 2),
                  ),
                );
              },
            );
          },
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          Navigator.pushNamed(context, '/manageReportPage');
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}