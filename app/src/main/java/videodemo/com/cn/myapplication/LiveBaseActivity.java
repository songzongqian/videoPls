package videodemo.com.cn.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import cn.com.venvy.common.utils.VenvyDebug;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.live.VideoLiveView;
import videodemo.com.cn.myapplication.base.BasePlayerActivity;
import videodemo.com.cn.myapplication.bean.SettingsBean;
import videodemo.com.cn.myapplication.weidget.VideoControllerView;

public class LiveBaseActivity extends BasePlayerActivity implements AdapterView.OnItemSelectedListener{


    protected SettingsBean mSettingsBean;
    protected String mSelectCate;
    protected VideoLiveView mVideoLiveView;

    protected boolean isSmallVertical = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return mVideoLiveView = new VideoLiveView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return null;
    }

    @Override
    protected View getInflate(LayoutInflater inflater) {
        return null;
    }

    /**
     * Video++直播PlatformId
     */
    protected static String getPlatformId() {
        if (VenvyDebug.isDebug() || VenvyDebug.isPreView()) {
            return "556c38e7ec69d5bf655a0fb2";
        }
        return "575e6e087c395e0501980c89";
    }

    //初始化相关的默认参数
    @Override
    protected void initSettingsValue() {
        mSettingsBean = new SettingsBean();
        mSettingsBean.mRoomId = "34";
        mSettingsBean.mPlatformId = getPlatformId();
        mSettingsBean.mCate = "";
        mSettingsBean.isAnchor = false;
    }

    @Override
    protected void initMediaController() {
        super.initMediaController();
        mController.isLive(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectCate = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void verticalTypeChange(VideoControllerView.Screen screen) {
        VenvyLog.i("screenChanged verticalTypeChange = " + screen);
        if (isSmallVertical) {
            //屏幕切换调用，切换竖屏全屏
            getAdapter().notifyLiveVerticalScreen(0);
            setFullScreen();
        } else {
            //屏幕切换调用，切换竖屏小屏
            getAdapter().notifyLiveVerticalScreen(1);
            setSmallScreen();
        }
        isSmallVertical = !isSmallVertical;
    }

    @Override
    public void screenChanged(VideoControllerView.Screen screen) {
        VenvyLog.i("screenChanged = " + screen);
        if (screen == VideoControllerView.Screen.LAND_SCAPE) {
            setFullScreen();
        } else {
            if (isSmallVertical) {
                setSmallScreen();
            } else {
                setFullScreen();
            }
        }
    }

    protected static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (TextUtils.equals(value, (apsAdapter.getItem(i).toString()))) {
                {
                    spinner.setSelection(i, true);// 默认选中项
                    break;
                }
            }
        }
    }

    protected void setSmallScreen() {
        RelativeLayout.LayoutParams rootParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                mWidowPlayerHeight);
        mRootView.setLayoutParams(rootParams);
    }

    protected void setFullScreen() {
        RelativeLayout.LayoutParams rootParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRootView.setLayoutParams(rootParams);
    }
}
