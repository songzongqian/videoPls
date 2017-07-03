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
    protected VideoPlusView videoPlusView;

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

        //Video++实例化
        videoPlusView = initVideoPlusView();
        mVideoPlusAdapter = initVideoPlusAdapter();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getContentView().addView(videoPlusView, layoutParams);
        videoPlusView.setVideoOSAdapter(mVideoPlusAdapter);
        //Video++中在一段视频播放中只调用一次start()，如果同页面切换视频，请先调用stop(),然后再调用start()
        videoPlusView.start();
    }

    /**
     * 播放器控制，对接时忽略
     */
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
        //Video++调用
        mVideoPlusAdapter.onResume();

        //开始播放视频，对接忽略
        String url = getMediaUrl();
        mVideoView.setVideoPath(url);
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

        //播放器暂停，对接忽略
        mVideoView.pause();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Video++调用
        mVideoPlusAdapter.onDestroy();
        videoPlusView.destroy();

        //播放控制器销毁，对接忽略
        mController.onDestory();
        super.onDestroy();
    }

    public void startPlay() {
        //Video++主动调用，开始播放
        mVideoView.start();
    }

    public void pausePlay() {
        //Video++主动调用，暂停播放器
        mVideoView.pause();
    }

    public void stopPlay() {
        //Video++主动调用，停止播放器
        mVideoView.stopPlayback();
    }

    public long getPlayerPosition() {
        //Video++主动调用，获取播放器播放时间
        return mVideoView.getCurrentPosition();
    }

    public void seekTo(long position) {
        //Video++主动调用，快进播放器
        mVideoView.seekTo(position);
    }

    /**
     * 对接忽略
     */
    protected String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir == null ? "" : sdDir.toString();
    }

    /**
     * 模拟屏幕切换
     */
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
