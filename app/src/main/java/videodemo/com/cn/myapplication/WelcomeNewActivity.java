package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.Platform;
import cn.com.venvy.common.utils.VenvyAsyncTaskUtil;
import cn.com.venvy.common.utils.VenvyFileUtil;
import cn.com.venvy.common.utils.VenvyReflectUtil;
import videodemo.com.cn.myapplication.player.LiveOsActivity;
import videodemo.com.cn.myapplication.player.MallOsActivity;
import videodemo.com.cn.myapplication.player.VideoOTTActivity;
import videodemo.com.cn.myapplication.player.VideoOsActivity;

import static videodemo.com.cn.myapplication.HyRoomType.ROOM_ANCHOR_CONFIG;
import static videodemo.com.cn.myapplication.HyRoomType.ROOM_ANCHOR_LANDSCAPE;
import static videodemo.com.cn.myapplication.HyRoomType.ROOM_ANCHOR_VERTICAL;
import static videodemo.com.cn.myapplication.HyRoomType.ROOM_USER;


public class WelcomeNewActivity extends Activity {

    private Button mClearButton, mOttButton, mBtnDianbo, mBtnLive, mMallBtn, mOrderBtn, mHuYuBtn;
    Platform testPlatFrom;
    private AlertDialog mSelectorRoomDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_new);
        findView();
        initListerner();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void findView() {
        mBtnDianbo = (Button) findViewById(R.id.btn_dianbo);
        if (VenvyReflectUtil.getClass("cn.com.venvy.keep.LocationPresenter") == null) {
            mBtnDianbo.setVisibility(View.GONE);
        }
        mBtnLive = (Button) findViewById(R.id.btn_live);
        if (VenvyReflectUtil.getClass("cn.com.venvy.keep.LiveOsManager") == null) {
            mBtnLive.setVisibility(View.GONE);
        }
        mClearButton = (Button) findViewById(R.id.clear);
        mOttButton = (Button) findViewById(R.id.ott);
        if (VenvyReflectUtil.getClass("cn.com.venvy.keep.OTTLocationPresenter") == null) {
            mOttButton.setVisibility(View.GONE);
        }

        mOrderBtn = (Button) findViewById(R.id.myOrder);
        mMallBtn = (Button) findViewById(R.id.mall);
        if (VenvyReflectUtil.getClass("cn.com.venvy.keep.MallViewHelper") == null) {
            mOrderBtn.setVisibility(View.GONE);
            mMallBtn.setVisibility(View.GONE);
        }

        mHuYuBtn = (Button) findViewById(R.id.btn_huyu);
        if (VenvyReflectUtil.getClass("cn.com.venvy.keep.HuYuController") == null) {
            mHuYuBtn.setVisibility(View.GONE);
        }

        mOttButton.requestFocus();

    }

    private void initListerner() {
        mBtnDianbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDianboActivity();
            }
        });
        mBtnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLiveActivity();
            }
        });
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCache();
            }
        });
        mOttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOTTActivity();
            }
        });
        mHuYuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectorDialog();
            }
        });
        mMallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMallActivity();
            }
        });
    }


    private void goDianboActivity() {
        Intent intent = new Intent();
        intent.setClass(WelcomeNewActivity.this, VideoOsActivity.class);
        WelcomeNewActivity.this.startActivity(intent);
    }

    private void goLiveActivity() {
        final Intent intent = new Intent(WelcomeNewActivity.this, LiveOsActivity.class);
        startActivity(intent);
    }

    private void goHuyuActivity(@HyRoomType int type) {

    }

    private void addSelectorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_selector_room_type, null);
        builder.setView(view);
        view.findViewById(R.id.tv_popup_selector_anchor_vertical).setOnClickListener(new View
                .OnClickListener() {

            @Override
            public void onClick(View v) {
                goHuyuActivity(ROOM_ANCHOR_VERTICAL);
            }
        });
        view.findViewById(R.id.tv_popup_selector_user).setOnClickListener(new View
                .OnClickListener() {

            @Override
            public void onClick(View v) {
                goHuyuActivity(ROOM_USER);
            }
        });
        view.findViewById(R.id.tv_popup_selector_anchor_landscape).setOnClickListener(new View
                .OnClickListener() {

            @Override
            public void onClick(View v) {
                goHuyuActivity(ROOM_ANCHOR_LANDSCAPE);
            }
        });
        view.findViewById(R.id.tv_popup_selector_anchor_config).setOnClickListener(new View
                .OnClickListener() {

            @Override
            public void onClick(View v) {
                goHuyuActivity(ROOM_ANCHOR_CONFIG);
            }
        });
        mSelectorRoomDialog = builder.show();
    }

    private void goOTTActivity() {
        Intent intent = new Intent();
        intent.setClass(WelcomeNewActivity.this, VideoOTTActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (testPlatFrom != null) {
            testPlatFrom.onDestroy();
        }
    }

    private void goMallActivity() {
        Intent intent = new Intent();
        intent.setClass(WelcomeNewActivity.this, MallOsActivity.class);
//        intent.setClass(WelcomeNewActivity.this, MallActivity.class);
        startActivity(intent);
    }

    private void clearCache() {
        VenvyAsyncTaskUtil.doAsyncTask("delete_cache", new VenvyAsyncTaskUtil.IDoAsyncTask<Void,
                Void>() {
            @Override
            public Void doAsyncTask(Void... voids) throws Exception {
                Glide.get(WelcomeNewActivity.this).clearDiskCache();
                VenvyFileUtil.deleteFile(WelcomeNewActivity.this, VenvyFileUtil.getCachePath
                        (WelcomeNewActivity.this));
                return null;
            }
        }, new VenvyAsyncTaskUtil.IAsyncCallback<Void>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Void aVoid) {
                Toast.makeText(WelcomeNewActivity.this, "清除缓存成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled() {

            }

            @Override
            public void onException(Exception ie) {

            }
        });
    }
}