package videodemo.com.cn.myapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.bean.WidgetInfo;

import cn.com.venvy.common.interf.IPlatformLoginInterface;
import cn.com.venvy.common.interf.IWidgetClickListener;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetEmptyListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
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
                    .setUserId(mSettingsBean.mRoomId)//roomId 或者userId
                    .setPlatformId(mSettingsBean.mPlatformId)//videojj直播后台平台Id
                    .setHorVideoWidth(Math.max(mScreenWidth, mScreenHeight))//横屏视频的宽
                    .setHorVideoHeight(Math.min(mScreenWidth, mScreenHeight))//横屏视频的高
                    .setVerticalFullVideoWidth(Math.min(mScreenWidth, mScreenHeight))//竖屏全屏视频的宽
                    .setVerticalFullVideoHeight(Math.max(mScreenWidth, mScreenHeight))//竖屏全屏视屏的高
                    .setVerVideoWidth(Math.min(mScreenWidth, mScreenHeight))//small视频小屏视频的宽
                    .setVerVideoHeight(mWidowPlayerHeight)//small 视频小屏视频的高
                    .setVerticalType(0)//1 竖屏小屏，0竖屏全屏
                    .setDirection(2) //2横竖屏，0竖屏，1是横屏
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
                    userInfo.cate = mSettingsBean.mCate;
                    return userInfo;
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
