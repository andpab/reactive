package p3_vacationplanning_solution;

import p3_vacationplanning_solution.services.DestinationData;
import p3_vacationplanning_solution.services.RecommendedDestinationService;
import p3_vacationplanning_solution.services.TemperatureForecastService;
import p3_vacationplanning_solution.services.VacationQuoteService;
import rx.Observable;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ReactiveSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    public static void run() {
        long startTime = System.nanoTime();

        Observable<String> vacationRecommendationObservable =
                new RecommendedDestinationService().getAsyncRx();

        vacationRecommendationObservable
                .flatMap(destination -> new VacationQuoteService(destination).getAsyncRx()
                        .zipWith(new TemperatureForecastService(destination).getAsyncRx(),
                                 (quote, temperature) -> new DestinationData(destination, quote, temperature)))
                .map(destinationDataComplete -> String.format("%s -> %d Grad, %d €",
                                                              destinationDataComplete.getDestination(),
                                                              destinationDataComplete.getTemperature(),
                                                              destinationDataComplete.getQuote()))
                .subscribe(
                        logger::severe,
                        Throwable::printStackTrace,
                        () -> {
                            logger.info("took: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
                            System.exit(0);
                        });
    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        run();
    }

}
