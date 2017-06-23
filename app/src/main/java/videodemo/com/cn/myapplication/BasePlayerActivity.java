package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.File;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import videodemo.com.cn.myapplication.weidget.CustomMediaController;
import videodemo.com.cn.myapplication.weidget.CustomVideoView;

/**
 * 模拟播放器播放视频功能
 */
public abstract class BasePlayerActivity extends Activity implements CustomMediaController.OnMediaScreenChangedListener {

    protected VideoPlusAdapter mVideoPlusAdapter;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected int mWidowPlayerHeight;
    protected FrameLayout mContentView;
    private CustomVideoView mVideoView;
    protected CustomMediaController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //定义全屏参数
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentView = (FrameLayout) findViewById(R.id.root_view);
        mScreenWidth = VenvyUIUtil.getScreenWidth(this);
        mScreenHeight = VenvyUIUtil.getScreenHeight(this);
        mWidowPlayerHeight = VenvyUIUtil.dip2px(this, 195);
        mVideoView = (CustomVideoView) findViewById(R.id.video_view);
        initMediaPlayerController();

        VideoPlusView videoPlusView = initVideoPlusView();
        mVideoPlusAdapter = initVideoPlusAdapter();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getContentView().addView(videoPlusView, layoutParams);
        videoPlusView.setVideoOSAdapter(mVideoPlusAdapter);
        videoPlusView.start();
    }

    protected void initMediaPlayerController() {
        mController = new CustomMediaController(this, true, getContentView());
        mController.setMediaScreenChangedListener(this);
        mVideoView.setMediaController(mController);
    }


    @NonNull
    protected abstract VideoPlusView initVideoPlusView();

    @NonNull
    protected abstract VideoPlusAdapter initVideoPlusAdapter();

    @NonNull
    protected String getMediaUrl() {
        return getSDPath() + "/download/0502.mp4";
    }


    protected VideoPlusAdapter getAdapter() {
        return mVideoPlusAdapter;
    }

    protected FrameLayout getContentView() {
        return mContentView;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVideoPlusAdapter.onResume();
        String url = getMediaUrl();
        mVideoView.setVideoPath(url);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mVideoPlusAdapter.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlusAdapter.onPause();
    }


    @Override
    protected void onStop() {
        mVideoPlusAdapter.onStop();
        mVideoView.pause();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mVideoPlusAdapter.onDestroy();
        mController.onDestory();
        super.onDestroy();
    }

    public void startPlay() {
        mVideoView.start();
    }

    public void pausePlay() {
        mVideoView.pause();
    }

    public void stopPlay() {
        mVideoView.stopPlayback();
    }

    public long getPlayerPosition() {
        return mVideoView.getCurrentPosition();
    }

    public void seekTo(long position) {
        mVideoView.seekTo(position);
    }

    protected String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir == null ? "" : sdDir.toString();
    }

    @Override
    public void screenChanged(CustomMediaController.Screen screen) {
        switch (screen) {
            case PORTRAIT:
                rebuildSize(Math.min(mScreenWidth, mScreenHeight), mWidowPlayerHeight);
                mVideoView.rebuildSize(Math.min(mScreenWidth, mScreenHeight), mWidowPlayerHeight);
                break;
            case PORTRAIT_FULL:
                rebuildSize(Math.min(mScreenWidth, mScreenHeight), Math.max(mScreenWidth, mScreenHeight));
                mVideoView.rebuildSize(Math.min(mScreenWidth, mScreenHeight), Math.max(mScreenWidth, mScreenHeight));
                break;
            case LAND_SCAPE:
                rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min(mScreenWidth, mScreenHeight));
                mVideoView.rebuildSize(Math.max(mScreenWidth, mScreenHeight), Math.min(mScreenWidth, mScreenHeight));
                break;
        }
    }

    public void rebuildSize(int width, int height) {
        ViewGroup.LayoutParams lp = getContentView().getLayoutParams();
        lp.width = width;
        lp.height = height;
        getContentView().setLayoutParams(lp);
    }
}
