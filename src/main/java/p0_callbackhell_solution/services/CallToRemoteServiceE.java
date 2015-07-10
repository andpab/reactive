package p0_callbackhell_solution.services;

import p0_callbackhell_solution.Callback;

public final class CallToRemoteServiceE extends CallToRemoteService<Integer> {

    private final Integer dependencyFromB;

    public CallToRemoteServiceE(Integer dependencyFromB) {
        this.dependencyFromB = dependencyFromB;
    }

    public CallToRemoteServiceE(Callback<Integer> callback, Integer dependencyFromB) {
        super(callback);
        this.dependencyFromB = dependencyFromB;
    }


    @Override
    protected Integer getResultSync() {
        // simulate fetching data from remote service
        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5000 + dependencyFromB;
    }
}
