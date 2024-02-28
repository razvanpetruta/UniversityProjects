import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_app/components/daily_report_item.dart';
import 'package:flutter_app/model/daily_report.dart';
import 'package:flutter_app/repository/daily_reports_repo.dart';

class DailyReportList extends StatelessWidget {
  final void Function(DailyReport) onDeleteReport;

  const DailyReportList({
    Key? key,
    required this.onDeleteReport,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Consumer<DailyReportsRepository>(
      builder: (context, repository, child) {
        List<DailyReport> reports = repository.reports;

        return ListView.builder(
          itemCount: reports.length,
          itemBuilder: (context, index) {
            final report = reports[index];

            return DailyReportItem(
              report: report,
              onDeleteReport: onDeleteReport,
            );
          },
        );
      },
    );
  }
}