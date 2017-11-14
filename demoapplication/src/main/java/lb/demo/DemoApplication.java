package lb.demo;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liubo on 2017/9/27.
 */

public class DemoApplication extends Application {

    public static boolean debug;
    public static Map<String, String> stationMap;

    @Override
    public void onCreate() {
        super.onCreate();

        debug = false;
        stationMap = new HashMap<>();
    }
}
