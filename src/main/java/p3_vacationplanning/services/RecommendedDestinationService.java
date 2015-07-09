package p3_vacationplanning.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecommendedDestinationService extends CallToIterableRemoteService<String> {

    public static final List<String> DESTINATIONS =
            new ArrayList<>(Arrays.asList("Barcelona", "Rio de Janeiro", "Rom", "Dresden", "San Francisco",
                                          "Gründau-Lieblos", "Garmisch-Partenkirchen", "Sylt",
                                          "Rothenburg ob der Tauber", "Prag"));

    @Override
    protected String getResultSync() {
        if (DESTINATIONS.isEmpty()) {
            return null;
        }
        // simulate fetching data from remote service
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return DESTINATIONS.remove((int)(Math.random() * DESTINATIONS.size()));
    }
}
