package videodemo.com.cn.myapplication;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;

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
                // VideoType 中标识本次接入业务类型，具体接入请咨询商务同学
                // VideoType.LIVEOS 表示只接入直播
                // VideoType.VIDEOOS 表示只接入点播
                // VideoType.OTT 表示只接入OTT
                // VideoType.MALL 表示只接入子商城
                // VideoPlus.VideoType.HUYU 互娱
                VideoPlus.appCreate(MyApp.this, VideoPlus.VideoType.HUYU,VideoPlus.VideoType
                        .LIVEOS, VideoPlus.VideoType.MALL);
                QbSdk.forceSysWebView();
            }
        }, 3000);
    }
}
