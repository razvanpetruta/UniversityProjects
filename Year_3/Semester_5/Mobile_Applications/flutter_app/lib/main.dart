import 'package:flutter/material.dart';
import 'package:flutter_app/repository/daily_reports_repo.dart';
import 'package:flutter_app/screens/manage_report_page.dart';
import 'package:flutter_app/screens/main_page.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(ChangeNotifierProvider(
    create: (context) => DailyReportsRepository(),
    child: DailyReportApp(),
  ));
}

class DailyReportApp extends StatelessWidget {
  final DailyReportsRepository dailyReportsRepository =
      DailyReportsRepository();

  DailyReportApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.orange),
        useMaterial3: true,
      ),
      home: const DailyReportNavigator(),
    );
  }
}

class DailyReportNavigator extends StatelessWidget {
  const DailyReportNavigator({super.key});

  @override
  Widget build(BuildContext context) {
    return Navigator(
      onGenerateRoute: (RouteSettings settings) {
        switch (settings.name) {
          case '/':
            return MaterialPageRoute(
              builder: (context) => const MainPage(),
            );
          case '/manageReportPage':
            return MaterialPageRoute(
              builder: (context) {
                final args = settings.arguments;
                if (args != null && args is String) {
                  return ManageReportPage(
                    dailyReportsRepository:
                        Provider.of<DailyReportsRepository>(context),
                    reportId: args,
                  );
                } else {
                  return ManageReportPage(
                    dailyReportsRepository:
                        Provider.of<DailyReportsRepository>(context),
                  );
                }
              },
            );
          default:
            return MaterialPageRoute(
              builder: (context) => Container(),
            );
        }
      },
    );
  }
}
