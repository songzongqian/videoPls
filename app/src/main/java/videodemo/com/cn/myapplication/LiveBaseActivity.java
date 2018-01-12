package videodemo.com.cn.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;

import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.live.VideoLiveView;
import videodemo.com.cn.myapplication.base.BasePlayerActivity;
import videodemo.com.cn.myapplication.bean.SettingsBean;
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

public class LiveBaseActivity extends BasePlayerActivity {


    protected SettingsBean mSettingsBean;
    protected VideoLiveView mVideoLiveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return mVideoLiveView = new VideoLiveView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return null;
    }

    /**
     * Video++直播PlatformId
     */
    protected static String getPlatformId() {
        if (VenvyDebug.isDebug()) {
            return "556c38e7ec69d5bf655a0fb2";
        }
        return "575e6e087c395e0501980c89";
    }

    //初始化相关的默认参数
    @Override
    protected void initSettingsValue() {
        mSettingsBean = new SettingsBean();
        mSettingsBean.mRoomId = "34";
        mSettingsBean.mPlatformId = getPlatformId();
    }

    @Override
    protected void initMediaController() {
        super.initMediaController();
        mController.isLive(true);
    }

    @Override
    public void screenChanged(VideoControllerView.Screen screen) {
        super.screenChanged(screen);
        switch (screen) {
            case PORTRAIT:
                //屏幕切换调用，切换竖屏小屏
                getAdapter().notifyLiveVerticalScreen(1);
                break;
            case PORTRAIT_FULL:
                //屏幕切换调用，切换竖屏全屏
                getAdapter().notifyLiveVerticalScreen(0);
                break;

            case LAND_SCAPE:

                break;
        }
    }

}
