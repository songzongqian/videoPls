package videodemo.com.cn.myapplication.player;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.image.IImageLoader;
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
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

//import cn.com.venvy.glide.GlideImageLoader;

public class VideoOTTActivity extends BasePlayerActivity {

    private SettingsBean mSettingsBean;
    private boolean enableMQTT = false;

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

    @Override
    public void verticalTypeChange(VideoControllerView.Screen screen) {

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
            return new MyMediaController();
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

    private class MyMediaController extends IMediaControlListener.LeMediaControlListener {
        @Override
        public void start() {

        }

        @Override
        public boolean isLive() {
            return enableMQTT;
        }

        @Override
        public boolean isPositive() {
            return true;
        }

        @Override
        public void pause() {
        }

        @Override
        public void seekTo(long position) {
        }

        @Override
        public void restart() {

        }

        @Override
        public void stop() {
        }

        @Override
        public long getCurrentPosition() {
            if (mCustomVideoView != null) {
                return mCustomVideoView.getMediaPlayerCurrentPosition();
            } else {
                return -1;
            }
        }

    }

    @Override
    protected View getInflate(LayoutInflater inflater) {
        return inflater.inflate(R.layout.pop_up_demond, null);
    }

    @Override
    protected void initButtons(View contentview, final PopupWindow popupWindow) {
        final EditText appkey = (EditText) contentview.findViewById(R.id.et_appkey);
        appkey.setText(mSettingsBean.mAppkey);

        final EditText url = (EditText) contentview.findViewById(R.id.et_uri);
        url.setText(mSettingsBean.mUrl);

        final RadioGroup selectMqtt = (RadioGroup) contentview.findViewById(R.id.rg_select_mqtt);
        selectMqtt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_mqtt_true) {
                    enableMQTT = true;
                }
            }
        });

        initEnvButtons(contentview);
        final RadioGroup selectEnv = (RadioGroup) contentview.findViewById(R.id.rg_select_env);

        Button apply = (Button) contentview.findViewById(R.id.btn_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsBean.mUrl = url.getText().toString();
                mSettingsBean.mAppkey = appkey.getText().toString();
                debugToggle(selectEnv.getCheckedRadioButtonId());
                //update adapter
                updateAdapter();

                popupWindow.dismiss();
            }
        });
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

    private void updateAdapter() {
        final int width = VenvyUIUtil.getScreenWidth(VideoOTTActivity.this);
        final int height = VenvyUIUtil.getScreenHeight(VideoOTTActivity.this);
        Provider provider = new Provider.Builder()
                .setAppKey(mSettingsBean.mAppkey)//appkey
                .setHorVideoHeight(Math.min(width, height))//横屏视频的高
                .setHorVideoWidth(Math.max(width, height))//横屏视频的宽
                .setVerVideoWidth(Math.min(width, height))//small视频小屏视频的宽
                .setVerVideoHeight(mWidowPlayerHeight)//small 视频小屏视频的高
                .setVideoPath(mSettingsBean.mUrl)//视频地址
                .setVideoType(3)//
                .setVideoTitle("ttt")//
                .build();
        mCustomVideoView.mediaPlayerSeekTo(0);
        getAdapter().updateProvider(provider);
        videoPlusView.destroy();
        videoPlusView.start();
    }

}
