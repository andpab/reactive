package p0_callbackhell_solution;

import p0_callbackhell_solution.services.*;
import rx.Observable;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class CompletableFutureSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    /**
     * Demonstration of reactive observables which are composed together in a declarative manner.
     */
    public static void run() throws Exception {

        long startTime = System.nanoTime();

        // get C with dependent result from A
        CompletableFuture<String> asyncResultA = new CallToRemoteServiceA().getAsync();
        CompletableFuture<String> asyncResultC =
                asyncResultA.thenApply(resultA -> new CallToRemoteServiceC(resultA).getAsync().join());

        // get D * E after dependency B completes
        CompletableFuture<Integer> asyncResultB = new CallToRemoteServiceB().getAsync();
        CompletableFuture<Integer> asyncResultDTimesE =
                asyncResultB.thenCompose(resultB -> {
                    CompletableFuture<Integer> asyncResultD = new CallToRemoteServiceD(resultB).getAsync();
                    CompletableFuture<Integer> asyncResultE = new CallToRemoteServiceE(resultB).getAsync();
                    return asyncResultD.thenCombine(asyncResultE,
                                             (resultD, resultE) -> resultD * resultE);
                });

        // combine results
        CompletableFuture<Void> asyncResultProcessor =
                CompletableFuture.allOf(asyncResultC, asyncResultDTimesE)
                    .thenApply(ignored -> asyncResultC.join() + " => " + asyncResultDTimesE.join())
                    .thenAccept(result -> {
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
