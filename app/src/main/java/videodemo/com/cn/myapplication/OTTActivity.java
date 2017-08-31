package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.interf.IWidgetClickListener;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.ott.VideoOTTView;
import cn.com.videopls.venvy.listener.IMediaControlListener;

public class OTTActivity extends Activity implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private VideoView mVideoView;
    private int mPositionWhenPaused = -1;
    private VideoOTTView videoOsView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ott);
        initView();
        initVideoOsView();
    }

    public void initView() {
        mVideoView = (VideoView) findViewById(R.id.videoview);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoOsView.start();
            }
        });
    }

    public void initVideoOsView() {
        videoOsView = (VideoOTTView) findViewById(R.id.video);
        adapter = new MyAdapter();
        videoOsView.setVideoOSAdapter(adapter);
        adapter.onCreate();
    }

    //用户主动关闭投票，卡牌，红包等信息层
    private void closeVideojjCloudWindow() {
        videoOsView.closeCloudWindow();
    }

    //使得互动层强制获取焦点
    private void setFocusForVideojj() {
        videoOsView.forceFocus();
    }

    //查询互动层是否有view需要获取焦点
    private void checkIfNeedFocus() {
        videoOsView.checkFocus();
    }

    private class MyMediaController implements IMediaControlListener {
        @Override
        public void start() {
            if (mVideoView != null) {
                mVideoView.start();
            }
        }

        @Override
        public void pause() {
            if (mVideoView != null) {
                mVideoView.pause();
            }
        }

        @Override
        public void seekTo(long position) {
            if (mVideoView != null) {
                mVideoView.seekTo(0);
            }
        }

        @Override
        public void restart() {
            if (mVideoView != null) {
                mVideoView.start();
            }
        }

        @Override
        public void stop() {
            if (mVideoView != null) {
                mVideoView.stopPlayback();
            }
        }

        @Override
        public long getCurrentPosition() {
            if (mVideoView != null) {
                return mVideoView.getCurrentPosition();
            } else {
                return -1;
            }
        }

    }


    private class MyAdapter extends VideoPlusAdapter {

        @Override
        public Provider createProvider() {

            final int width = VenvyUIUtil.getScreenWidth(OTTActivity.this);
            final int height = VenvyUIUtil.getScreenHeight(OTTActivity.this);
            Provider provider = new Provider.Builder()
                    .setAppKey("rJ2d86SwZ")//appkey
                    .setHorVideoHeight(Math.min(width, height))//横屏视频的高
                    .setHorVideoWidth(Math.max(width, height))//横屏视频的宽
                    .setVideoPath("http://sdkcdn.videojj.com/flash/player/video/1.mp4")//视频地址
                    .setVideoType(3)//
                    .setVideoTitle("ttt")
                    //是否为精准机型
                    .setmAccurate(false)
                    .build();
            return provider;
        }

        /**
         * 点播视频控制监听接口，该接口必须提供，否则点播业务无法正常工作
         *
         * @return
         */
        @Override
        public IMediaControlListener buildMediaController() {
            return new MyMediaController();
        }

        /***
         * 广告展示监听
         *
         * @return
         */
        @Override
        public IWidgetShowListener buildWidgetShowListener() {
            return new IWidgetShowListener() {
                @Override
                public void onShow(Object o) {
                    WidgetInfo info = (WidgetInfo) o;
                    if (info != null) {
                        //广告类型
                        WidgetInfo.WidgetType widgetType = info.getWidgetType();
                        //广告ID(注：有跳转关系的adId返回值相同)
                        String adId = info.getAdId();
                        //资源ID
                        String resourceId = info.getResourceId();
                        //获取外链H5地址(注：只有 WidgetInfo.WidgetType==INFO类型才会有值)
                        String url = info.getUrl();

                        boolean needFocus = info.getNeedFocus();
                        //needFocus该参数是OTT专用，可以告诉接入方当前出现的热点是否需要获取焦点
                        VenvyLog.i("=====listener==onShow" + "==type=" + widgetType.toString() + "=adID=" + adId + "=resourceId=" + resourceId + "==url==" + url + "==needFocus==" + needFocus);
                    }
                }
            };
        }

        /***
         * 广告关闭监听
         *
         * @return
         */
        @Override
        public IWidgetCloseListener buildWidgetCloseListener() {
            return new IWidgetCloseListener() {
                @Override
                public void onClose(Object o) {
                    WidgetInfo info = (WidgetInfo) o;
                    if (info != null) {
                        //广告类型
                        WidgetInfo.WidgetType widgetType = info.getWidgetType();
                        //广告ID(注：有跳转关系的adId返回值相同)
                        String adId = info.getAdId();
                        //资源ID
                        String resourceId = info.getResourceId();
                        //获取外链H5地址(注：只有 WidgetInfo.WidgetType==INFO类型才会有值)
                        String url = info.getUrl();
                        VenvyLog.i("=====listener==" + "==type=" + widgetType.toString() + "=adID=" + adId + "=resourceId=" + resourceId + "==url==" + url);
                    }
                }
            };
        }

        /***
         * 广告点击监听
         *
         * @return
         */
        @Override
        public IWidgetClickListener buildWidgetClickListener() {
            return new IWidgetClickListener() {
                @Override
                public void onClick(@Nullable Object o) {
                    WidgetInfo info = (WidgetInfo) o;
                    if (info != null) {
                        //广告类型
                        WidgetInfo.WidgetType widgetType = info.getWidgetType();
                        //广告ID(注：有跳转关系的adId返回值相同)
                        String adId = info.getAdId();
                        //资源ID
                        String resourceId = info.getResourceId();
                        //获取外链H5地址(注：只有 WidgetInfo.WidgetType==INFO类型才会有值)
                        String url = info.getUrl();
                        VenvyLog.i("=====listener==" + "==type=" + widgetType.toString() + "=adID=" + adId + "=resourceId=" + resourceId + "==url==" + url);
                    }
                }
            };
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVideoView.setVideoURI(Uri.parse("http://7xr4xn.media1.z0.glb.clouddn.com/snh48sxhsy.mp4"));
//        mVideoView.setVideoURI(Uri.parse("http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129.mp4?v=1146"));
        mVideoView.start();
        adapter.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();
        adapter.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoOsView != null) {
            videoOsView.destroy();
        }
        adapter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }
        adapter.onResume();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


}
