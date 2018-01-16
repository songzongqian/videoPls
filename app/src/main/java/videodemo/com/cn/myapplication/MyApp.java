package videodemo.com.cn.myapplication;

import android.app.Application;

import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.VideoPlus;

/**
 * Created by lgf on 2017/3/22.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VenvyDebug.changeEnvironmentStatus(VenvyDebug.EnvironmentStatus.RELEASE);
        VenvyUIUtil.runOnUIThreadDelay(new Runnable() {
            @Override
            public void run() {
                // VideoType 中标识本次接入业务类型，具体接入请咨询商务同学
                // VideoType.BOTH 表示接入点播和直播
                // VideoType.LIVEOS 表示只接入直播
                // VideoType.VIDEOOS 表示只接入点播
                // VideoType.OTT 表示只接入OTT
                // VideoType.MALL 表示只接入子商城
                VideoPlus.appCreate(MyApp.this, VideoPlus.VideoType.BOTH, VideoPlus.VideoType.MALL, VideoPlus.VideoType.HUYU);
            }
        }, 3000);
    }
}
