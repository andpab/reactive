package p0_callbackhell.services;

import p0_callbackhell.Callback;

public final class CallToRemoteServiceB extends CallToRemoteService<Integer> {

    public CallToRemoteServiceB() {
    }

    public CallToRemoteServiceB(Callback<Integer> callback) {
        super(callback);
    }

    @Override
    protected Integer getResultSync() {
        // simulate fetching data from remote service
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 100;
    }
}
