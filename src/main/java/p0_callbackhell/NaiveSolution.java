package p0_callbackhell;

import p0_callbackhell.services.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class NaiveSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    /**
     * Demonstration of naive synchronous solution
     */
    public static void run() {
        long startTime = System.nanoTime();
        CallToRemoteServiceA serviceA = new CallToRemoteServiceA();
        CallToRemoteServiceB serviceB = new CallToRemoteServiceB();

        // get result of C with dependent result from A
        String resultA = serviceA.getSync();
        String resultC = new CallToRemoteServiceC(resultA).getSync();

        // get result of D and E after dependency B completes
        Integer resultB = serviceB.getSync();
        Integer resultD = new CallToRemoteServiceD(resultB).getSync();
        Integer resultE = new CallToRemoteServiceE(resultB).getSync();

        String result = resultC + " => " + (resultD * resultE);
        logResult(result, startTime);
    }

    public static void logResult(String result, long startTime) {
        logger.severe(result);
        logger.severe("took: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        run();
    }

}
