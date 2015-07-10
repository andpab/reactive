package p0_callbackhell_solution.services;

import p0_callbackhell_solution.Callback;

public final class CallToRemoteServiceD extends CallToRemoteService<Integer> {

    private final Integer dependencyFromB;

    public CallToRemoteServiceD(Integer dependencyFromB) {
        this.dependencyFromB = dependencyFromB;
    }

    public CallToRemoteServiceD(Callback<Integer> callback, Integer dependencyFromB) {
        super(callback);
        this.dependencyFromB = dependencyFromB;
    }


    @Override
    protected Integer getResultSync() {
        // simulate fetching data from remote service
        try {
            Thread.sleep(1400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 40 + dependencyFromB;
    }
}
