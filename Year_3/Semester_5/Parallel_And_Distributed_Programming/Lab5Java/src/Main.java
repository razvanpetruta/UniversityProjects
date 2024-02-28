import Implementations.ParallelClassic;
import Implementations.ParallelKaratsuba;
import Implementations.SequentialClassic;
import Implementations.SequentialKaratsuba;
import Library.Polynomial;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

class Main {

    static void runSequentialClassic(Polynomial p1, Polynomial p2) {
        Instant startTime = Instant.now();
        SequentialClassic.multiply(p1, p2);
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println("Sequential classic duration (p1 degree = " + p1.getDegree() +
                ", p2 degree = " + p2.getDegree() + "): " + duration + " milliseconds");
    }

    static void runParallelClassic(Polynomial p1, Polynomial p2) throws InterruptedException {
        Instant startTime = Instant.now();
        ParallelClassic.multiply(p1, p2);
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println("Parallel classic duration (p1 degree = " + p1.getDegree() +
                ", p2 degree = " + p2.getDegree() + "): " + duration + " milliseconds");
    }

    static void runSequentialKaratsuba(Polynomial p1, Polynomial p2) {
        Instant startTime = Instant.now();
        SequentialKaratsuba.multiply(p1, p2);
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println("Sequential Karatsuba duration (p1 degree = " + p1.getDegree() +
                ", p2 degree = " + p2.getDegree() + "): " + duration + " milliseconds");
    }

    static void runParallelKaratsuba(Polynomial p1, Polynomial p2) throws ExecutionException, InterruptedException {
        Instant startTime = Instant.now();
        ParallelKaratsuba.multiply(p1, p2, 1);
        Instant endTime = Instant.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println("Parallel Karatsuba duration (p1 degree = " + p1.getDegree() +
                ", p2 degree = " + p2.getDegree() + "): " + duration + " milliseconds");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Polynomial p1 = new Polynomial(32768);
        Polynomial p2 = new Polynomial(32768);

        runSequentialClassic(p1, p2);
        runParallelClassic(p1, p2);
        runSequentialKaratsuba(p1, p2);
        runParallelKaratsuba(p1, p2);
    }
}
