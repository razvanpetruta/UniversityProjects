class DailyReport {
  String id;
  String title;
  String gratitude;
  String accomplishments;
  String shortcomings;
  String improvementAreas;
  int rating;
  DateTime date;

  DailyReport({
    required this.id,
    required this.title,
    required this.gratitude,
    required this.accomplishments,
    required this.shortcomings,
    required this.improvementAreas,
    required this.rating,
    required this.date,
  });
}
