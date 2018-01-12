package videodemo.com.cn.myapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;

import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.interf.IMediaControlListener;
import cn.com.venvy.common.interf.IWidgetClickListener;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.ott.VideoOTTView;
import videodemo.com.cn.myapplication.base.BasePlayerActivity;
import videodemo.com.cn.myapplication.bean.SettingsBean;

public class VideoOTTActivity extends BasePlayerActivity {

    private SettingsBean mSettingsBean;

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return new VideoOTTView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return new MyAdapter();
    }

    @Override
    protected void initMediaController() {
        super.initMediaController();
        mController.isLive(false);
    }


    /**
     * 申请的Video++ KEY
     *
     * @return
     */
    private String getAppKey() {
        if (VenvyDebug.isDebug()) {
            return "Hk_MWKOEW";
        }
        return "Hk_MWKOEW";
    }

    /**
     * Video path或者Video ID
     *
     * @return
     */
    private String getVideoPath() {
        if (VenvyDebug.isDebug()) {
            return "http://7xr4xn.media1.z0.glb.clouddn.com/snh48sxhsy.mp4?v=0";
        }
        return "http://sdkcdn.videojj.com/flash/player/video/1.mp4?v=5";
    }

    private class MyAdapter extends VideoPlusAdapter {

        @Override
        public Provider createProvider() {
            final int width = VenvyUIUtil.getScreenWidth(VideoOTTActivity.this);
            final int height = VenvyUIUtil.getScreenHeight(VideoOTTActivity.this);
            Provider provider = new Provider.Builder()
                    .setAppKey(getAppKey())//appkey
                    .setHorVideoHeight(Math.min(width, height))//横屏视频的高
                    .setHorVideoWidth(Math.max(width, height))//横屏视频的宽
                    .setVerVideoWidth(Math.min(width, height))//small视频小屏视频的宽
                    .setVerVideoHeight(mWidowPlayerHeight)//small 视频小屏视频的高
                    .setVideoPath(getVideoPath())//视频地址
                    .setVideoType(3)//
                    .setVideoTitle("ttt")//
                    .build();
            return provider;
        }

        /**
         * 点播视频控制监听接口，该接口必须提供，否则点播业务无法正常工作
         * 此接口是控制播放器行为
         */
        @Override
        public IMediaControlListener buildMediaController() {
            return new IMediaControlListener() {
                @Override
                public void start() {
                    startPlay();
                }

                @Override
                public void pause() {
                    pausePlay();
                }

                @Override
                public void restart() {
                    startPlay();
                }

                @Override
                public void seekTo(long position) {
                    playerSeekTo(position);
                }

                @Override
                public void stop() {
                    stopPlay();
                }

                @Override
                public long getCurrentPosition() {
                    return getPlayerPosition();
                }
            };
        }

        /**
         * 广告点击监听
         *
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
         *
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
         *
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

    @Override
    protected void initSettingsValue() {
        mSettingsBean = new SettingsBean();
        mSettingsBean.mAppkey = getAppKey();
        mSettingsBean.mUrl = getVideoPath();
    }

}