package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity implements View.OnClickListener{

    private ImageView btnDianbo, btnLive;

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
        btnDianbo = (ImageView) findViewById(R.id.btn_dianbo);
        btnLive = (ImageView) findViewById(R.id.btn_live);
    }

    private void initListerner(){
        btnDianbo.setOnClickListener(this);
        btnLive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dianbo:
                goDianboActivity();
                break;
            case R.id.btn_live:
                goLiveActivity();
                break;
            default:
                break;
        }

    }

    private void goDianboActivity(){
        Intent intent=new Intent();
        intent.setClass(WelcomeActivity.this, MainActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }

    private void goLiveActivity(){
        Intent intent=new Intent();
        intent.setClass(WelcomeActivity.this, LiveActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }

}
