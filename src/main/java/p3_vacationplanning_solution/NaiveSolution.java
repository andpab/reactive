package p3_vacationplanning_solution;

import p3_vacationplanning_solution.services.DestinationData;
import p3_vacationplanning_solution.services.RecommendedDestinationService;
import p3_vacationplanning_solution.services.TemperatureForecastService;
import p3_vacationplanning_solution.services.VacationQuoteService;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NaiveSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    /**
     * Demonstration of naive synchronous solution
     */
    public static void run() {
        long startTime = System.nanoTime();

        RecommendedDestinationService recommendedDestinationService = new RecommendedDestinationService();
        Collection<String> recommendedDestinations = recommendedDestinationService.getSync();

        List<DestinationData> recommendedDestinationsWithQuotes = recommendedDestinations.stream()
                .map((destination) -> {
                    VacationQuoteService quoteService = new VacationQuoteService(destination);
                    Integer quote = quoteService.getSync();
                    return new DestinationData(destination, quote);
                }).collect(Collectors.toList());

        List<DestinationData> recommendationsComplete = recommendedDestinationsWithQuotes.stream()
                .map((destinationWithQuote) -> {
                    String destination = destinationWithQuote.getDestination();
                    TemperatureForecastService temperatureService = new TemperatureForecastService(destination);
                    Integer temperature = temperatureService.getSync();
                    return new DestinationData(destination, destinationWithQuote.getQuote(), temperature);
                }).collect(Collectors.toList());

        recommendationsComplete.stream()
                .map(destinationData -> String.format("%s -> %d Grad, %d €",
                                                     destinationData.getDestination(),
                                                     destinationData.getTemperature(),
                                                     destinationData.getQuote()))
                .forEach(logger::severe);

        logger.info("took: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        run();
    }

}
