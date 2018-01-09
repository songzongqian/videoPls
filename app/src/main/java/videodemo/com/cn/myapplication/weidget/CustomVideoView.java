package videodemo.com.cn.myapplication.weidget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;


/**
 * Created by liyangyang on 2017/12/28.
 */

public class CustomVideoView extends SurfaceView implements VideoControllerView.MediaPlayerControl, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    protected MediaPlayer mMediaPlayer = null;
    private boolean mIsPrepared;
    private int mCurrentPosition = 0;
    SurfaceHolder mSurfaceHolder = null;
    private Context mContext;
    private VideoControllerView mMediaController;

    public CustomVideoView(Context context) {
        this(context, null, 0);
        this.mContext = context;
        initView();
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initView() {

        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void mediaPlayerStart() {
        mMediaPlayer.start();
    }

    @Override
    public void mediaPlayerPause() {
        mMediaPlayer.pause();
    }

    @Override
    public int getMediaPlayerDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public int getMediaPlayerCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return -1;
        }
    }

    @Override
    public void mediaPlayerSeekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isMediaPlayerPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    @Override
    public int getMediaPlayerBufferPercentage() {
        return 0;
    }

    @Override
    public boolean mediaPlayerCanPause() {
        return true;
    }

    @Override
    public void mediaPlayerToggleFullScreen() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPlay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceHolder = null;
        stopPlaying();
    }

    public void startPlay() {
        if (mSurfaceHolder == null) {
            return;
        }
        stopPlaying();
        // 关闭系统音乐声音
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mIsPrepared = false;
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setDataSource(mContext, Uri.parse("http://7xr4xn.media1.z0.glb.clouddn.com/snh48sxhsy.mp4?v=0"));
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopPlaying() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mCurrentPosition = mMediaPlayer.getCurrentPosition();
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void rebuildSize(int width, int height) {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.width = width;
        lp.height = height;
        this.setLayoutParams(lp);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        this.setMediaController(mMediaController);
        mIsPrepared = true;
        if (mMediaPlayer != null && mIsPrepared) {
            mMediaPlayer.seekTo(mCurrentPosition);
            mMediaPlayer.start();
        }
    }

    public void setMediaController(VideoControllerView controller) {
        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            ViewGroup anchorView = (ViewGroup) this.getParent();
            mMediaController.setAnchorView(anchorView);
            mMediaController.show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mMediaController.isShowing()) {
                mMediaController.hide();
            } else {
                mMediaController.show();
            }
        }
        return true;
    }
}
