package videodemo.com.cn.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;

import cn.com.live.videopls.venvy.entry.listeners.OnViewClickListener;
import cn.com.live.videopls.venvy.entry.listeners.WedgeListener;
import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.live.VideoLiveView;
import videodemo.com.cn.myapplication.weidget.CustomMediaController;

public class LiveActivity extends BasePlayerActivity {

    private String mRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoomId = getIntent().getStringExtra("roomId");
    }

    @NonNull
    @Override
    protected String getMediaUrl() {
        return "http://7xr5j6.com1.z0.glb.clouddn.com/hunantv0129" +
                ".mp4?v=999";
    }

    @Override
    protected void initMediaPlayerController() {
        super.initMediaPlayerController();
        mController.isLive(true);
    }

    /**
     * Video++直播PlatformId
     *
     * @return
     */
    private String getPlatformId() {
        if (VenvyDebug.getInstance().isDebug()) {
            return "556c38e7ec69d5bf655a0fb2";
        }
        return "575e6e087c395e0501980c89";
    }

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return new VideoLiveView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return new LiveAdapter();
    }


    //竖屏小屏adapter
    private class LiveAdapter extends VideoPlusAdapter {

        @Override
        public Provider createProvider() {

            return new Provider.Builder()
                    .setUserId(mRoomId)//roomId 或者userId
                    .setPlatformId(getPlatformId())//videojj直播后台平台Id
                    .setHorVideoWidth(Math.max(mScreenWidth, mScreenHeight))//横屏视频的宽
                    .setHorVideoHeight(Math.min(mScreenWidth, mScreenHeight))//横屏视频的高
                    .setVerticalFullVideoWidth(Math.min(mScreenWidth, mScreenHeight))//竖屏全屏视频的宽
                    .setVerticalFullVideoHeight(Math.max(mScreenWidth, mScreenHeight))//竖屏全屏视屏的高
                    .setVerVideoWidth(Math.min(mScreenWidth, mScreenHeight))//small视频小屏视频的宽
                    .setVerVideoHeight(mWidowPlayerHeight)//small 视频小屏视频的高
                    .setVerticalType(1)//1 竖屏小屏，0竖屏全屏
                    .setDirection(2) //2横竖屏，0竖屏，1是横屏
                    .setIsMango() //是否为芒果配置
                    //.setIsPear() //是否是梨视频配置
                    .build();
        }

        /**
         * 中插视频播放监听接口，用来监听中插视频的播放和结束，
         * 如果不用中插功能的话，不用重写buildWedgeListener
         *
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
         * 跳转url,url可能为null
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
    public void screenChanged(CustomMediaController.Screen screen) {
        super.screenChanged(screen);
        switch (screen) {
            case PORTRAIT:
                //竖屏小屏
                getAdapter().notifyLiveVerticalScreen(1);
                break;
            case PORTRAIT_FULL:
                //竖屏全屏
                getAdapter().notifyLiveVerticalScreen(0);
                break;

            case LAND_SCAPE:

                break;
        }
    }
}
