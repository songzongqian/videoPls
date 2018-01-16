package videodemo.com.cn.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.bean.WidgetInfo;
import cn.com.venvy.common.interf.IPlatformLoginInterface;
import cn.com.venvy.common.interf.IWidgetCloseListener;
import cn.com.venvy.common.interf.IWidgetShowListener;
import cn.com.venvy.common.utils.VenvyLog;
import cn.com.videopls.pub.Provider;
import cn.com.videopls.pub.VideoPlusAdapter;
import cn.com.videopls.pub.VideoPlusView;
import cn.com.videopls.pub.huyu.EnjoyAdapter;
import cn.com.videopls.pub.huyu.PlatFormUserInfoImpl;
import cn.com.videopls.pub.live.VideoLiveView;
import videodemo.com.cn.myapplication.bean.SettingsBean;

public class HuyuOsActivity extends LiveBaseActivity implements TextWatcher {

    private View mHuyuAnchorView;
    private TextView mPreConfig;
    private EditText mUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHuyuAnchorView = getLayoutInflater().inflate(R.layout.activity_huyu_sublayout, getContentView(), true);
        mVideoLiveView.bringToFront();
        initView();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mUId == null) {
            return;
        }
        VenvyLog.i("onTextChanged s = " + s.toString());
        String roomId = s.toString();
        mUId.setText("12007866" + roomId.substring(roomId.length() - 1));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void initSettingsValue() {
        mSettingsBean = new SettingsBean();
        mSettingsBean.mRoomId = "2121651";
        mSettingsBean.mPlatformId = "577a572aefd57d4b00ebdcb0";
        mSettingsBean.mCate = "";
        mSettingsBean.isAnchor = true;
        mSettingsBean.uId = "120078661";
    }

    @NonNull
    @Override
    protected VideoPlusView initVideoPlusView() {
        return mVideoLiveView = new VideoLiveView(this);
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

    protected void setSmallScreen() {
        FrameLayout.LayoutParams rootParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                mWidowPlayerHeight);
        mCustomVideoView.setLayoutParams(rootParams);
    }

    protected void setFullScreen() {
        FrameLayout.LayoutParams rootParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mCustomVideoView.setLayoutParams(rootParams);
    }


    private void initView() {
        mRootView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        mCustomVideoView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, mWidowPlayerHeight));

        mPreConfig = (TextView) mHuyuAnchorView.findViewById(R.id.pre_config);

        setAppType();

        mPreConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoLiveView.openPreConfig();
            }
        });
    }

    private void setAppType() {
        if (mSettingsBean.isAnchor) {
            mPreConfig.setVisibility(View.VISIBLE);
        } else {
            mPreConfig.setVisibility(View.GONE);
        }
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
                    platformUserInfo.isPush = mSettingsBean.isPush;
                    platformUserInfo.platformId = mSettingsBean.mPlatformId;
                    return platformUserInfo;
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
                        if (info.getWidgetType() == WidgetInfo.WidgetType.CLOUND) {
                            mPreConfig.setVisibility(View.GONE);
                        }
                    }
                }
            };
        }

        @Override
        public IWidgetCloseListener buildWidgetCloseListener() {
            return new IWidgetCloseListener<WidgetInfo>() {
                @Override
                public void onClose(WidgetInfo info) {
                    if (info != null) {
                        VenvyLog.i("--互娱关闭" + info.getWidgetType());
                        if (info.getWidgetType() == WidgetInfo.WidgetType.CLOUND) {
                            mPreConfig.setVisibility(View.VISIBLE);
                        }
                    }
                }
            };
        }
    }

    @Override
    protected View getInflate(LayoutInflater inflater) {
        return inflater.inflate(R.layout.pop_up_huyu, null);
    }

    @Override
    protected void initButtons(View contentview, final PopupWindow popupWindow) {
        final EditText roomId = (EditText) contentview.findViewById(R.id.et_roomId);
        roomId.addTextChangedListener(this);
        roomId.setText(mSettingsBean.mRoomId);

        mUId = (EditText) contentview.findViewById(R.id.et_uId);
        mUId.setText(mSettingsBean.uId);

        final EditText platfromId = (EditText) contentview.findViewById(R.id.et_platform);
        platfromId.setText(mSettingsBean.mPlatformId);

        Spinner cateSpinner = (Spinner) contentview.findViewById(R.id.cate);
        setSpinnerItemSelectedByValue(cateSpinner, mSelectCate);
        cateSpinner.setOnItemSelectedListener(this);

        final int id = initEnvButtons(contentview);

        final TextView textViewUser = (TextView) contentview.findViewById(R.id.tv_user);
        CheckBox checkBoxUser = (CheckBox) contentview.findViewById(R.id.cb_user);
        setUserText(mSettingsBean.isAnchor, textViewUser);
        checkBoxUser.setChecked(mSettingsBean.isAnchor);
        checkBoxUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettingsBean.isAnchor = isChecked;
                setUserText(mSettingsBean.isAnchor, textViewUser);
            }
        });
        Button apply = (Button) contentview.findViewById(R.id.btn_live);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsBean.mRoomId = roomId.getText().toString();
                mSettingsBean.mPlatformId = platfromId.getText().toString();
                mSettingsBean.uId = mUId.getText().toString();
                if (!TextUtils.isEmpty(mSelectCate) && mSelectCate.length() > 1) {
                    mSettingsBean.mCate = mSelectCate.split("/")[0];
                }
                debugToggle(id);
                setAppType();
                updateAdapter();
                initView();
                //update adapter
                popupWindow.dismiss();
            }
        });

        CheckBox checkStatus = (CheckBox) contentview.findViewById(R.id.cb_status);
        final TextView textStatus = (TextView) contentview.findViewById(R.id.tv_status);
        setStatusText(textStatus);
        checkStatus.setChecked(mSettingsBean.isPush);
        checkStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettingsBean.isPush = isChecked;
                setStatusText(textStatus);
            }
        });

    }

    private void setStatusText(TextView textStatus) {
        textStatus.setText(mSettingsBean.isPush ? "已开播" : "未开播");
    }

    private void updateAdapter() {
        videoPlusView.stop();
        Provider provider = new Provider.Builder()
                .build();
        getAdapter().updateProvider(provider);
        videoPlusView.start();
    }


    protected void setUserText(boolean isChecked, TextView textView) {
        if (isChecked) {
            textView.setText(this.getString(R.string.huyu_anchor));
        } else {
            textView.setText(this.getString(R.string.huyu_user));
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
