import 'package:flutter/material.dart';
import 'package:flutter_app/model/daily_report.dart';
import 'package:flutter_app/repository/daily_reports_repo.dart';
import 'package:uuid/uuid.dart';

class ManageReportPage extends StatefulWidget {
  final DailyReportsRepository dailyReportsRepository;
  final String? reportId;

  const ManageReportPage(
      {Key? key, required this.dailyReportsRepository, this.reportId})
      : super(key: key);

  @override
  _ManageReportPageState createState() => _ManageReportPageState();
}

class _ManageReportPageState extends State<ManageReportPage> {
  late TextEditingController titleController;
  late TextEditingController gratitudeController;
  late TextEditingController accomplishmentsController;
  late TextEditingController shortcomingsController;
  late TextEditingController improvementAreasController;
  int rating = 3;

  String? titleError;
  String? gratitudeError;
  String? accomplishmentsError;
  String? shortcomingsError;
  String? improvementAreasError;

  @override
  void initState() {
    super.initState();

    final existingReport = widget.reportId != null ? widget.dailyReportsRepository.getById(widget.reportId!) : null;

    setState(() {
      titleController = TextEditingController(
          text: widget.reportId != null ? existingReport?.title : '');
      gratitudeController = TextEditingController(
          text: widget.reportId != null ? existingReport?.gratitude : '');
      accomplishmentsController = TextEditingController(
          text: widget.reportId != null ? existingReport?.accomplishments : '');
      shortcomingsController = TextEditingController(
          text: widget.reportId != null ? existingReport?.shortcomings : '');
      improvementAreasController = TextEditingController(
          text: widget.reportId != null ? existingReport?.improvementAreas : '');
      rating = widget.reportId != null ? existingReport!.rating : 3;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: widget.reportId == null
            ? const Text("Add a daily report")
            : const Text("Update a daily report"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 16),
              TextField(
                controller: titleController,
                onChanged: (value) {
                  setState(() {
                    titleError = value.isEmpty ? 'Title is required' : null;
                  });
                },
                decoration: InputDecoration(
                  labelText: 'Title',
                  errorText: titleError,
                ),
              ),
              const SizedBox(height: 16),
              TextField(
                controller: gratitudeController,
                onChanged: (value) {
                  setState(() {
                    gratitudeError =
                        value.isEmpty ? 'Gratitude is required' : null;
                  });
                },
                maxLines: null,
                decoration: InputDecoration(
                  labelText: 'Gratitude',
                  errorText: gratitudeError,
                ),
              ),
              const SizedBox(height: 16),
              TextField(
                controller: accomplishmentsController,
                onChanged: (value) {
                  setState(() {
                    accomplishmentsError =
                        value.isEmpty ? 'Accomplishments are required' : null;
                  });
                },
                maxLines: null,
                decoration: InputDecoration(
                  labelText: 'Accomplishments',
                  errorText: accomplishmentsError,
                ),
              ),
              const SizedBox(height: 16),
              TextField(
                controller: shortcomingsController,
                onChanged: (value) {
                  setState(() {
                    shortcomingsError =
                        value.isEmpty ? 'Shortcomings are required' : null;
                  });
                },
                maxLines: null,
                decoration: InputDecoration(
                  labelText: 'Shortcomings',
                  errorText: shortcomingsError,
                ),
              ),
              const SizedBox(height: 16),
              TextField(
                controller: improvementAreasController,
                onChanged: (value) {
                  setState(() {
                    improvementAreasError =
                        value.isEmpty ? 'Improvement Areas are required' : null;
                  });
                },
                maxLines: null,
                decoration: InputDecoration(
                  labelText: 'Improvement Areas',
                  errorText: improvementAreasError,
                ),
              ),
              const SizedBox(height: 16),
              Text('Rating: $rating', style: const TextStyle(fontSize: 20)),
              Slider(
                value: rating.toDouble(),
                onChanged: (value) {
                  setState(() {
                    rating = value.toInt();
                  });
                },
                min: 1,
                max: 5,
                divisions: 4,
                label: rating.toString(),
              ),
              const SizedBox(height: 16),
              ButtonBar(
                alignment: MainAxisAlignment.center,
                buttonMinWidth:
                    double.infinity, // Set the width to the screen width
                children: [
                  ElevatedButton(
                    onPressed: () {
                      setState(() {
                        titleError = titleController.text.isEmpty
                            ? 'Title is required'
                            : null;
                        gratitudeError = gratitudeController.text.isEmpty
                            ? 'Gratitude is required'
                            : null;
                        accomplishmentsError =
                            accomplishmentsController.text.isEmpty
                                ? 'Accomplishments are required'
                                : null;
                        shortcomingsError = shortcomingsController.text.isEmpty
                            ? 'Shortcomings are required'
                            : null;
                        improvementAreasError =
                            improvementAreasController.text.isEmpty
                                ? 'Improvement Areas are required'
                                : null;
        
                        if (titleError != null ||
                            gratitudeError != null ||
                            accomplishmentsError != null ||
                            shortcomingsError != null ||
                            improvementAreasError != null) {
                          return;
                        }
        
                        if (widget.reportId == null) {
                          widget.dailyReportsRepository.addReport(
                            DailyReport(
                              id: Uuid().v4(),
                              title: titleController.text,
                              gratitude: gratitudeController.text,
                              accomplishments: accomplishmentsController.text,
                              shortcomings: shortcomingsController.text,
                              improvementAreas: improvementAreasController.text,
                              rating: rating,
                              date: DateTime.now(),
                            ),
                          );
                        } else {
                          widget.dailyReportsRepository.updateReport(
                            DailyReport(
                              id: widget.reportId!,
                              title: titleController.text,
                              gratitude: gratitudeController.text,
                              accomplishments: accomplishmentsController.text,
                              shortcomings: shortcomingsController.text,
                              improvementAreas: improvementAreasController.text,
                              rating: rating,
                              date: DateTime.now(),
                          ));
                        }
                        Navigator.pop(context);
                      });
                    },
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all(
                          Theme.of(context).scaffoldBackgroundColor),
                    ),
                    child: widget.reportId == null
                        ? const Text('Save Report')
                        : const Text('Update Report'),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
