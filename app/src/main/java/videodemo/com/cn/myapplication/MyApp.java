package videodemo.com.cn.myapplication;

import android.app.Application;

import cn.com.venvy.VideoPlus;
import cn.com.venvy.common.utils.VenvyUIUtil;

/**
 * Created by lgf on 2017/3/22.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VenvyUIUtil.runOnUIThreadDelay(new Runnable() {
            @Override
            public void run() {
                VideoPlus.appCreate(MyApp.this);
            }
        }, 3000);
    }
}
