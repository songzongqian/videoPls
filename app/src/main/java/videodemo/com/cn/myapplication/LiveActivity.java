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
import com.facebook.drawee.backends.pipeline.Fresco;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.live.VideoLiveView;
import cn.com.videopls.venvy.listener.IMediaControlListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live);
        Fresco.initialize(this);
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
                    .setUserId("34")
                    .setPlatformId("556c38e7ec69d5bf655a0fb2")
                    .setHorVideoHeight(height)
                    .setHorVideoWidth(width)
                    .setVerVideoHeight(height)
                    .setVerVideoWidth(width)
                    .setVerticalSmallVideoWidth(width)
                    .setVerticalSmallVideoHeight(screenHeightSmall)
                    .setVerticalType(1)
                    //2横竖屏，0竖屏，1是横屏
                    .setDirection(2)
                    .build();
            return provider;
        }

        @Override
        public IMediaControlListener buildMediaController() {
            return null;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mVideoView.setVideoURI(Uri.parse("http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129.mp4?v=999"));
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
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rootParams.width = screenWidth;
        rootParams.height = screenHeightSmall;
        rootView.setLayoutParams(rootParams);
    }


    //设置为横屏全屏
    private void setScreenlandscape() {

        if (VenvyUIUtil.isScreenOriatationPortrait(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置为横屏
        }
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

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
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

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
        rootParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rootParams.width = screenWidth;
        rootParams.height = screenHeightSmall;
        rootView.setLayoutParams(rootParams);
    }

}
