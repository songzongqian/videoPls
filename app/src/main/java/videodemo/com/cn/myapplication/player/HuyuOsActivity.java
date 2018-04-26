package videodemo.com.cn.myapplication.player;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.image.IImageLoader;
import cn.com.venvy.common.image.IImageView;
import cn.com.venvy.common.interf.IPlatformLoginInterface;
import cn.com.venvy.common.interf.ISvgaImageView;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.venvy.fresco.FrescoImageLoader;
import cn.com.venvy.fresco.VenvyFrescoImageView;
import cn.com.venvy.svga.view.VenvySvgaImageView;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.huyu.EnjoyAdapter;
import cn.com.videopls.pub.huyu.PlatFormUserInfoImpl;
import videodemo.com.cn.myapplication.bean.SettingsBean;
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

import static videodemo.com.cn.myapplication.HyRoomType.ROOM_ANCHOR_CONFIG;
import static videodemo.com.cn.myapplication.HyRoomType.ROOM_ANCHOR_LANDSCAPE;
import static videodemo.com.cn.myapplication.HyRoomType.ROOM_USER;

public class HuyuOsActivity extends LiveBaseActivity {

    private View mHuYuAnchorView;
    private EditText mUId;
    private FrameLayout.LayoutParams mVideoParams;
    private TextView mTv_anchorConfig;
    private int mRoomType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!belowSDK21()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Intent intent = getIntent();
        mRoomType = intent.getIntExtra("roomType", ROOM_USER);
        super.onCreate(savedInstanceState);
        if (mRoomType != ROOM_USER) {
            mController.isEnjoy();
        }
    }


    @Override
    protected String getPlatformId() {
        return "5a658c13085bd4e05736a985";
    }

    @Override
    protected void initSettingsValue() {
        mSettingsBean = new SettingsBean();
        mSettingsBean.mRoomId = "2121653";
        mSettingsBean.mPlatformId = "5a658c13085bd4e05736a985";
        mSettingsBean.uId = "120078663";
        mSettingsBean.isAnchor = mRoomType != ROOM_USER;
        mSettingsBean.isPortraitFullScreen = mRoomType == ROOM_ANCHOR_CONFIG;
    }


    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return new HuYuAdapter();
    }

    @Override
    protected void initMediaController() {
        super.initMediaController();
        mController.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, mWidowPlayerHeight));
    }

    @Override
    protected void initView() {
        super.initView();
        mHuYuAnchorView = getLayoutInflater().inflate(R.layout.activity_huyu_sublayout,
                getContentView(), true);
        mTv_anchorConfig = (TextView) mHuYuAnchorView.findViewById(R.id.pre_config);
        mTv_anchorConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSmallVertical) {
                    changeStatusColor();
                }
                mVideoLiveView.openPreConfig();
            }
        });

        RelativeLayout.LayoutParams rootParams = (RelativeLayout.LayoutParams) mRootView
                .getLayoutParams();
        rootParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        rootParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        mRootView.setLayoutParams(rootParams);

        mVideoParams = getVideoParams();

        setConfig();
    }

    protected void setSmallScreen() {
        if (mRoomType == ROOM_USER) {
            mSettingsBean.isPortraitFullScreen = false;
        }
        mVideoParams = getVideoParams();
        mVideoParams.height = mWidowPlayerHeight;
        mCustomVideoView.setLayoutParams(mVideoParams);
    }

    protected void setFullScreen() {
        if (mRoomType == ROOM_USER) {
            mSettingsBean.isPortraitFullScreen = true;
        }
        mVideoParams = getVideoParams();
        mVideoParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        mCustomVideoView.setLayoutParams(mVideoParams);
    }

    private void setConfig() {
        if (mRoomType != ROOM_ANCHOR_CONFIG && mRoomType != ROOM_ANCHOR_LANDSCAPE) {
            // 竖屏进入
            mVideoParams.height = mWidowPlayerHeight;

        } else {
            isSmallVertical = false;
            mVideoParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        }
        mCustomVideoView.setLayoutParams(mVideoParams);

        // 配置页
        if (mRoomType == ROOM_ANCHOR_CONFIG) {
            mRootView.removeView(mCustomVideoView);
            changeStatusColor();
            mTv_anchorConfig.setVisibility(View.GONE);
            mVideoLiveView.openPreConfig();
        }

        if (mRoomType == ROOM_USER) {
            mTv_anchorConfig.setVisibility(View.GONE);
            VenvyLog.i("isAnchor " + mSettingsBean.isAnchor);
        }

        // 主播端横屏
        if (mRoomType == ROOM_ANCHOR_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void changeStatusColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTv_anchorConfig.setVisibility(View.GONE);
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(0xff2896F0);
            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View .
                // 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }
    }

    private FrameLayout.LayoutParams getVideoParams() {
        if (mVideoParams == null) {
            mVideoParams = (FrameLayout.LayoutParams) mCustomVideoView.getLayoutParams();
        }
        return mVideoParams;
    }

    //huyu adapter
    class HuYuAdapter extends EnjoyAdapter {

        @Override
        public IPlatformLoginInterface buildLoginInterface() {
            return new PlatFormUserInfoImpl() {
                @Override
                public PlatformUserInfo getLoginUser() {
                    PlatformUserInfo platformUserInfo = new PlatformUserInfo();
                    platformUserInfo.isAnchor = mSettingsBean.isAnchor;
                    platformUserInfo.uid = mSettingsBean.uId;
                    platformUserInfo.roomId = mSettingsBean.mRoomId;
                    platformUserInfo.platformId = mSettingsBean.mPlatformId;
                    platformUserInfo.isPortraitFullScreen = mSettingsBean.isPortraitFullScreen;
                    return platformUserInfo;
                }

                @Override
                public void screenChanged(ScreenChangedInfo changedInfo) {
                    HuyuOsActivity.this.setRequestedOrientation(ActivityInfo
                            .SCREEN_ORIENTATION_PORTRAIT);
                    HuyuOsActivity.this.screenChanged(VideoControllerView.Screen.PORTRAIT);
                }
            };
        }

        @Override
        public IWidgetShowListener buildWidgetShowListener() {
            return new IWidgetShowListener<WidgetInfo>() {
                @Override
                public void onShow(WidgetInfo info) {
                    if (info != null) {
                        VenvyLog.i("--互娱展示" + info.getWidgetType());
                    }
                }
            };
        }


        @Override
        public IWidgetCloseListener buildWidgetCloseListener() {
            return new IWidgetCloseListener<WidgetInfo>() {
                @Override
                public void onClose(WidgetInfo info) {
                    if (info == null) {
                        return;
                    }
                    VenvyLog.i("--互娱关闭" + info.getWidgetType());
                    if (info.getWidgetType() == WidgetInfo.WidgetType.CLOUND) {
                        if (mRoomType == ROOM_ANCHOR_CONFIG) {
                            finish();
                            return;
                        }
                        mTv_anchorConfig.setVisibility(View.VISIBLE);
                        if (mSettingsBean.isPortraitFullScreen && !belowSDK21()) {
                            Window window = getWindow();
                            window.clearFlags(WindowManager.LayoutParams
                                    .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            ViewGroup mContentView = (ViewGroup) findViewById(Window
                                    .ID_ANDROID_CONTENT);
                            View mChildView = mContentView.getChildAt(0);
                            if (mChildView != null) {
                                ViewCompat.setFitsSystemWindows(mChildView, false);
                            }
                        }
                    }
                }
            };
        }

        @Override
        public Class<? extends IImageLoader> buildImageLoader() {
            return FrescoImageLoader.class;
        }

        @Override
        public Class<? extends IImageView> buildImageView() {
            return VenvyFrescoImageView.class;
        }

        @Override
        public Class<? extends ISvgaImageView> buildSvgaImageLoader() {
            return VenvySvgaImageView.class;
        }

    }

    @Override
    protected View getInflate(LayoutInflater inflater) {
        return inflater.inflate(R.layout.pop_up_huyu, null);
    }

    @Override
    protected void initButtons(View contentview, final PopupWindow popupWindow) {
        final EditText roomId = (EditText) contentview.findViewById(R.id.et_roomId);
        roomId.setText(mSettingsBean.mRoomId);

        mUId = (EditText) contentview.findViewById(R.id.et_uId);
        mUId.setText(mSettingsBean.uId);

        final EditText platfromId = (EditText) contentview.findViewById(R.id.et_platform);
        platfromId.setText(mSettingsBean.mPlatformId);

        initEnvButtons(contentview);
        final RadioGroup selectEnv = (RadioGroup) contentview.findViewById(R.id.rg_select_env);


        Button apply = (Button) contentview.findViewById(R.id.btn_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsBean.mRoomId = roomId.getText().toString();
                mSettingsBean.uId = mUId.getText().toString();
                mSettingsBean.mPlatformId = platfromId.getText().toString();

                debugToggle(selectEnv.getCheckedRadioButtonId());
                updateAdapter();
                //update adapter
                popupWindow.dismiss();
            }
        });

    }

    private void updateAdapter() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        videoPlusView.stop();
        getAdapter().updateProvider(null);
        videoPlusView.start();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //注意父类中的调用
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
