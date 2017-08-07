package videodemo.com.cn.myapplication;

import android.app.Application;

import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.VideoPlus;

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
                VideoPlus.appCreate(MyApp.this, VideoPlus.VideoType.BOTH);
            }
        }, 3000);
    }
}
