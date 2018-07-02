package venvy.com.appdemo.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import cn.com.venvy.common.interf.ScreenStatus;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import venvy.com.appdemo.R;
import venvy.com.appdemo.bean.SettingsBean;
import venvy.com.appdemo.utils.ConfigUtil;
import venvy.com.appdemo.widget.CustomVideoView;
import venvy.com.appdemo.widget.VideoControllerView;

public abstract class BasePlayerActivity extends AppCompatActivity implements VideoControllerView
        .IVideoControllerListener {

    protected VideoPlusAdapter mVideoPlusAdapter;
    protected VideoPlusView mVideoPlusView;
    protected int mWidowPlayerHeight;
    protected ViewGroup mRootView;
    protected VideoControllerView mController;
    protected CustomVideoView mCustomVideoView;
    protected SettingsBean mSettingsBean = new SettingsBean();
    protected FrameLayout mVideoContentView;
    protected boolean mIsInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = (ViewGroup) LayoutInflater.from(this)
                .inflate(R.layout.activity_base_player, null);
        setContentView(mRootView);

        mSettingsBean = ConfigUtil.getSettingCache(this);

        initMediaPlayer();

        //Video++实例化
        mVideoPlusView = initVideoPlusView();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRootView.addView(mVideoPlusView, layoutParams);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mCustomVideoView.isMediaPlayerPlaying()) {
            mController.onConfigurationChanged(newConfig);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
                || mSettingsBean.mScreenStatus == ScreenStatus.FULL_VERTICAL) {
            setVideoFullScreen();
        } else {
            setVideoVerticalScreen();
        }
    }

    @NonNull
    protected abstract VideoPlusView initVideoPlusView();

    @NonNull
    protected abstract VideoPlusAdapter initVideoPlusAdapter();

    protected abstract int getVideoType();

    protected void initMediaPlayer() {
        mWidowPlayerHeight = VenvyUIUtil.dip2px(this, 195);
        mVideoContentView = (FrameLayout) findViewById(R.id.root);
        mCustomVideoView = new CustomVideoView(this);
        mCustomVideoView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, mWidowPlayerHeight));
        mVideoContentView.addView(mCustomVideoView);
        mController = new VideoControllerView(this);
        mController.setVideoControllerListener(this);
        mController.setVideoType(getVideoType());
        mCustomVideoView.setMediaController(mController);
    }


    protected void setVideoFullScreen() {
        ViewGroup.LayoutParams params = mCustomVideoView.getLayoutParams();
        if (params != null) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mCustomVideoView.setLayoutParams(params);
        }
    }

    protected void setVideoVerticalScreen() {
        ViewGroup.LayoutParams params = mCustomVideoView.getLayoutParams();
        if (params != null) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = mWidowPlayerHeight;
            mCustomVideoView.setLayoutParams(params);
        }
    }

    @Override
    public void screenChange(boolean isPortrait) {
        //如果是竖屏
        if (!isPortrait) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void screenTypeChange() {

    }

    @Override
    public void clickMall() {

    }

    @Override
    public void clickHy() {

    }
}
