package p0_callbackhell_solution;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import p0_callbackhell_solution.services.*;

public class CallbackSolution {

    private static final Logger logger = Logger.getAnonymousLogger();

    /**
     * Demonstration of nested callbacks which then need to composes their responses together.
     * <p>
     * Various different approaches for composition can be done but eventually they end up relying upon
     * synchronization techniques such as the CountDownLatch used here or converge on callback design
     * changes similar to <a href="https://github.com/Netflix/RxJava">Rx</a>.
     */
    public static void run() {
        long startTime = System.nanoTime();

        final ExecutorService executor = CallToRemoteService.DEFAULT_EXECUTOR;
        /* the following are used to synchronize and compose the asynchronous callbacks */
        final CountDownLatch latch = new CountDownLatch(3);
        final AtomicReference<String> f3Value = new AtomicReference<String>();
        final AtomicReference<Integer> f4Value = new AtomicReference<Integer>();
        final AtomicReference<Integer> f5Value = new AtomicReference<Integer>();

        try {
            // get f3 with dependent result from f1
            executor.execute(new CallToRemoteServiceA(new Callback<String>() {

                @Override
                public void call(String f1) {
                    executor.execute(new CallToRemoteServiceC(new Callback<String>() {

                        @Override
                        public void call(String f3) {
                            // we have f1 and f3 now need to compose with others
                            // set to thread-safe variable accessible by external scope
                            f3Value.set(f3);
                            latch.countDown();
                        }

                    }, f1));
                }

            }));

            // get f4/f5 after dependency f2 completes
            executor.execute(new CallToRemoteServiceB(new Callback<Integer>() {

                @Override
                public void call(Integer f2) {
                    executor.execute(new CallToRemoteServiceD(new Callback<Integer>() {

                        @Override
                        public void call(Integer f4) {
                            // we have f2 and f4 now need to compose with others
                            // set to thread-safe variable accessible by external scope
                            f4Value.set(f4);
                            latch.countDown();
                        }

                    }, f2));
                    executor.execute(new CallToRemoteServiceE(new Callback<Integer>() {

                        @Override
                        public void call(Integer f5) {
                            // we have f2 and f5 now need to compose with others
                            // set to thread-safe variable accessible by external scope
                            f5Value.set(f5);
                            latch.countDown();
                        }

                    }, f2));
                }

            }));

            /* we must wait for all callbacks to complete */
            latch.await();
            logger.severe(f3Value.get() + " => " + (f4Value.get() * f5Value.get()));
            logger.severe("took: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms");
        } catch (InterruptedException e) {
            logger.warning("await interrupted: " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                           "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        run();
    }

}
