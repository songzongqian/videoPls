package videodemo.com.cn.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import cn.com.live.videopls.venvy.entry.listeners.OnViewClickListener;
import cn.com.live.videopls.venvy.entry.listeners.WedgeListener;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.interf.IWidgetClickListener;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetEmptyListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.live.VideoLiveView;
import io.vov.vitamio.utils.Log;
import videodemo.com.cn.myapplication.weidget.CustomMediaController;

public class LiveOsActivity extends BasePlayerActivity {

    private String mRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRoomId = getIntent().getStringExtra("roomId");
        super.onCreate(savedInstanceState);
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
     */
    private static String getPlatformId() {
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
                    .setVerticalType(0)//1 竖屏小屏，0竖屏全屏
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

                @Override
                public void onEmpty() {

                }
            };
        }

        /**
         * 1.6.0+版本已废弃，请使用最新接口IWidgetClickListener
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


        /**
         * 广告点击监听
         * @return
         */
        public IWidgetClickListener<WidgetInfo> buildWidgetClickListener() {
            return new IWidgetClickListener<WidgetInfo>() {

                @Override
                public void onClick(@Nullable WidgetInfo widgetInfo) {
                    if (widgetInfo != null) {
                        WidgetInfo.WidgetType type = widgetInfo.getWidgetType();
                        String url = widgetInfo.getUrl();
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        //具体type为区分广告类型。具体广告内容可看枚举注释
                        switch (type) {
                            case CLOUND:
                                //TODO,根据广告类型做不同的逻辑
                                break;

                            case ADGIFT:

                                break;
                        }
                    }
                }
            };
        }

        /**
         * 广告点击监听
         * @return
         */
        public IWidgetShowListener<WidgetInfo> buildWidgetShowListener() {
            return new IWidgetShowListener<WidgetInfo>() {
                @Override
                public void onShow(WidgetInfo widgetInfo) {
                    if (widgetInfo != null) {
                        WidgetInfo.WidgetType type = widgetInfo.getWidgetType();
                        String url = widgetInfo.getUrl();
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        //具体type为区分广告类型。具体广告内容可看枚举注释
                        switch (type) {
                            case CLOUND:
                                //TODO,根据广告类型做不同的逻辑
                                break;

                            case ADGIFT:

                                break;
                        }
                    }
                }
            };
        }

        /**
         * 广告关闭监听
         * @return
         */
        public IWidgetCloseListener<WidgetInfo> buildWidgetCloseListener() {
            return new IWidgetCloseListener<WidgetInfo>() {
                @Override
                public void onClose(WidgetInfo widgetInfo) {
                    if (widgetInfo != null) {
                        WidgetInfo.WidgetType type = widgetInfo.getWidgetType();
                        String url = widgetInfo.getUrl();
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        //具体type为区分广告类型。具体广告内容可看枚举注释
                        switch (type) {
                            case CLOUND:
                                //TODO,根据广告类型做不同的逻辑
                                break;

                            case ADGIFT:

                                break;
                        }
                    }
                }
            };
        }

        /**
         * 进入直播间没有互动广告时回调
         * 直播专用，点播业务不生效
         */

        @Override
        public IWidgetEmptyListener buildWidgetEmptyListener() {
            return new IWidgetEmptyListener() {
                @Override
                public void onEmpty() {

                }
            };
        }
    }

    @Override
    public void screenChanged(CustomMediaController.Screen screen) {
        super.screenChanged(screen);
        switch (screen) {
            case PORTRAIT:
                //屏幕切换调用，切换竖屏小屏
                getAdapter().notifyLiveVerticalScreen(1);
                break;
            case PORTRAIT_FULL:
                //屏幕切换调用，切换竖屏全屏
                getAdapter().notifyLiveVerticalScreen(0);
                break;

            case LAND_SCAPE:

                break;
        }
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
