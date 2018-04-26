package videodemo.com.cn.myapplication.player;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.image.IImageLoader;
import cn.com.venvy.common.interf.IPlatformLoginInterface;
import cn.com.venvy.common.interf.IWidgetClickListener;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetEmptyListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.interf.WedgeListener;
import cn.com.venvy.glide.GlideImageLoader;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.huyu.PlatFormUserInfoImpl;

public class LiveOsActivity extends LiveBaseActivity {

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
                    .setHorVideoWidth(Math.max(mScreenWidth, mScreenHeight))//横屏视频的宽
                    .setHorVideoHeight(Math.min(mScreenWidth, mScreenHeight))//横屏视频的高
                    .setVerticalFullVideoWidth(Math.min(mScreenWidth, mScreenHeight))//竖屏全屏视频的宽
                    .setVerticalFullVideoHeight(Math.max(mScreenWidth, mScreenHeight))//竖屏全屏视屏的高
                    .setVerVideoWidth(Math.min(mScreenWidth, mScreenHeight))//small视频小屏视频的宽
                    .setVerVideoHeight(mWidowPlayerHeight)//small 视频小屏视频的高
                    .setVerticalType(1)//1 竖屏小屏，0竖屏全屏
                    .setDirection(2)
                    .build();
        }

        /**
         * buildLoginInterface : 设置分区信息, 用户登录信息
         *
         * @return
         */
        @Override
        public IPlatformLoginInterface buildLoginInterface() {
            return new PlatFormUserInfoImpl() {
                @Override
                public PlatformUserInfo getLoginUser() {
                    PlatformUserInfo userInfo = new PlatformUserInfo();
                    userInfo.cate = mSettingsBean.mCate;//设置分区，e.g. lol, hearthstone, dota1 ...
                    userInfo.roomId = mSettingsBean.mRoomId;
                    userInfo.platformId = mSettingsBean.mPlatformId;
                    userInfo.uid = mSettingsBean.uId;
                    userInfo.userName = mSettingsBean.mUserName;
                    return userInfo;
                }

                @Override
                public void login(LoginCallback loginCallback) {

                }
            };
        }

        @Override
        public WedgeListener buildWedgeListener() {
            return new WedgeListener() {
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
         * 广告展示监听
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

        @Override
        public Class<? extends IImageLoader> buildImageLoader() {
            return GlideImageLoader.class;
        }
    }

    @Override
    protected View getInflate(LayoutInflater inflater) {
        return inflater.inflate(R.layout.pop_up_live, null);
    }

    @Override
    protected void initButtons(View contentview, final PopupWindow popupWindow) {
        final EditText roomId = (EditText) contentview.findViewById(R.id.et_roomId);
        roomId.setText(mSettingsBean.mRoomId);

        final EditText platfromId = (EditText) contentview.findViewById(R.id.et_platform);
        platfromId.setText(mSettingsBean.mPlatformId);

        Spinner cateSpinner = (Spinner) contentview.findViewById(R.id.cate);
        setSpinnerItemSelectedByValue(cateSpinner, mSelectCate);
        cateSpinner.setOnItemSelectedListener(this);

        initEnvButtons(contentview);
        final RadioGroup selectEnv = (RadioGroup) contentview.findViewById(R.id.rg_select_env);

        final EditText et_userId = (EditText) contentview.findViewById(R.id.et_user_id);

        final EditText et_userName = (EditText) contentview.findViewById(R.id.et_user_name);

        Button apply = (Button) contentview.findViewById(R.id.btn_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsBean.mRoomId = roomId.getText().toString();
                if (!TextUtils.isEmpty(mSelectCate) && mSelectCate.length() > 1) {
                    mSettingsBean.mCate = mSelectCate.split("/")[0];
                }
                mSettingsBean.uId = et_userId.getText().toString();
                mSettingsBean.mUserName = et_userName.getText().toString();
                debugToggle(selectEnv.getCheckedRadioButtonId());
                updateSettingsBeans();
                updateAdapter();
                //update adapter
                popupWindow.dismiss();
            }
        });


    }

    private void updateAdapter() {
        videoPlusView.stop();
        Provider provider = new Provider.Builder()
                .setHorVideoWidth(Math.max(mScreenWidth, mScreenHeight))//横屏视频的宽
                .setHorVideoHeight(Math.min(mScreenWidth, mScreenHeight))//横屏视频的高
                .setVerticalFullVideoWidth(Math.min(mScreenWidth, mScreenHeight))//竖屏全屏视频的宽
                .setVerticalFullVideoHeight(Math.max(mScreenWidth, mScreenHeight))//竖屏全屏视屏的高
                .setVerVideoWidth(Math.min(mScreenWidth, mScreenHeight))//small视频小屏视频的宽
                .setVerVideoHeight(mWidowPlayerHeight)//small 视频小屏视频的高
                .setVerticalType(isSmallVertical ? 1 : 0)//1 竖屏小屏，0竖屏全屏
                .setDirection(2) //2横竖屏，0竖屏，1是横屏
                .build();
        getAdapter().updateProvider(provider);
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
