package p0_callbackhell.services;

import p0_callbackhell.Callback;

public final class CallToRemoteServiceA extends CallToRemoteService<String> {

    public CallToRemoteServiceA() {
    }

    public CallToRemoteServiceA(Callback<String> callback) {
        super(callback);
    }

    @Override
    protected String getResultSync() {
        // simulate fetching data from remote service
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "responseA";
    }
}
