package videodemo.com.cn.myapplication.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import videodemo.com.cn.myapplication.weidget.CustomVideoView;
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

public abstract class BasePlayerActivity extends Activity implements VideoControllerView.OnMediaScreenChangedListener {

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
            rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min(mScreenWidth, mScreenHeight));
            mCustomVideoView.rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min(mScreenWidth, mScreenHeight));

        }
        initMediaController();

        //Video++实例化
        videoPlusView = initVideoPlusView();
        mVideoPlusAdapter = initVideoPlusAdapter();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        videoPlusView.stop();
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
        mCustomVideoView.setMediaController(mController);
    }

    protected VideoPlusAdapter getAdapter() {
        return mVideoPlusAdapter;
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

    protected void initSettingsValue() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
