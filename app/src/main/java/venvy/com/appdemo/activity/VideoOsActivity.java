package venvy.com.appdemo.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.com.venvy.common.interf.ScreenStatus;
import cn.com.venvy.common.router.VenvyRouterManager;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.os.VideoOsView;
import venvy.com.appdemo.R;
import venvy.com.appdemo.adapter.OsAdapter;
import venvy.com.appdemo.bean.OsConfigureBean;
import venvy.com.appdemo.bean.SettingsBean;
import venvy.com.appdemo.utils.ConfigUtil;
import venvy.com.appdemo.widget.ConfigDialog;
import venvy.com.appdemo.widget.VideoControllerView;

public class VideoOsActivity extends BasePlayerActivity implements View.OnClickListener,
        ConfigDialog.SettingChangedListener {

    private String data;

    private ConstraintLayout mSettingView;
    private OsConfigureBean mData;
    private ConfigDialog mConfigDialog;
    private View mBtSetting;
    private View mBtMall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VenvyRouterManager.getInstance().injectRouter(this);
        VenvyLog.e("VideoOSActivity", "data ===" + data);

        Parcelable config_data = getIntent().getParcelableExtra("config_data");
        if (config_data instanceof OsConfigureBean) {
            mData = (OsConfigureBean) config_data;
        }
    }

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return new VideoOsView(this);
    }

    @NonNull
    @Override
    protected VideoPlusAdapter initVideoPlusAdapter() {
        return new OsAdapter(this, mSettingsBean);
    }

    @Override
    protected int getVideoType() {
        return VideoControllerView.VIDEO_OS;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSettingView();

        if (mConfigDialog != null) {
            mConfigDialog.addDataChangerListener(this);
        }

        if (mVideoPlusAdapter == null) {
            mSettingsBean = ConfigUtil.getSettingCache(this, mData);
            mSettingsBean.mVerticalHeight = mWidowPlayerHeight;
            mData = ConfigUtil.getConfig(this, mData, mSettingsBean);
            mVideoPlusAdapter = initVideoPlusAdapter();
        }

        if (mVideoPlusAdapter instanceof OsAdapter && !mIsInitialized) {
            // 获取保存的设置
            ((OsAdapter) mVideoPlusAdapter).setMediaController(mCustomVideoView);
            mVideoPlusView.setVideoOSAdapter(mVideoPlusAdapter);
            mVideoPlusView.start();
            mIsInitialized = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mConfigDialog != null) {
            mConfigDialog.addDataChangerListener(null);
        }

        mBtSetting.setOnClickListener(null);
        mBtMall.setOnClickListener(null);
    }

    @Override
    protected void setVideoFullScreen() {
        super.setVideoFullScreen();
        if (mVideoPlusAdapter != null) {
            mVideoPlusAdapter.notifyVideoScreenChanged(ScreenStatus.LANDSCAPE);
        }
    }

    @Override
    protected void setVideoVerticalScreen() {
        super.setVideoVerticalScreen();
        if (mVideoPlusAdapter != null) {
            mVideoPlusAdapter.notifyVideoScreenChanged(ScreenStatus.SMALL_VERTICAL);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mConfigDialog == null) {
            mConfigDialog = ConfigDialog.get(this, mData, mSettingsBean);
            mConfigDialog.addDataChangerListener(this);
        }
        if (id == R.id.iv_os_setting) {
            mConfigDialog.showOsSetting();
        } else if (id == R.id.bt_os_setting_mall) {
            mConfigDialog.showLiveConfig();
        }
    }

    @Override
    public void onDataChanger(SettingsBean bean) {
        if (bean != null) {
            mSettingsBean = bean;
            Toast.makeText(this, R.string.config_success, Toast.LENGTH_SHORT).show();
            // 设置完成后更新数据
            if (mVideoPlusView != null && mVideoPlusAdapter instanceof OsAdapter) {
                mVideoPlusView.stop();
                ((OsAdapter) mVideoPlusAdapter).upDate(bean);
                mVideoPlusView.start();
            }
        } else {
            Toast.makeText(this, R.string.config_failed, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSettingView
                .getLayoutParams();
        ConstraintLayout.LayoutParams buttonParams = (ConstraintLayout.LayoutParams) mBtMall
                .getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;

            buttonParams.rightToLeft = ConstraintLayout.LayoutParams.UNSET;
            buttonParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            buttonParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;

            buttonParams.rightToLeft = R.id.iv_os_setting;
            buttonParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
        }
        mBtMall.setLayoutParams(buttonParams);
        mSettingView.setLayoutParams(params);
    }

    private void initSettingView() {
        if (mSettingView == null) {
            mSettingView = (ConstraintLayout) LayoutInflater.from(this)
                    .inflate(R.layout.layout_os_setting_button, mRootView, false);
            mBtSetting = mSettingView.findViewById(R.id.iv_os_setting);
            mBtMall = mSettingView.findViewById(R.id.bt_os_setting_mall);
            mRootView.addView(mSettingView);
        }

        mBtSetting.setOnClickListener(this);
        mBtMall.setOnClickListener(this);
    }
}
