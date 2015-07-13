package p2_availabilityinfo_solution;

import rx.Observable;

import java.util.Arrays;
import java.util.logging.Logger;

public class AvailabilityMain {

    private static final Logger logger = Logger.getAnonymousLogger();

    public static void checkServiceAvailabilityAsync() {
        AvailabilityService availabilityService = new AvailabilityService();

        //TODO asynchronously print whether all services are available or not
        //TODO bonus task: only print status info if overall availability status has changed

        Observable.combineLatest(Arrays.asList(availabilityService.isAppServerAvailable(),
                                               availabilityService.isDatabaseAvailable(),
                                               availabilityService.isExternalSystemAvailable()),
                                 (currentStatusInfos) -> Arrays.stream(currentStatusInfos)
                                                 .allMatch(objStatus -> (Boolean) objStatus))
                .distinctUntilChanged()
                .map(allServicesAvailable -> allServicesAvailable ? "YES" : "NO")
                .subscribe(status -> logger.severe("All services available: " + status));

        logger.info("EXIT checkServiceAvailabilityAsync");
    }


    @SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");

        checkServiceAvailabilityAsync();

        while (true) {
            // main program loop
        }
    }

}
