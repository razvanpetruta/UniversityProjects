import 'package:flutter/material.dart';
import 'package:flutter_app/model/daily_report.dart';
import 'package:intl/intl.dart';

class DailyReportItem extends StatelessWidget {
  final DailyReport report;
  final void Function(DailyReport) onDeleteReport;

  const DailyReportItem({super.key, 
    required this.report,
    required this.onDeleteReport,
  });

  @override
  Widget build(BuildContext context) {
    final dateFormat = DateFormat("MMMM d, y 'at' HH:mm");
    
    return Card(
      margin: const EdgeInsets.all(8.0),
      child: InkWell(
        onTap: () {
          Navigator.pushNamed(context, '/manageReportPage', arguments: report.id);
        },
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                report.title,
                style: TextStyle(fontWeight: FontWeight.bold, color: Theme.of(context).primaryColor),
              ),
              const SizedBox(height: 16.0),
              Text("Rating: ${report.rating}"),
              Text("Updated on: ${dateFormat.format(report.date)}"),
              Align(
                alignment: Alignment.bottomRight,
                child: IconButton(
                  icon: Icon(Icons.delete, color: Theme.of(context).colorScheme.error),
                  onPressed: () {
                    showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return AlertDialog(
                          title: const Text("Delete Report"),
                          content: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              const Text("Are you sure you want to delete this report?"),
                              Text("Title: ${report.title}"),
                              Text("Date: ${dateFormat.format(report.date)}"),
                              Text("Rating: ${report.rating}"),
                            ],
                          ),
                          actions: [
                            TextButton(
                              onPressed: () {
                                // Dismiss the dialog and delete the report
                                Navigator.of(context).pop();
                                onDeleteReport(report);
                              },
                              child: const Text("Yes"),
                            ),
                            TextButton(
                              onPressed: () {
                                // Dismiss the dialog
                                Navigator.of(context).pop();
                              },
                              child: Text("No"),
                            ),
                          ],
                        );
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}