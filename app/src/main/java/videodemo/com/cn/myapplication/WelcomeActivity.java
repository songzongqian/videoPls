package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import both.video.venvy.com.appdemo.R;

public class WelcomeActivity extends Activity implements View.OnClickListener {

    private ImageView mImageDianbo, mImageLive;
    private Button mOttButton, mMallBtn, mMyOderBtn, mHuyuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        findView();
        initListerner();
    }

    private void findView() {
        mImageDianbo = (ImageView) findViewById(R.id.btn_dianbo);
        mImageLive = (ImageView) findViewById(R.id.btn_live);
        mOttButton = (Button) findViewById(R.id.ott);
        mMallBtn = (Button) findViewById(R.id.mall);
        mMyOderBtn = (Button) findViewById(R.id.myoder);
        mHuyuBtn = (Button) findViewById(R.id.huyu);
    }

    private void initListerner() {
        mImageDianbo.setOnClickListener(this);
        mImageLive.setOnClickListener(this);
        mOttButton.setOnClickListener(this);
        mMallBtn.setOnClickListener(this);
        mMyOderBtn.setOnClickListener(this);
        mHuyuBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dianbo:
                goDianboActivity();
                break;
            case R.id.btn_live:
                goLiveActivity();
                break;
            case R.id.ott:
                goOTTActivity();
                break;
            case R.id.mall:
                goMallActivity();
                break;
            case R.id.myoder:
                goOrderActivity();
                break;
            case R.id.huyu:
                goHuyuActivity();
                break;
            default:
                break;
        }

    }


    private void goOTTActivity() {
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this,
                VideoOTTActivity.class);
        startActivity(intent);
    }


    private void goDianboActivity() {
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, VideoOsActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }


    private void goLiveActivity() {
        final Intent intent = new Intent(WelcomeActivity.this, LiveOsActivity.class);
        startActivity(intent);
    }

    private void goMallActivity() {
        final Intent intent = new Intent(WelcomeActivity.this, MallOsActivity.class);
        startActivity(intent);
    }

    private void goOrderActivity() {
        final Intent intent = new Intent(WelcomeActivity.this, MyOrderAcitvity.class);
        startActivity(intent);
    }

    private void goHuyuActivity() {
        final Intent intent = new Intent(WelcomeActivity.this, HuyuOsActivity.class);
        startActivity(intent);
    }
}
