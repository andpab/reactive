package p3_vacationplanning.services;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.*;
import java.util.logging.Logger;

public abstract class CallToRemoteService<T> {

    private static final Logger logger = Logger.getAnonymousLogger();

    private final ExecutorService executor;

    public CallToRemoteService(ExecutorService executor) {
        this.executor = executor;
    }

    public final T getSync() {
        logger.info(this.getClass().getSimpleName() + " starts call ...");
        T result = getResultSync();
        logger.info(this.getClass().getSimpleName() + " got result: " + result);
        return result;
    }

    protected abstract T getResultSync();

    /**
     * @return an rx observable that is updated with the value asynchronously
     */
    public final Observable<T> getAsyncRx() {
        return Observable.<T>create(o -> {
            try {
                T resultSync = getSync();
                o.onNext(resultSync);
                o.onCompleted();
            } catch (Throwable e) {
                o.onError(e);
            }
        }).subscribeOn(Schedulers.from(executor));
    }

}
