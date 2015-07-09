package p3_vacationplanning.services;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class CallToIterableRemoteService<T> {

    private static final Logger logger = Logger.getAnonymousLogger();

    public final ExecutorService executor =
            new ThreadPoolExecutor(10, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());


    public final Collection<T> getSync() {
        logger.info(this.getClass().getSimpleName() + " starts call ...");
        T result = getResultSync();
        List<T> results = new ArrayList<>();
        while (result != null) {
            results.add(result);
            result = getResultSync();
        }
        logger.info(this.getClass().getSimpleName() + " got results: " + results);
        return results;
    }

    protected abstract T getResultSync();

    /**
     * @return an rx observable that is updated with the value asynchronously
     */
    public final Observable<T> getAsyncRx() {
        return Observable.<T>create(o -> {
                try {
                    logger.info(this.getClass().getSimpleName() + " starts call ...");
                    T result = getResultSync();
                    while (result != null) {
                        o.onNext(result);
                        result = getResultSync();
                    }
                    logger.info(this.getClass().getSimpleName() + " completed");
                    o.onCompleted();
                } catch (Throwable e) {
                    o.onError(e);
                }
        }).subscribeOn(Schedulers.from(executor));
    }

}
