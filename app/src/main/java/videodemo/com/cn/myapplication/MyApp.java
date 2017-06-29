package videodemo.com.cn.myapplication;

import android.app.Application;

import cn.com.venvy.VideoPlus;

/**
 * Created by lgf on 2017/3/22.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VideoPlus.appCreate(this);
    }
}
