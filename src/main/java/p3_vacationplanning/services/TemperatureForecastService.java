package p3_vacationplanning.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TemperatureForecastService extends CallToRemoteService<Integer> {

    public static final ExecutorService DEFAULT_EXECUTOR =
            new ThreadPoolExecutor(3, 3, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());


    private final String destination;

    public TemperatureForecastService(String destination) {
        super(DEFAULT_EXECUTOR);
        this.destination = destination;
    }

    @Override
    protected Integer getResultSync() {
        // simulate fetching data from remote service
        try {
            Thread.sleep(330);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destination.length();
    }
}
