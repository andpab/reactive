package p0_callbackhell_solution;

import p0_callbackhell_solution.services.*;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class CompletableFutureSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    /**
     * Demonstration of reactive observables which are composed together in a declarative manner.
     */
    public static void run() throws Exception {

        long startTime = System.nanoTime();

        // get f3 with dependent result from f1

        CompletableFuture<String> asyncResultA = new CallToRemoteServiceA().getAsync();
        CompletableFuture<String> asyncResultC =
                asyncResultA.thenCompose(resultA -> new CallToRemoteServiceC(resultA).getAsync());

        // get f4/f5 after dependency f2 completes (needs to be nested in same resultB flat-mapping)
        CompletableFuture<Integer> asyncResultB = new CallToRemoteServiceB().getAsync();
        CompletableFuture<Integer> asyncResultDTimesE =
                asyncResultB.thenCompose(
                        resultB -> new CallToRemoteServiceD(resultB).getAsync()
                                .thenCombine(new CallToRemoteServiceE(resultB).getAsync(),
                                        (d, e) -> d * e));

        // combine results
        CompletableFuture<Void> asyncResultProcessor =
                asyncResultC.thenCombine(asyncResultDTimesE, (c, dTimesE) -> c + " => " + dTimesE)
                .thenAccept((result) -> {
                    logger.severe(result);
                    logger.info("took: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
                    CallToRemoteService.DEFAULT_EXECUTOR.shutdownNow();
                });

        CallToRemoteService.DEFAULT_EXECUTOR.submit((Callable<Void>) asyncResultProcessor::get);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        run();
    }

}
