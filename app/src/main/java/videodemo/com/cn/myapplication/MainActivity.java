package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.facebook.drawee.backends.pipeline.Fresco;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.os.VideoOsView;
import cn.com.videopls.venvy.listener.IMediaControlListener;
import cn.com.videopls.venvy.listener.OnCloudWindowShowListener;
import cn.com.videopls.venvy.listener.OnTagShowListener;
import cn.com.videopls.venvy.listener.OnOutsideLinkClickListener;

//Request
public class MainActivity extends Activity implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private VideoView mVideoView;
    private int mPositionWhenPaused = -1;
    private Button btn;
    private VideoOsView videoOsView;

    private int screenWidth;
    private int screenHeightSmall;
    private RelativeLayout.LayoutParams rootParams;
    private RelativeLayout rootView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        initView();
        initVideoOsView();
    }


    public void initView() {
        rootView = (RelativeLayout) findViewById(R.id.root);
        mVideoView = (VideoView) findViewById(R.id.videoview);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoOsView.start();
            }
        });

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenlandscape();
            }
        });
        setLayoutParam();
    }

    public void initVideoOsView() {
        videoOsView = (VideoOsView) findViewById(R.id.video);
        adapter = new MyAdapter();
        videoOsView.setVideoOSAdapter(adapter);
        adapter.onCreate();
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

            final int width = VenvyUIUtil.getScreenWidth(MainActivity.this);
            final int height = VenvyUIUtil.getScreenHeight(MainActivity.this);
            Provider provider = new Provider.Builder()
                    .setAppKey("ryKc0El-Z")//appkey
                    .setHorVideoHeight(Math.min(width, height))//横屏视频的高
                    .setHorVideoWidth(Math.max(width, height))//横屏视频的宽
                    .setVerVideoWidth(Math.min(width, height))//small视频小屏视频的宽
                    .setVerVideoHeight(screenHeightSmall)//small 视频小屏视频的高
                    .setVideoPath("http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129.mp4?v=1102")//视频地址
                    .setVideoType(3)//
                    .setVideoTitle("ttt")//
                    .build();
            return provider;
        }

        /**
         *点播视频控制监听接口，该接口必须提供，否则点播业务无法正常工作
         * @return
         */
        @Override
        public IMediaControlListener buildMediaController() {
            return new MyMediaController();
        }

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
         *跳转url,url可能为null
         * @return
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
         *热点出现监听接口，当视频中出现热点的时候回调用该方法
         * @return
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVideoView.setVideoURI(Uri.parse("http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129.mp4?v=999"));
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

    private void setLayoutParam() {
        screenWidth = VenvyUIUtil.getScreenWidth(this);
        float f = (float) Math.min(VenvyUIUtil.getScreenHeight(MainActivity.this), VenvyUIUtil.getScreenWidth(MainActivity.this)) / (float) Math.max(VenvyUIUtil.getScreenHeight(MainActivity.this), VenvyUIUtil.getScreenWidth(MainActivity.this));
        screenHeightSmall = (int) (f * screenWidth);
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rootParams.width = screenWidth;
        rootParams.height = screenHeightSmall;
        rootView.setLayoutParams(rootParams);
    }

    private void setScreenlandscape() {

        if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置为横屏

            rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            int a = VenvyUIUtil.getScreenWidth(this);
            int b = VenvyUIUtil.getScreenHeight(this);
            rootParams.width = a;
            rootParams.height = b;
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置为竖屏

            rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rootParams.width = screenWidth;
            rootParams.height = screenHeightSmall;
        }
        rootView.setLayoutParams(rootParams);
    }
}
