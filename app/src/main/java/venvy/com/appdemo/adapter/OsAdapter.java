package venvy.com.appdemo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.bean.VideoPlayerSize;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.http.base.IRequestConnect;
import cn.com.venvy.common.image.IImageLoader;
import cn.com.venvy.common.interf.IMediaControlListener;
import cn.com.venvy.common.interf.IPlatformLoginInterface;
import cn.com.venvy.common.interf.ISocketConnect;
import cn.com.venvy.common.interf.ISvgaImageView;
import cn.com.venvy.common.interf.IWidgetClickListener;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.interf.PlatformLoginListener;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.venvy.glide.GlideImageLoader;
import cn.com.venvy.mqtt.VenvyMqtt;
import cn.com.venvy.okhttp.OkHttpHelper;
import cn.com.venvy.svga.view.VenvySvgaImageView;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import venvy.com.appdemo.bean.SettingsBean;
import venvy.com.appdemo.helper.MyMediaHelper;
import venvy.com.appdemo.widget.VideoControllerView;

/**
 * Create by bolo on 08/06/2018
 */
public class OsAdapter extends VideoPlusAdapter {

    private Context mContext;
    private SettingsBean mData;
    private final int mScreenWidth;
    private final int mScreenHeight;
    private MyMediaHelper mMediaController;

    private AdapterScreenChangedListener mAdapterScreenChangedListener;

    public OsAdapter(Context context, SettingsBean bean) {
        mData = bean;
        mContext = context;
        mScreenWidth = VenvyUIUtil.getScreenWidth(mContext);
        mScreenHeight = VenvyUIUtil.getScreenHeight(mContext);
    }

    public void addScreenChangedListener(AdapterScreenChangedListener listener) {
        mAdapterScreenChangedListener = listener;
    }

    public void setMediaController(VideoControllerView.MediaPlayerControl control) {
        if (mMediaController == null) {
            mMediaController = new MyMediaHelper();
        }
        mMediaController.setMediaPlayerControl(control);
        mMediaController.setVideoSize(new VideoPlayerSize(mScreenHeight, mScreenWidth,
                mScreenWidth, mData.mVerticalHeight, 0));
    }

    public void setVideoController(VideoControllerView.IVideoControllerListener l) {
        if (mMediaController == null) {
            mMediaController = new MyMediaHelper();
        }
        mMediaController.setVideoController(l);
    }

    public void upDate(SettingsBean bean) {
        if (bean != null) {
            mData = bean;
        }
        updateProvider(setProvider());
    }

    @Override
    public Provider createProvider() {
        return setProvider();
    }

    @Override
    public IPlatformLoginInterface buildLoginInterface() {
        return new PlatformLoginListener() {
            @Override
            public PlatformUserInfo getLoginUser() {
                PlatformUserInfo platformUserInfo = new PlatformUserInfo();
                platformUserInfo.userType = mData.mUserType;
                platformUserInfo.uid = mData.mUid;
                return platformUserInfo;
            }

            @Override
            public boolean isLogined() {
                return true;
            }

            @Override
            public void login(LoginCallback loginCallback) {

            }

            @Override
            public void screenChanged(ScreenChangedInfo changedInfo) {
                if (mMediaController == null) {
                    return;
                }
                // --1横屏切竖屏 2竖屏切横屏
                switch (changedInfo.screenType) {
                    case 1:
                       mMediaController.screenChange(false);
                        break;
                    case 2:
                        mMediaController.screenChange(true);
                        break;
                }
            }
        };
    }

    /**
     * 点播视频控制监听接口，该接口必须提供，否则点播业务无法正常工作
     * 此接口是控制播放器行为
     */
    @Override
    public IMediaControlListener buildMediaController() {
        return mMediaController;
    }


    @Override
    public Class<? extends IRequestConnect> buildConnectProvider() {
        return OkHttpHelper.class;
    }

    @Override
    public Class<? extends ISvgaImageView> buildSvgaImageView() {
        return VenvySvgaImageView.class;
    }

    @Override
    public Class<? extends IImageLoader> buildImageLoader() {
        return GlideImageLoader.class;
    }

    @Override
    public Class<? extends ISocketConnect> buildSocketConnect() {
        return VenvyMqtt.class;
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
//                    if (TextUtils.isEmpty(url)) {
//                        return;
//                    }
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
//                    if (TextUtils.isEmpty(url)) {
//                        return;
//                    }
                    //具体type为区分广告类型。具体广告内容可看枚举注释
                    switch (type) {
                        case CLOUND:
                            //TODO,根据广告类型做不同的逻辑
                            break;

                        case ADGIFT:

                            break;
                        case VIDEOCLIP:
                            if (mMediaController != null) {
                                mMediaController.pause();
                            }
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
//                    if (TextUtils.isEmpty(url)) {
//                        return;
//                    }
                    //具体type为区分广告类型。具体广告内容可看枚举注释
                    switch (type) {
                        case CLOUND:
                            //TODO,根据广告类型做不同的逻辑
                            break;

                        case ADGIFT:

                            break;
                        case VIDEOCLIP:
                            if (mMediaController != null) {
                                mMediaController.start();
                            }
                            break;
                    }
                }
            }
        };
    }

    private Provider setProvider() {
        Provider provider = new Provider.Builder()
                .setPlatformId(mData.mPlatformId)
                .setVideoID(mData.mRoomId)//视频地址
                .setDirection(mData.mScreenStatus)
                .build();
        return provider;
    }

    public interface AdapterScreenChangedListener {
        void screenChanged(int status);
    }
}
