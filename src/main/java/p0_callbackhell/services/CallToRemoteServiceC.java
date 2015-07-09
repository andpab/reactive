package p0_callbackhell.services;

import p0_callbackhell.Callback;

public final class CallToRemoteServiceC extends CallToRemoteService<String> {

    private final String dependencyFromA;

    public CallToRemoteServiceC(String dependencyFromA) {
        this.dependencyFromA = dependencyFromA;
    }

    public CallToRemoteServiceC(Callback<String> callback, String dependencyFromA) {
        super(callback);
        this.dependencyFromA = dependencyFromA;
    }

    @Override
    protected String getResultSync() {
        // simulate fetching data from remote service
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "responseC_" + dependencyFromA;
    }

}
