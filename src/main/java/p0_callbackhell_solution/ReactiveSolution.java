package p0_callbackhell_solution;

import p0_callbackhell_solution.services.*;
import rx.Observable;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ReactiveSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    public static void run() {
        long startTime = System.nanoTime();

        // get C with dependent result from A
        Observable<String> asyncResultA = new CallToRemoteServiceA().getResultAsync();
        Observable<String> asyncResultC = asyncResultA
                .flatMap(resultA -> new CallToRemoteServiceC(resultA).getResultAsync());

        // get D * E after dependency B completes
        Observable<Integer> asyncResultB = new CallToRemoteServiceB().getResultAsync();
        Observable<Integer> asyncResultOfDTimesE = asyncResultB
                .flatMap(resultB -> new CallToRemoteServiceD(resultB).getResultAsync()
                        .zipWith(new CallToRemoteServiceE(resultB).getResultAsync(),
                                (resultD, resultE) -> resultD * resultE));

        // combine results
        Observable<String> asyncEndResult = asyncResultC
                .zipWith(asyncResultOfDTimesE,
                         (resultC, resultOfDTimesE) -> resultC + " => " + resultOfDTimesE);

        asyncEndResult.subscribe(
                logger::severe,
                Throwable::printStackTrace,
                () -> {
                    logger.severe("took: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
                    CallToRemoteService.DEFAULT_EXECUTOR.shutdownNow();
                });

    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        run();
    }

}
