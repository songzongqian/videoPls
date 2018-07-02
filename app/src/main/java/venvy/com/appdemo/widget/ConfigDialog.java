package venvy.com.appdemo.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import cn.com.venvy.common.debug.DebugStatus;
import venvy.com.appdemo.R;
import venvy.com.appdemo.bean.OsConfigureBean;
import venvy.com.appdemo.bean.SettingsBean;
import venvy.com.appdemo.utils.ConfigUtil;

/**
 * Create by bolo on 07/06/2018
 */
public class ConfigDialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Context mContext;
    private SettingsBean mSettingsBean;
    private OsConfigureBean mData;
    private AlertDialog mDialog;

    private SettingChangedListener mChangedListener;
    private View mLiveConfig;
    private View mOsConfig;

    public static ConfigDialog get(Context context, OsConfigureBean data, SettingsBean bean) {
        return new ConfigDialog(context, data, bean);
    }

    private ConfigDialog(Context context, OsConfigureBean data, SettingsBean bean) {
        mContext = context;
        mSettingsBean = bean;
        mData = data;
        mDialog = new AlertDialog.Builder(context).create();
    }

    public void addDataChangerListener(SettingChangedListener listener) {
        mChangedListener = listener;
    }

    public void showOsSetting() {
        if (mData == null) {
            return;
        }
        if (mOsConfig == null) {
            mOsConfig = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_setting_os, null, false);
        }
        setDefaultChecked();
        initOsSettingLayout(mOsConfig);
        mDialog.setView(mOsConfig);
        mDialog.show();
    }

    public void showLiveConfig() {
        if (mData == null) {
            return;
        }
        if (mLiveConfig == null) {
            mLiveConfig = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_setting_live, null, false);
        }
        initLiveSettingLayout(mLiveConfig);
        mDialog.setView(mLiveConfig);
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_setting_apply) {
            ConfigUtil.saveConfig(mContext, mSettingsBean);
            if (mChangedListener != null) {
                mChangedListener.onDataChanger(mSettingsBean);
            }
        }
        if (mDialog != null) {
            mDialog.setView(null);
            mDialog.dismiss();
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_debug) {
            DebugStatus.changeEnvironmentStatus(DebugStatus.EnvironmentStatus.DEBUG);
            mSettingsBean.mStatus = DebugStatus.EnvironmentStatus.DEBUG;
        } else if (checkedId == R.id.rb_preview) {
            DebugStatus.changeEnvironmentStatus(DebugStatus.EnvironmentStatus.PREVIEW);
            mSettingsBean.mStatus = DebugStatus.EnvironmentStatus.PREVIEW;
        } else if (checkedId == R.id.rb_release) {
            DebugStatus.changeEnvironmentStatus(DebugStatus.EnvironmentStatus.RELEASE);
            mSettingsBean.mStatus = DebugStatus.EnvironmentStatus.RELEASE;
        }
    }


    private void initOsSettingLayout(View view) {
        initButtonAndRadioGroup(view);
        Spinner appIdSpinner = (Spinner) view.findViewById(R.id.sp_setting_app_id);
        Spinner videoPathSpinner = (Spinner) view.findViewById(R.id.sp_setting_app_video_path);

        appIdSpinner.setAdapter(new ArrayAdapter(mContext,
                android.R.layout.simple_list_item_activated_1, mData.platformIdList));
        videoPathSpinner.setAdapter(new ArrayAdapter(mContext,
                android.R.layout.simple_list_item_activated_1, mData.roomIdList));

        appIdSpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.platformIdList != null) {
                    mSettingsBean.mPlatformId = mData.platformIdList.get(position);
                }
            }
        });

        videoPathSpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.roomIdList != null) {
                    mSettingsBean.mRoomId = mData.roomIdList.get(position);
                }
            }
        });
    }

    private void setDefaultChecked() {
        mData = ConfigUtil.getConfig(mContext, mData);
    }


    /**
     * 参数的设置
     */
    private void initLiveSettingLayout(View view) {
        initButtonAndRadioGroup(view);

        Spinner cateSpinner = view.findViewById(R.id.sp_layout_setting_cate);
        Spinner uIdSpinner = view.findViewById(R.id.sp_setting_uid);
        Spinner roomIdSpinner = view.findViewById(R.id.sp_setting_room);
        Spinner platformIdSpinner = view.findViewById(R.id.sp_setting_platform);

        cateSpinner.setAdapter(new ArrayAdapter(mContext,
                android.R.layout.simple_list_item_activated_1, mData.cateList));
        uIdSpinner.setAdapter(new ArrayAdapter(mContext,
                android.R.layout.simple_list_item_activated_1, mData.userIdList));
        roomIdSpinner.setAdapter(new ArrayAdapter(mContext,
                android.R.layout.simple_list_item_activated_1, mData.roomIdList));
        platformIdSpinner.setAdapter(new ArrayAdapter(mContext,
                android.R.layout.simple_list_item_activated_1, mData.platformIdList));

        // 分区
        cateSpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.cateList != null) {
                    mSettingsBean.mCate = mData.cateList.get(position);
                }
            }
        });
        // UserId
        uIdSpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.userIdList != null) {
                    mSettingsBean.mUid = mData.userIdList.get(position);
                }
            }
        });
        // RoomId
        roomIdSpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.roomIdList != null) {
                    mSettingsBean.mRoomId = mData.roomIdList.get(position);
                }
            }
        });
        // PlatformId
        platformIdSpinner.setOnItemSelectedListener(new OnItemSelectedListenerAdapter() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null && mData.platformIdList != null) {
                    mSettingsBean.mPlatformId = mData.platformIdList.get(position);
                }
            }
        });
    }

    private void initButtonAndRadioGroup(View view) {
        view.findViewById(R.id.bt_setting_cancel).setOnClickListener(this);
        view.findViewById(R.id.bt_setting_apply).setOnClickListener(this);

        int status = mContext.getSharedPreferences(ConfigUtil.SP_DEMO_CONFIG, Activity.MODE_PRIVATE)
                .getInt(ConfigUtil.SP_DEMO_CONFIG_STATUS, -1);
        if (status >= 0) {
            mSettingsBean.mStatus = DebugStatus.EnvironmentStatus.getStatusByIntType(status);
        }


        RadioGroup mEnvironGroup = (RadioGroup) view.findViewById(R.id
                .layout_setting_change_environment);
        mEnvironGroup.setOnCheckedChangeListener(this);
        switch (mSettingsBean.mStatus) {
            case DEBUG:
                mEnvironGroup.check(R.id.rb_debug);
                break;
            case PREVIEW:
                mEnvironGroup.check(R.id.rb_preview);
                break;
            case RELEASE:
                mEnvironGroup.check(R.id.rb_release);
                break;
        }
    }


    public interface SettingChangedListener {
        void onDataChanger(SettingsBean bean);
    }

    static abstract class OnItemSelectedListenerAdapter implements AdapterView
            .OnItemSelectedListener {
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

}
