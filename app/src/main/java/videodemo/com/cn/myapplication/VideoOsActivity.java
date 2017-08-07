package videodemo.com.cn.myapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.interf.IWidgetClickListener;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.os.VideoOsView;
import cn.com.videopls.venvy.listener.IMediaControlListener;
import cn.com.videopls.venvy.listener.OnCloudWindowShowListener;
import cn.com.videopls.venvy.listener.OnOutsideLinkClickListener;
import cn.com.videopls.venvy.listener.OnTagShowListener;

public class VideoOsActivity extends BasePlayerActivity {


    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return new VideoOsView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return new MyAdapter();
    }
    @NonNull
    @Override
    protected String getMediaUrl() {
        return getVideoPath();
    }
    @Override
    protected void initMediaPlayerController() {
        super.initMediaPlayerController();
        //点播不支持竖屏全屏
        mController.isLive(false);
    }
    /**
     * 申请的Video++ KEY
     *
     * @return
     */
    private String getAppKey() {
        if (VenvyDebug.getInstance().isDebug()) {
            return "ryKc0El-Z";
        }
        return "ryZSzdBWZ";
    }


    /**
     * Video path或者Video ID
     *
     * @return
     */
    private String getVideoPath() {
        if (VenvyDebug.getInstance().isDebug()) {
            //return "http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129.mp4?v=1102";
            return "http://sdkcdn.videojj.com/flash/player/video/1.mp4?v=5";
        }
        return "http://sdkcdn.videojj.com/flash/player/video/1.mp4?v=5";
    }


    private class MyAdapter extends VideoPlusAdapter {

        @Override
        public Provider createProvider() {

            return new Provider.Builder()
                    .setAppKey(getAppKey())//appkey
                    .setHorVideoHeight(Math.min(mScreenWidth, mScreenHeight))//横屏视频的高
                    .setHorVideoWidth(Math.max(mScreenWidth, mScreenHeight))//横屏视频的宽
                    .setVerVideoWidth(Math.min(mScreenWidth, mScreenHeight))//small视频小屏视频的宽
                    .setVerVideoHeight(mWidowPlayerHeight)//small 视频小屏视频的高
                    .setVideoPath(getVideoPath())//视频地址
                    .setVideoType(VideoType.LOCAL_VIDEO)// 视频类型
                    .setVideoTitle("ttt")// 视频标题
                    .build();
        }
        /**
         * 点播视频控制监听接口，该接口必须提供，否则点播业务无法正常工作
         * 此接口是控制播放器行为
         */
        @Override
        public IMediaControlListener buildMediaController() {
            return new IMediaControlListener() {

                /**
                 * 开始播放
                 */
                @Override
                public void start() {
                    startPlay();
                }

                /**
                 * 暂停播放
                 */
                @Override
                public void pause() {
                    pausePlay();
                }

                /**
                 * 继续播放
                 */
                @Override
                public void restart() {
                    startPlay();
                }

                /**
                 * 拖动
                 * position 指拖动到视频哪个位置，单位为毫秒
                 */
                @Override
                public void seekTo(long position) {
                    VideoOsActivity.this.seekTo(position);
                }

                /**
                 * 停止播放
                 */
                @Override
                public void stop() {
                    stopPlay();
                }

                /**
                 * 获取播放器当前播放时间，单位ms
                 */
                @Override
                public long getCurrentPosition() {
                    return getPlayerPosition();
                }
            };
        }

        /**
         * 云窗显示回调接口
         */
        @Override
        public OnCloudWindowShowListener buildCloudWindowShowListener() {
            return new OnCloudWindowShowListener() {
                @Override
                public void onCloudWindowShow() {
                    VenvyLog.e("==========333云窗显示======");
                }
            };
        }

        /**
         * 用来监听页面元素的点击事件,当点击图片，图文链接等元素时会回调此方法，获取对应页面元素的
         * 跳转url,url可能为null
         */
        @Override
        public OnOutsideLinkClickListener buildOutsideLinkListener() {
            return new OnOutsideLinkClickListener() {
                @Override
                public void onLinkShow(String mUrl) {
                    VenvyLog.e("==========333外链显示======");
                }

                @Override
                public void onLinkClose() {
                    VenvyLog.e("==========333外链关闭======");
                }
            };
        }

        /**
         * 热点出现监听接口，当视频中出现热点的时候回调用该方法
         */
        @Override
        public OnTagShowListener buildTagShowListener() {
            return new OnTagShowListener() {
                @Override
                public void onTagShow() {
                    VenvyLog.e("==========333热点显示======");
                }
            };
        }

        /**
         * 点击监听
         * @return
         */
        public IWidgetClickListener<WidgetInfo> buildWidgetClickListener() {
            return new IWidgetClickListener<WidgetInfo>() {

                @Override
                public void onClick(@Nullable WidgetInfo widgetInfo) {

                }
            };
        }


        public IWidgetShowListener<WidgetInfo> buildWidgetShowListener() {
            return new IWidgetShowListener<WidgetInfo>() {
                @Override
                public void onShow(WidgetInfo widgetInfo) {
                    //展示监听
                }
            };
        }

        public IWidgetCloseListener<WidgetInfo> buildWidgetCloseListener() {
            return new IWidgetCloseListener<WidgetInfo>() {
                @Override
                public void onClose(WidgetInfo widgetInfo) {

                }
            };
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注意父类中的调用
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //注意父类中的调用
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注意父类中的调用
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注意父类中的调用
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注意父类中的调用
    }
}
