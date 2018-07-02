package venvy.com.appdemo.helper;

import cn.com.venvy.common.bean.VideoPlayerSize;
import cn.com.venvy.common.interf.MediaStatus;
import cn.com.venvy.common.interf.VideoOSMediaController;
import venvy.com.appdemo.widget.VideoControllerView;

/**
 * Create by bolo on 08/06/2018
 */
public class MyMediaHelper extends VideoOSMediaController {

    private VideoControllerView.MediaPlayerControl mMediaPlayerControl;
    private VideoControllerView.IVideoControllerListener mVideoControllerListener;
    private VideoPlayerSize mVideoSizes;

    public void setMediaPlayerControl(VideoControllerView.MediaPlayerControl control) {
        mMediaPlayerControl = control;
    }

    public void setVideoController(VideoControllerView.IVideoControllerListener control) {
        mVideoControllerListener = control;
    }

    public void setVideoSize(VideoPlayerSize size) {
        mVideoSizes = size;
    }

    public void screenChange(boolean isPortrait) {
        if (mVideoControllerListener != null) {
            mVideoControllerListener.screenChange(isPortrait);
        }
    }

    /**
     * 控制播放器开始播放
     */
    @Override
    public void start() {
        if (mMediaPlayerControl != null) {
            mMediaPlayerControl.mediaPlayerStart();
        }
    }

    /**
     * 控制播放器暂停
     */
    @Override
    public void pause() {
        if (mMediaPlayerControl != null) {
            mMediaPlayerControl.mediaPlayerPause();
        }
    }

    @Override
    public boolean isPositive() {
        return true;
    }

    /**
     * {@link MediaStatus}
     * @return 当前播放器的状态
     */
    @Override
    public MediaStatus getCurrentMediaStatus() {
        return mMediaPlayerControl == null ? MediaStatus.STOP : mMediaPlayerControl
                .isMediaPlayerPlaying()
                ? MediaStatus.PLAYING : MediaStatus.DEFAULT;
    }

    /**
     * 点播业务必须要实现该方法
     * @return 获取当前播放器的进度
     */
    @Override
    public long getCurrentPosition() {
        if (mMediaPlayerControl != null) {
            return mMediaPlayerControl.getMediaPlayerCurrentPosition();
        } else {
            return -1;
        }
    }

    /**
     * 该方法必须要实现
     * @return  设置 sdk 互动层 VideoOsView 的显示宽高
     */
    @Override
    public VideoPlayerSize getVideoSize() {
        return mVideoSizes;
    }
}
