package videodemo.com.cn.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import videodemo.com.cn.myapplication.weidget.CustomVideoView;
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

public abstract class BasePlayerActivity extends Activity implements VideoControllerView.OnMediaScreenChangedListener, VideoControllerView.OnSetingsClickListener {

    protected VideoPlusAdapter mVideoPlusAdapter;
    protected VideoPlusView videoPlusView;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected int mWidowPlayerHeight;
    protected FrameLayout mRootView;
    protected VideoControllerView mController;
    protected CustomVideoView mCustomVideoView;
    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        initSettingsValue();
        mContentView = LayoutInflater.from(this).inflate(R.layout.activity_base_player, null);
        setContentView(mContentView);

        mScreenWidth = VenvyUIUtil.getScreenWidth(this);
        mScreenHeight = VenvyUIUtil.getScreenHeight(this);
        mWidowPlayerHeight = VenvyUIUtil.dip2px(this, 195);
        mRootView = (FrameLayout) findViewById(R.id.root);

        mCustomVideoView = new CustomVideoView(this);
        mRootView.addView(mCustomVideoView);

        if (this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min(mScreenWidth,
                    mScreenHeight));
            mCustomVideoView.rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min
                    (mScreenWidth, mScreenHeight));

        }
        initMediaController();
        initView();
    }

    protected void initView() {
        videoPlusView = initVideoPlusView();
        //Video++实例化
        mVideoPlusAdapter = initVideoPlusAdapter();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getContentView().addView(videoPlusView, layoutParams);
        videoPlusView.setVideoOSAdapter(mVideoPlusAdapter);
        //Video++中在一段视频播放中只调用一次start()，如果同页面切换视频，请先调用stop(),然后再调用start()
        videoPlusView.start();
    }


    @NonNull
    protected abstract VideoPlusView initVideoPlusView();

    @NonNull
    protected abstract VideoPlusAdapter initVideoPlusAdapter();


    @Override
    protected void onResume() {
        super.onResume();
        //Video++调用
        mVideoPlusAdapter.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Video++调用
        mVideoPlusAdapter.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Video++调用
        mVideoPlusAdapter.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Video++调用
        mVideoPlusAdapter.onPause();
    }


    @Override
    protected void onStop() {
        //Video++调用
        mVideoPlusAdapter.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Video++调用
        mVideoPlusAdapter.onDestroy();
        videoPlusView.destroy();

        //播放控制器销毁，对接忽略
        if (mCustomVideoView != null) {
            mCustomVideoView.stopPlaying();
        }
        super.onDestroy();
    }

    protected FrameLayout getContentView() {
        return mRootView;
    }

    protected void initMediaController() {
        mController = new VideoControllerView(this, this);
        mController.setMediaScreenChangedListener(this);
        mController.setmOnSettingsClickListener(this);
        mCustomVideoView.setMediaController(mController);
    }

    protected VideoPlusAdapter getAdapter() {
        return mVideoPlusAdapter;
    }

    protected boolean belowSDK21() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 模拟屏幕切换
     */
    @Override
    public void screenChanged(VideoControllerView.Screen screen) {
        switch (screen) {
            case PORTRAIT:
                rebuildSize(Math.min(mScreenWidth, mScreenHeight), mWidowPlayerHeight);
                mCustomVideoView.rebuildSize(Math.min(mScreenWidth, mScreenHeight), mWidowPlayerHeight);
                break;
            case PORTRAIT_FULL:
                rebuildSize(Math.min(mScreenWidth, mScreenHeight), Math.max(mScreenWidth, mScreenHeight));
                mCustomVideoView.rebuildSize(Math.min(mScreenWidth, mScreenHeight), mWidowPlayerHeight);
                break;
            case LAND_SCAPE:
                rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min(mScreenWidth, mScreenHeight));
                mCustomVideoView.rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min(mScreenWidth, mScreenHeight));
                break;
        }
    }

    public void rebuildSize(int width, int height) {
        ViewGroup.LayoutParams lp = getContentView().getLayoutParams();
        lp.width = width;
        lp.height = height;
        getContentView().setLayoutParams(lp);
    }

    @Override
    public void settingsClick() {
        initiatePopupWindow();
    }

    protected void initSettingsValue() {

    }

    private void initiatePopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = getInflate(inflater);
        final PopupWindow popupWindow = new PopupWindow(contentview, FrameLayout.LayoutParams
                .WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        initButtons(contentview, popupWindow);
        popupWindow.showAtLocation(getContentView(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @NonNull
    protected abstract View getInflate(LayoutInflater inflater);


    protected void initButtons(View view, PopupWindow popupWindow) {

    }

    protected void initEnvButtons(View contentview) {

        final RadioButton testButton = (RadioButton) contentview.findViewById(R.id.rb_test);
        final RadioButton preButton = (RadioButton) contentview.findViewById(R.id.rb_pre);
        final RadioButton releaseButton = (RadioButton) contentview.findViewById(R.id.rb_release);
        testButton.setChecked(VenvyDebug.isDebug());
        preButton.setChecked(VenvyDebug.isPreView());
        releaseButton.setChecked(VenvyDebug.isRelease());
    }

    //更新相关的参数
    protected void updateSettingsBeans(){

    }
    protected void debugToggle(int id) {
        if (id == R.id.rb_test) {
            VenvyDebug.changeEnvironmentStatus(VenvyDebug.EnvironmentStatus.DEBUG);
        } else if (id == R.id.rb_pre) {
            VenvyDebug.changeEnvironmentStatus(VenvyDebug.EnvironmentStatus.PREVIEW);
        } else if (id == R.id.rb_release) {
            VenvyDebug.changeEnvironmentStatus(VenvyDebug.EnvironmentStatus.RELEASE);
        }
    }
}
