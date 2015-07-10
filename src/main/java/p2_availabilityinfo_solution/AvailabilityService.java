package p2_availabilityinfo_solution;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.logging.Logger;

public class AvailabilityService {

    private static final Logger logger = Logger.getAnonymousLogger();

    public Observable<Boolean> isDatabaseAvailable() {
        return createObservableSimulatingStatusUpdates("database");
    }

    public Observable<Boolean> isAppServerAvailable() {
        return createObservableSimulatingStatusUpdates("app server");
    }

    public Observable<Boolean> isExternalSystemAvailable() {
        return createObservableSimulatingStatusUpdates("external system");
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Observable<Boolean> createObservableSimulatingStatusUpdates(String serviceName) {
        return Observable.<Boolean>create(observer -> {
            boolean available = true;
            observer.onNext(true);
            while(true) {
                long randomSleepTime = (long)(Math.random() * 1000);
                try {
                    Thread.sleep(randomSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (randomSleepTime % 5 == 0 || !available) {
                    available = !available;
                    logger.info("Status of " + serviceName + " changed to " + (available ? "UP" : "DOWN"));
                    observer.onNext(available);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
