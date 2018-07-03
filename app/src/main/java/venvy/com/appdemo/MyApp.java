package venvy.com.appdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

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
        Fresco.initialize(this);

        // 打开 video++ 日志
        VenvyLog.needLog = true;
        // 切换成 debug
        DebugStatus.changeEnvironmentStatus(DebugStatus.EnvironmentStatus.DEBUG);
        // 初始化 video++
        VideoPlus.appCreate(MyApp.this);
    }
}
