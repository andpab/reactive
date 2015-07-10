package p1_helloworld_solution;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;

public class HelloSaxoniaService {

    public Observable<String> getSaxoniaLocationNames() {
        //TODO asynchronen Strom aller Saxonia-Standorte zurückgeben
        return Observable.<String>create(observer -> {
            Arrays.asList("Dresden","Görlitz","München")
                    .forEach(observer::onNext);
            observer.onCompleted();
        }).subscribeOn(Schedulers.io());
    }

}
