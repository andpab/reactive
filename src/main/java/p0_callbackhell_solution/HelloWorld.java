package p0_callbackhell_solution;

import rx.Observable;
import rx.Observer;

public class HelloWorld {
    public static void main(String[] args) {
        Observable.just("Hello","World").subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("!");
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.print(s);
            }
        });
    }
}
