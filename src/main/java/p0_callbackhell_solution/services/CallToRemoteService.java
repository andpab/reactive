package p0_callbackhell_solution.services;

import p0_callbackhell_solution.Callback;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.*;
import java.util.logging.Logger;

public abstract class CallToRemoteService<T> implements Runnable {

    private static final Logger logger = Logger.getAnonymousLogger();

    public static final ExecutorService DEFAULT_EXECUTOR =
            new ThreadPoolExecutor(4, 4, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    private Callback<T> callback;

    protected CallToRemoteService() {
    }

    protected CallToRemoteService(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public final void run() {
        T result = getSync();
        callback.call(result);
    }

    public final T getSync() {
        logger.info(this.getClass().getSimpleName() + " starts call ...");
        T result = getResultSync();
        logger.info(this.getClass().getSimpleName() + " got result: " + result);
        return result;
    }

    protected abstract T getResultSync();

    public Observable<T> getResultAsync() {
        return Observable.<T>create(subscriber -> {
            try {
                T value = getSync();

                subscriber.onNext(value);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }

        }).subscribeOn(Schedulers.from(DEFAULT_EXECUTOR));
    }

    public CompletableFuture<T> getAsync() {
        return CompletableFuture.supplyAsync(this::getSync, DEFAULT_EXECUTOR);
    }
}
