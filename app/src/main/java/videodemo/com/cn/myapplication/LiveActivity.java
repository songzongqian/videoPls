package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import cn.com.live.videopls.venvy.entry.listeners.OnViewClickListener;
import cn.com.live.videopls.venvy.entry.listeners.WedgeListener;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.live.VideoLiveView;

public class LiveActivity extends Activity implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private VideoView mVideoView;
    private Button btnVerFull, btnHorFull, btnVerSmall;
    private VideoLiveView videoLiveView;
    private int screenWidth;
    private int screenHeightSmall;
    private RelativeLayout.LayoutParams rootParams;
    private RelativeLayout rootView;
    LiveAdapter liveAdatper;
    private String mRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live);
        mRoomId = getIntent().getStringExtra("roomId");
        initView();
        initVideoLiveView();
    }

    private void initVideoLiveView() {
        videoLiveView = (VideoLiveView) findViewById(R.id.venvyLive);
        liveAdatper = new LiveAdapter();
        videoLiveView.setVideoOSAdapter(liveAdatper);
        videoLiveView.start();

    }

    public void initView() {
        rootView = (RelativeLayout) findViewById(R.id.root);
        mVideoView = (VideoView) findViewById(R.id.videoview);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });

        //横屏全屏按钮
        btnHorFull = (Button) findViewById(R.id.btn_hor_full);
        btnHorFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VenvyUIUtil.isScreenOriatationPortrait(v.getContext())) {
                    //如果从竖屏（包括竖屏小屏和竖屏全屏）切换到横屏
                    setScreenlandscape();
                }

            }
        });

        //竖屏全屏按钮
        btnVerFull = (Button) findViewById(R.id.btn_ver_full);
        btnVerFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是竖屏
                if (VenvyUIUtil.isScreenOriatationPortrait(v.getContext())) {
                    liveAdatper.notifyLiveVerticalScreen(0);
                }
                setScreenVerscape();

            }
        });

        //竖屏小屏
        btnVerSmall = (Button) findViewById(R.id.btn_ver_small);
        btnVerSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveAdatper.notifyLiveVerticalScreen(1);
                setScreenVerSmallscape();
            }
        });

        setLayoutParam();
    }

    //竖屏小屏adapter
    private class LiveAdapter extends VideoPlusAdapter {

        @Override
        public Provider createProvider() {
            final int width = VenvyUIUtil.getScreenWidth(LiveActivity.this);
            final int height = VenvyUIUtil.getScreenHeight(LiveActivity.this);

            Provider provider = new Provider.Builder()
                    .setUserId(mRoomId)//roomId 或者userId
                    .setPlatformId("556c38e7ec69d5bf655a0fb2")//videojj直播后台平台Id
                    .setHorVideoHeight(height)//横屏视频的高
                    .setHorVideoWidth(width)//横屏视频的宽
                    .setVerVideoHeight(screenHeightSmall)//small 视频小屏视频的高
                    .setVerVideoWidth(width)//small视频小屏视频的宽
                    .setVerticalFullVideoWidth(height)//Full 视频全屏视频的高
                    .setVerticalFullVideoHeight(width)//视频全屏视屏的宽
                    .setVerticalType(1)//1 竖屏小屏，0竖屏全屏
                    .setDirection(2) //2横竖屏，0竖屏，1是横屏
                    .setIsPear()
                    .build();
            return provider;
        }

        /**
         *中插视频播放监听接口，用来监听中插视频的播放和结束，
         * 如果不用中插功能的话，不用重写buildWedgeListener
         * @return WedgeListener
         */
        public WedgeListener buildWedgeListener() {
            return new WedgeListener() {
                @Override
                public void onStart() {
                    VenvyLog.i("-------中插开始播放--------");
                }

                @Override
                public void onFinish() {
                    VenvyLog.i("******中插结束播放******");
                }
            };
        }

        /**
         * 用来监听页面元素的点击事件,当点击图片，图文链接等元素时会回调此方法，获取对应页面元素的
         *跳转url,url可能为null
         * @return
         */
        @Override
        public OnViewClickListener buildOnViewClickListener() {
          return new OnViewClickListener() {
              @Override
              public void onClick(String url) {
                  VenvyLog.i("---点击图片等页面元素的时候返回对应的url----");
              }
          };
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mVideoView.setVideoURI(Uri.parse("http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129" +
                ".mp4?v=999"));
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.stopPlayback();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoLiveView.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        float d = 9f;
        float e = 16f;
        float f = d / e;
        screenHeightSmall = (int) (f * screenWidth);
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rootParams.width = screenWidth;
        rootParams.height = screenHeightSmall;
        rootView.setLayoutParams(rootParams);
    }


    //设置为横屏全屏
    private void setScreenlandscape() {

        if (VenvyUIUtil.isScreenOriatationPortrait(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置为横屏
        }
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        int a = VenvyUIUtil.getScreenWidth(this);
        int b = VenvyUIUtil.getScreenHeight(this);
        rootParams.width = a;
        rootParams.height = b;
        rootView.setLayoutParams(rootParams);

    }

    //设置为竖屏全屏
    private void setScreenVerscape() {

        if (!VenvyUIUtil.isScreenOriatationPortrait(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置为横屏
        }
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        int a = VenvyUIUtil.getScreenWidth(this);
        int b = VenvyUIUtil.getScreenHeight(this);
        rootParams.width = a;
        rootParams.height = b;
        rootView.setLayoutParams(rootParams);

    }

    //设置为竖屏小屏
    private void setScreenVerSmallscape() {
        if (!VenvyUIUtil.isScreenOriatationPortrait(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置为竖屏
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置为横屏
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rootParams.width = screenWidth;
        rootParams.height = screenHeightSmall;
        rootView.setLayoutParams(rootParams);
    }

}
