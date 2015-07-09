package p3_vacationplanning.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VacationQuoteService extends CallToRemoteService<Integer> {

    public static final ExecutorService DEFAULT_EXECUTOR =
            new ThreadPoolExecutor(5, 5, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());


    private final String destination;

    public VacationQuoteService(String destination) {
        super(DEFAULT_EXECUTOR);
        this.destination = destination;
    }

    @Override
    protected Integer getResultSync() {
        // simulate fetching data from remote service
        try {
            Thread.sleep(170);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destination.charAt(0) * 10;
    }
}
