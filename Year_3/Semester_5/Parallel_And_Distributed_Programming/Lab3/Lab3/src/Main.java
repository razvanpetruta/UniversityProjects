import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static Integer a, b, c;
    public static Integer functionType;
    public static Integer scanType;
    public static Integer taskNumber;
    public static Matrix matrix1;
    public static Matrix matrix2;

    public static void generateMatrix() {
        matrix1 = new Matrix(a, b);
        matrix2 = new Matrix(b, c);
    }

    public static void testRowBasedWithThreads(int a, int b, int c, int taskNumber) throws InterruptedException {
        Main.a = a;
        Main.b = b;
        Main.c = c;
        Main.taskNumber = taskNumber;
        functionType = 0;
        scanType = 0;
        generateMatrix();

        double startTime = System.nanoTime();
        productByTasks();
        double endTime = System.nanoTime();
        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        String formattedTime = String.format("%.8f seconds", elapsedTimeInSeconds);
        System.out.println("Row-Based with Threads Elapsed time: " + formattedTime);
    }

    public static void testRowBasedWithThreadPool(int a, int b, int c, int taskNumber) throws InterruptedException {
        Main.a = a;
        Main.b = b;
        Main.c = c;
        Main.taskNumber = taskNumber;
        functionType = 1;
        scanType = 0;
        generateMatrix();

        double startTime = System.nanoTime();
        productByThreadPool();
        double endTime = System.nanoTime();
        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        String formattedTime = String.format("%.8f seconds", elapsedTimeInSeconds);
        System.out.println("Row-Based with ThreadPool Elapsed time: " + formattedTime);
    }

    public static void testColumnBasedWithThreads(int a, int b, int c, int taskNumber) throws InterruptedException {
        Main.a = a;
        Main.b = b;
        Main.c = c;
        Main.taskNumber = taskNumber;
        functionType = 0;
        scanType = 1;
        generateMatrix();

        double startTime = System.nanoTime();
        productByTasks();
        double endTime = System.nanoTime();
        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        String formattedTime = String.format("%.8f seconds", elapsedTimeInSeconds);
        System.out.println("Column-Based with Threads Elapsed time: " + formattedTime);
    }

    public static void testColumnBasedWithThreadPool(int a, int b, int c, int taskNumber) throws InterruptedException {
        Main.a = a;
        Main.b = b;
        Main.c = c;
        Main.taskNumber = taskNumber;
        functionType = 1;
        scanType = 1;
        generateMatrix();

        double startTime = System.nanoTime();
        productByThreadPool();
        double endTime = System.nanoTime();
        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        String formattedTime = String.format("%.8f seconds", elapsedTimeInSeconds);
        System.out.println("Column-Based with ThreadPool Elapsed time: " + formattedTime);
    }

    public static void testKthElementBasedWithThreads(int a, int b, int c, int taskNumber) throws InterruptedException {
        Main.a = a;
        Main.b = b;
        Main.c = c;
        Main.taskNumber = taskNumber;
        functionType = 0;
        scanType = 2;
        generateMatrix();

        double startTime = System.nanoTime();
        productByTasks();
        double endTime = System.nanoTime();
        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        String formattedTime = String.format("%.8f seconds", elapsedTimeInSeconds);
        System.out.println("kth Element-Based with Threads Elapsed time: " + formattedTime);
    }

    public static void testKthElementBasedWithThreadPool(int a, int b, int c, int taskNumber) throws InterruptedException {
        Main.a = a;
        Main.b = b;
        Main.c = c;
        Main.taskNumber = taskNumber;
        functionType = 1;
        scanType = 2;
        generateMatrix();

        double startTime = System.nanoTime();
        productByThreadPool();
        double endTime = System.nanoTime();
        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        String formattedTime = String.format("%.8f seconds", elapsedTimeInSeconds);
        System.out.println("kth Element-Based with ThreadPool Elapsed time: " + formattedTime);
    }

    public static void productByTasks() throws InterruptedException {
        Integer[][] result = new Integer[a][c];
        List<Thread> threads = new ArrayList<>();
        int iterations = a * c / taskNumber;
        for (int i = 0; i < taskNumber; i++) {
            int left = i * iterations;
            int right = i == taskNumber - 1 ? (i + 1) * iterations : a * c;
            if (scanType == 0) {
                threads.add(new Thread(new RowThread(result, left, right)));
            } else if (scanType == 1) {
                threads.add(new Thread(new ColumnThread(result, left, right)));
            } else {
                threads.add(new Thread(new KthThread(result, i, taskNumber)));
            }
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void productByThreadPool() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(taskNumber);
        Integer[][] result = new Integer[a][c];
        List<Runnable> tasks = new ArrayList<>();
        int iterations = a * c / taskNumber;
        for (int i = 0; i < taskNumber; i++) {
            int left = i * iterations;
            int right = i == taskNumber - 1 ? (i + 1) * iterations : a * c;
            if (scanType == 0) {
                tasks.add(new RowThread(result, left, right));
            } else if (scanType == 1) {
                tasks.add(new ColumnThread(result, left, right));
            } else {
                tasks.add(new KthThread(result, i, taskNumber));
            }
        }
        for (Runnable task : tasks) {
            executor.execute(task);
        }
        executor.shutdown();
        while (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
            System.out.println("Still waiting for termination");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int a = 1000;
        int b = 1000;
        int c = 1000;
        int taskNumber = 8;

        testRowBasedWithThreads(a, b, c, taskNumber);
        testRowBasedWithThreadPool(a, b, c, taskNumber);
        testColumnBasedWithThreads(a, b, c, taskNumber);
        testColumnBasedWithThreadPool(a, b, c, taskNumber);
        testKthElementBasedWithThreads(a, b, c, taskNumber);
        testKthElementBasedWithThreadPool(a, b, c, taskNumber);
    }
}
