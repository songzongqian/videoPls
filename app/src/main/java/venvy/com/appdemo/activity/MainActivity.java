package venvy.com.appdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.utils.VenvyLog;
import venvy.com.appdemo.R;
import venvy.com.appdemo.bean.OsConfigureBean;
import venvy.com.appdemo.bean.SettingsBean;
import venvy.com.appdemo.helper.ConfigRequestHelper;
import venvy.com.appdemo.utils.ConfigUtil;
import venvy.com.appdemo.widget.ConfigDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ConfigRequestHelper.IGetConfigSuccessListener, ConfigDialog.SettingChangedListener {


    private static final int NONE = 0;
    private static final int HOME = 1;
    private static final int HOME_SELECT_LIVE = 2;

    private View mBackIcon;
    private TextView mTitle;
    private TextView mItem1;
    private TextView mItem2;
    private TextView mItem3;
    private View mSettingMenu;
    private Group mMenuGroup;
    private Button mSettingItem1;
    private Button mSettingItem2;

    private SettingsBean mSettingsBean;

    private int mLastStatus = NONE;
    private boolean mIsMenuItemVisible;

    private ConfigRequestHelper mRequestHelper;
    private OsConfigureBean mData;
    private int mProgress = -1;
    private ConfigDialog mConfigDialog;
    private AlertDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettingsBean = new SettingsBean();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        mSettingsBean.isFullScreen = true;
        mSettingsBean.isUserApp = true;
        if (id == R.id.ib_home_back) {
            goBack();
        } else if (id == R.id.tv_home_item_1) {
            if (mLastStatus == NONE) {
                // 选择直播属性
                selectLive();
            } else if (mLastStatus == HOME) {
                // 选择直播状态
                mSettingsBean.mUserType = PlatformUserInfo.UserType.Anchor;
                selectAnchor();
            } else if (mLastStatus == HOME_SELECT_LIVE) {
                // 打开预设置
                mSettingsBean.isUserApp = false;
                mSettingsBean.mUserType = PlatformUserInfo.UserType.Anchor;
                startLiveActivity();
                mLastStatus = HOME_SELECT_LIVE;
            }
        } else if (id == R.id.tv_home_item_2) {
            if (mLastStatus == NONE) {
                // 进入点播
                startOsActivity();
                return;
            } else if (mLastStatus == HOME) {
                // 用户
                mSettingsBean.mUserType = PlatformUserInfo.UserType.Consumer;
                mLastStatus = HOME;
            } else if (mLastStatus == HOME_SELECT_LIVE) {
                mSettingsBean.isFullScreen = false;
            }
            // 进入直播
            startLiveActivity();
        } else if (id == R.id.tv_home_item_3) {
            startLiveActivity();
            mLastStatus = HOME_SELECT_LIVE;
        } else if (id == R.id.iv_home_setting_menu) {
            if (mIsMenuItemVisible) {
                mMenuGroup.setVisibility(View.GONE);
            } else {
                mMenuGroup.setVisibility(View.VISIBLE);
            }
            mIsMenuItemVisible = !mIsMenuItemVisible;
            return;
        } else if (id == R.id.bt_home_setting_menu_live || id == R.id.bt_home_setting_menu_mall) {
            showSettingDialog();
            return;
        }
        hideMenuGroup();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        if (mRequestHelper == null) {
            mRequestHelper = new ConfigRequestHelper();
            mRequestHelper.request();
            mRequestHelper.setListener(this);
        }
        if (mConfigDialog != null) {
            mConfigDialog.addDataChangerListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBackIcon.setOnClickListener(null);
        mItem1.setOnClickListener(null);
        mItem2.setOnClickListener(null);
        mItem3.setOnClickListener(null);
        mSettingMenu.setOnClickListener(null);
        mSettingItem1.setOnClickListener(null);
        mSettingItem2.setOnClickListener(null);
        mRequestHelper.abort();
        if (mConfigDialog != null) {
            mConfigDialog.addDataChangerListener(null);
        }

    }

    @Override
    public void onBackPressed() {
        hideMenuGroup();
        if (mLastStatus == NONE) {
            super.onBackPressed();
        } else {
            goBack();
        }
    }


    @Override
    public void success(OsConfigureBean bean) {
        if (bean == null || isFinishing()) {
            return;
        }
        mProgress = 0;
        mData = bean;
        mProgressDialog.dismiss();
        mItem1.setClickable(true);
        mItem2.setClickable(true);
    }

    @Override
    public void progress(int progress) {
        if (isFinishing()) {
            return;
        }
        mProgress = progress;
    }

    @Override
    public void failed() {
        if (isFinishing()) {
            return;
        }
        mProgress = -1;
        mProgressDialog.dismiss();
    }

    @Override
    public void onDataChanger(SettingsBean bean) {
        if (bean != null) {
            mSettingsBean = bean;
            Toast.makeText(this, R.string.config_success, Toast.LENGTH_SHORT).show();
        }
    }


    private void showSettingDialog() {
        if (mData == null || mProgress == -1) {
            Toast.makeText(this, R.string.toast_config_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mConfigDialog == null) {
            mConfigDialog = ConfigDialog.get(this, mData, mSettingsBean);
        }
        mConfigDialog.addDataChangerListener(this);
        mConfigDialog.showLiveConfig();
    }


    private boolean hideMenuGroup() {
        if (mIsMenuItemVisible) {
            mMenuGroup.setVisibility(View.GONE);
            mIsMenuItemVisible = false;
            return true;
        }
        return false;
    }

    private void initView() {
        showProgressBar();

        if (mBackIcon == null) mBackIcon = findViewById(R.id.ib_home_back);
        if (mTitle == null) mTitle = findViewById(R.id.tv_home_title);
        if (mItem1 == null) mItem1 = findViewById(R.id.tv_home_item_1);
        if (mItem2 == null) mItem2 = findViewById(R.id.tv_home_item_2);
        if (mItem3 == null) mItem3 = findViewById(R.id.tv_home_item_3);
        if (mMenuGroup == null) mMenuGroup = findViewById(R.id.home_setting_menu_group);
        if (mSettingMenu == null) mSettingMenu = findViewById(R.id.iv_home_setting_menu);
        if (mSettingItem1 == null)
            mSettingItem1 = findViewById(R.id.bt_home_setting_menu_mall);
        if (mSettingItem2 == null)
            mSettingItem2 = findViewById(R.id.bt_home_setting_menu_live);

        mBackIcon.setOnClickListener(this);
        mItem1.setOnClickListener(this);
        mItem2.setOnClickListener(this);
        mItem3.setOnClickListener(this);
        mSettingMenu.setOnClickListener(this);
        mSettingItem1.setOnClickListener(this);
        mSettingItem2.setOnClickListener(this);

    }

    private void showProgressBar() {
        if (mProgressDialog == null) {
            mProgressDialog = new AlertDialog.Builder(this).create();
            mProgressDialog.setView(LayoutInflater.from(this)
                    .inflate(R.layout.layout_home_fetch_config_progress, null, false));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    /*
     * 打开直播 & 互娱
     */
    private void startLiveActivity() {
        ConfigUtil.saveConfig(this, mSettingsBean);
        Intent intent = new Intent(this, LiveOsActivity.class);
        startActivity(intent);
    }

    /**
     * 打开点播
     */
    private void startOsActivity() {
        Intent intent = new Intent(this, VideoOsActivity.class);
        intent.putExtra("settings", mSettingsBean);
        intent.putExtra("config_data", mData);
        startActivity(intent);
    }

    private void selectLive() {
        mLastStatus = HOME;
        // 显示关闭按钮
        mBackIcon.setVisibility(View.VISIBLE);
        mBackIcon.setClickable(true);
        // 隐藏第三个 item
        mItem3.setVisibility(View.GONE);
        // 显示设置菜单
        mSettingMenu.setVisibility(View.VISIBLE);
        mTitle.setText(getResources().getText(R.string.home_select_live_type));
        mItem1.setText(getResources().getText(R.string.home_live_anchor));
        mItem2.setText(getResources().getText(R.string.home_live_user));

    }

    private void selectAnchor() {
        mLastStatus = HOME_SELECT_LIVE;
        // 显示返回按钮
        mBackIcon.setVisibility(View.VISIBLE);
        mBackIcon.setClickable(true);
        // 显示第三个 item
        mItem3.setVisibility(View.VISIBLE);
        // 隐藏设置菜单
        mSettingMenu.setVisibility(View.GONE);
        mMenuGroup.setVisibility(View.GONE);
        mItem1.setText(getResources().getText(R.string.home_live_anchor_setting));
        mItem2.setText(getResources().getText(R.string.home_live_anchor_portrait));
        mItem3.setText(getResources().getText(R.string.home_live_anchor_landscape));
    }

    /**
     * 选择平台类型
     */
    private void backToHome() {
        mLastStatus = NONE;
        // 隐藏返回按钮
        mBackIcon.setClickable(false);
        mBackIcon.setVisibility(View.INVISIBLE);
        // 隐藏第三个 item
        mItem3.setVisibility(View.GONE);
        // 影藏设置选项
        mSettingMenu.setVisibility(View.GONE);
        mTitle.setText(getResources().getText(R.string.home_select_platform));
        mItem1.setText(getResources().getText(R.string.home_platform_live));
        mItem2.setText(getResources().getText(R.string.home_platform_video));
    }

    private void goBack() {
        switch (mLastStatus) {
            case HOME:
                backToHome();
                break;
            case HOME_SELECT_LIVE:
                selectLive();
                break;
        }
    }
}
