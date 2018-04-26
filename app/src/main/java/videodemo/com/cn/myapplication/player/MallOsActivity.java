package videodemo.com.cn.myapplication.player;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.image.IImageLoader;
import cn.com.venvy.common.interf.IPlatformLoginInterface;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.route.RouterRegistry;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.common.utils.VenvyReflectUtil;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.mall.VideoMallView;
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

//import cn.com.venvy.glide.GlideImageLoader;

public class MallOsActivity extends LiveBaseActivity {

    private IPlatformLoginInterface iPlatformLoginInterface;
    private static final String webViewName = "cn.com.venvy.mall.test.MallWebViewFactory";
    private Class mallClas;
    private View mMallRootView;
    private VideoMallView mVideoMallView;
    private Button mMallBtn, mShelfBtn;

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return mVideoMallView = new VideoMallView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return new mallAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMallRootView = getLayoutInflater().inflate(R.layout.activity_mall_buttons, getContentView(), true);
        initMallView();
        initMall();
    }

    //竖屏小屏adapter
    private class mallAdapter extends VideoPlusAdapter {
        @Override
        public Provider createProvider() {
            Provider provider = new Provider.Builder()
                    .setUserId(mSettingsBean.mRoomId)//roomId 或者userId
                    .setTestUserId(mSettingsBean.mRoomId)
                    .setPlatformId("56dd27a8b311dff60073e645")//videojj直播后台平台Id ：575e6e087c395e0501980c89
                    .build();
            return provider;
        }

        @Override
        public Class<? extends IImageLoader> buildImageLoader() {
//            return GlideImageLoader.class;
            return null;
        }

        @Override
        public IPlatformLoginInterface buildLoginInterface() {
            initLogin();
            return iPlatformLoginInterface;
        }

        @Override
        public IWidgetCloseListener<WidgetInfo> buildWidgetCloseListener() {
            return new IWidgetCloseListener<WidgetInfo>() {
                @Override
                public void onClose(WidgetInfo widgetInfo) {
                    WidgetInfo.WidgetType widgetType = widgetInfo.getWidgetType();
                    if (widgetType == WidgetInfo.WidgetType.MAILL) {
                    }
                }
            };
        }
    }

    private void initLogin() {
        iPlatformLoginInterface = new IPlatformLoginInterface() {
            @Override
            public PlatformUserInfo getLoginUser() {
                PlatformUserInfo platformUserInfo = new PlatformUserInfo();
                platformUserInfo.setUid("aaaaaa");
                platformUserInfo.setNickName("bbbbbb");
                platformUserInfo.setUserName("cccccc");
                platformUserInfo.setPhoneNum("999999");
                platformUserInfo.setUserToken("ddddddd");
                return platformUserInfo;
            }

            @Override
            public void userLogined(PlatformUserInfo userInfo) {
                if (userInfo != null) {
                    String token = userInfo.getUserToken();
                }
            }

            @Override
            public void login(LoginCallback loginCallback) {

            }

            @Override
            public void screenChanged(ScreenChangedInfo changedInfo) {
                System.out.println("---登陆的URL－－－" + changedInfo.url);
                //设为竖屏全屏
                VenvyReflectUtil.invokeStatic(mallClas,
                        "screenChanged", new Class[]{ScreenChangedInfo.class}, new Object[]{changedInfo});
            }
        };
    }

    private void initMall() {
        mallClas = VenvyReflectUtil.getClass(webViewName);
        if (mallClas != null) {
            View view = (View)
                    VenvyReflectUtil.invokeStatic(mallClas,
                            "initMallWebView", new Class[]{Context.class, IPlatformLoginInterface.class}, new Object[]{this, iPlatformLoginInterface});
            getContentView().addView(view);
            VenvyReflectUtil.invokeStatic(mallClas,
                    "gone", null, null);
        }
    }

    private void initMallView() {
        mMallBtn = (Button) mMallRootView.findViewById(R.id.mall);
        mMallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterRegistry.ROUTER_REGISTRY.invokeRouter("mall");
            }
        });
        mShelfBtn = (Button) mMallRootView.findViewById(R.id.shelf);
        mShelfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VenvyReflectUtil.invokeStatic(mallClas,
                        "open", new Class[]{String.class}, new Object[]{VideoMallView.getMallUrl() + "?video=34"});
            }
        });
    }

    @Override
    public void verticalTypeChange(VideoControllerView.Screen screen) {
        VenvyLog.i("screenChanged verticalTypeChange = " + screen);
        if (isSmallVertical) {
            //屏幕切换调用，切换竖屏全屏
            getAdapter().notifyLiveVerticalScreen(0);
            setFullScreen();
            mShelfBtn.setVisibility(View.VISIBLE);
            mMallBtn.setVisibility(View.GONE);
        } else {
            //屏幕切换调用，切换竖屏小屏
            getAdapter().notifyLiveVerticalScreen(1);
            setSmallScreen();
            VenvyReflectUtil.invokeStatic(mallClas,
                    "gone", null, null);
        }
        isSmallVertical = !isSmallVertical;
    }

    @Override
    public void screenChanged(VideoControllerView.Screen screen) {
        VenvyLog.i("screenChanged = " + screen);
        if (screen == VideoControllerView.Screen.LAND_SCAPE) {
            setFullScreen();
            mMallBtn.setVisibility(View.VISIBLE);
            mShelfBtn.setVisibility(View.GONE);
        } else {
            if (isSmallVertical) {
                setSmallScreen();
            } else {
                setFullScreen();
            }
        }
    }
}
