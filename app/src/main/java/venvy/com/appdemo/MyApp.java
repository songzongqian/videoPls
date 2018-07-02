package venvy.com.appdemo;

import android.app.Application;

import cn.com.venvy.common.debug.DebugStatus;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.videopls.pub.VideoPlus;

/**
 * Created by lgf on 2017/3/22.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DebugStatus.changeEnvironmentStatus(DebugStatus.EnvironmentStatus.DEBUG);
        VenvyLog.needLog = true;
        VideoPlus.appCreate(MyApp.this);
    }
}
