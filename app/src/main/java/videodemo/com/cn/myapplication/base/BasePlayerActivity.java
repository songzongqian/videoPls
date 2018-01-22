package videodemo.com.cn.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import videodemo.com.cn.myapplication.weidget.CustomVideoView;
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

public abstract class BasePlayerActivity extends Activity implements VideoControllerView.OnMediaScreenChangedListener,VideoControllerView.OnSetingsClickListener {
    protected VideoPlusAdapter mVideoPlusAdapter;
    protected VideoPlusView videoPlusView;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected int mWidowPlayerHeight;
    protected FrameLayout mRootView;
    protected VideoControllerView mController;
    protected CustomVideoView mCustomVideoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        initSettingsValue();
        setContentView(R.layout.activity_base_player);
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

        //Video++实例化
        videoPlusView = initVideoPlusView();
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

    public void verticalTypeChange(VideoControllerView.Screen screen) {
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
        contentview.setFocusable(true);
        contentview.setFocusableInTouchMode(true);
        final PopupWindow popupWindow = new PopupWindow(contentview, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        initButtons(contentview, popupWindow);
        popupWindow.showAtLocation(getContentView(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @NonNull
    protected abstract View getInflate(LayoutInflater inflater);


    protected void initButtons(View view, PopupWindow popupWindow) {

    }

    protected int initEnvButtons(View contentview) {

        final RadioGroup selectEnv = (RadioGroup) contentview.findViewById(R.id.rg_select_env);
        final RadioButton testButton = (RadioButton) contentview.findViewById(R.id.rb_test);
        final RadioButton preButton = (RadioButton) contentview.findViewById(R.id.rb_pre);
        final RadioButton releaseButton = (RadioButton) contentview.findViewById(R.id.rb_release);
        testButton.setChecked(VenvyDebug.isDebug());
        preButton.setChecked(VenvyDebug.isPreView());
        releaseButton.setChecked(VenvyDebug.isRelease());
        return selectEnv.getCheckedRadioButtonId();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    public void startPlay() {
        //Video++主动调用，开始播放
        mCustomVideoView.mediaPlayerStart();
    }

    public void pausePlay() {
        //Video++主动调用，暂停播放器
        mCustomVideoView.mediaPlayerPause();
    }

    public void stopPlay() {
        //Video++主动调用，停止播放器
        mCustomVideoView.mediaPlayerPause();
    }

    public long getPlayerPosition() {
        //Video++主动调用，获取播放器播放时间
        return mCustomVideoView.getMediaPlayerCurrentPosition();
    }

    public void playerSeekTo(long position) {
        //Video++主动调用，快进播放器
        mCustomVideoView.mediaPlayerSeekTo((int) position);
    }
}
