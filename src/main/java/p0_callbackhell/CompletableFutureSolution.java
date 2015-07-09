package p0_callbackhell;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CompletableFutureSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    public static void run() throws Exception {

        long startTime = System.nanoTime();

        //TODO get service results using respective getAsync() method that returns a CompletableFuture
        //TODO and combine them to obtain the result asynchronously as in CallbackSolution

        //TODO after obtaining the result
        //TODO print it using logResult(result, startTime);
    }

    private static void logResult(String result, long startTime) {
        logger.severe(result);
        logger.severe("took: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        run();
    }

}
