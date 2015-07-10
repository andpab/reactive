package p3_vacationplanning_solution.services;

public class DestinationData {

    private final String destination;
    private final Integer quote;
    private final Integer temperature;

    public DestinationData(String destination, Integer quote) {
        this(destination, quote, null);
    }

    public DestinationData(String destination, Integer quote, Integer temperature) {
        this.destination = destination;
        this.quote = quote;
        this.temperature = temperature;
    }

    public String getDestination() {
        return destination;
    }

    public Integer getQuote() {
        return quote;
    }

    public Integer getTemperature() {
        return temperature;
    }

}
