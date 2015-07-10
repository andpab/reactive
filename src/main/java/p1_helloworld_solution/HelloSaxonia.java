package p1_helloworld_solution;

public class HelloSaxonia {

    public static void main(String[] args) {
        greetSaxoniaAsync();

        System.out.println("Hauptprogramm läuft jetzt weiter ...");
        try {
            // Hauptprogramm tut etwas
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hauptprogramm ist fertig.");
    }

    public static void greetSaxoniaAsync() {
        HelloSaxoniaService helloSaxoniaService = new HelloSaxoniaService();

        //TODO asynchron den Strom von Saxonia-Standorten verarbeiten,
        //TODO dabei jeden Standort einzeln begrüßen 
        //TODO und am Ende allen viel Spass wünschen
        helloSaxoniaService.getSaxoniaLocationNames()
                .map((location) -> "Hallo " + location + "!")
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Viel Spass beim Saxoniatag!"));
        System.out.println("Exiting ...");
    }
}
