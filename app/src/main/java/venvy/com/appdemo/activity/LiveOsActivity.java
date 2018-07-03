package venvy.com.appdemo.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.HashMap;

import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.interf.ScreenStatus;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.os.VideoOsView;
import venvy.com.appdemo.adapter.OsAdapter;
import venvy.com.appdemo.widget.VideoControllerView;

public class LiveOsActivity extends BasePlayerActivity {

    private boolean isVerSmallScreen;

    @Override
    protected void onStart() {
        super.onStart();


        if (!mIsInitialized) {

            mVideoPlusAdapter = initVideoPlusAdapter();
            if(mVideoPlusAdapter instanceof OsAdapter){
                ((OsAdapter) mVideoPlusAdapter).setVideoController(this);
            }
            mVideoPlusView.setVideoOSAdapter(mVideoPlusAdapter);
            mVideoPlusView.start();
        }

        if (mSettingsBean.mUserType == PlatformUserInfo.UserType.Anchor) {
            // 主播
            if (!mSettingsBean.isUserApp) {
                // 预设置
                mVideoContentView.removeView(mCustomVideoView);
                clickHy();

            } else if (mSettingsBean.mUserType == PlatformUserInfo.UserType.Anchor) {

                if (mSettingsBean.isFullScreen) {
                    mSettingsBean.isFullScreen = false;
                    // 横屏推流
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
            setVideoFullScreen();
        } else {
            isVerSmallScreen = true;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isVerSmallScreen) {
                mSettingsBean.mScreenStatus = ScreenStatus.SMALL_VERTICAL;
            } else {
                mSettingsBean.mScreenStatus = ScreenStatus.FULL_VERTICAL;
            }
        } else {
            mSettingsBean.mScreenStatus = ScreenStatus.LANDSCAPE;
        }
        if (mVideoPlusAdapter != null) {
            mVideoPlusAdapter.notifyVideoScreenChanged(mSettingsBean.mScreenStatus);
        }
    }

    @Override
    public void screenTypeChange() {
        if (isVerSmallScreen) {
            setVideoFullScreen();
            mSettingsBean.mScreenStatus = ScreenStatus.FULL_VERTICAL;
        } else {
            setVideoVerticalScreen();
            mSettingsBean.mScreenStatus = ScreenStatus.SMALL_VERTICAL;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isVerSmallScreen = !isVerSmallScreen;
    }

    @Override
    public void screenChange(boolean isPortrait) {
        super.screenChange(isPortrait);
    }

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return new VideoOsView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return new OsAdapter(this, mSettingsBean);
    }

    @Override
    protected int getVideoType() {
        return mSettingsBean.mUserType == PlatformUserInfo.UserType.Anchor ? VideoControllerView
                .VIDEO_HY : VideoControllerView.VIDEO_LIVE;
    }

    @Override
    public void clickHy() {
        super.clickHy();
        if (mVideoPlusView == null) {
            return;
        }
        HashMap<String, String> configMap = new HashMap<>();
        String isFullScreen = mSettingsBean.isFullScreen ? "1" : "0";
        configMap.put("fullScreen", isFullScreen);
        Uri rule = Uri.parse("LuaView://defaultLuaView?template=enjoy_main.lua&id=enjoy_main");
        mVideoPlusView.navigation(rule, configMap, null);
    }
}
