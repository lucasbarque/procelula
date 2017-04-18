package estacio.br.com.procelula.Activities;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;


public class CelulasApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
    }
}
