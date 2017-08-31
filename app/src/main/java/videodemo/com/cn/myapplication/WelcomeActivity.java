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

public class WelcomeActivity extends Activity implements View.OnClickListener{

    private ImageView btnDianbo, btnLive;
    private AlertDialog mDialog;
    private Button mOttButton;

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
        mOttButton = (Button) findViewById(R.id.ott);
    }

    private void initListerner(){
        btnDianbo.setOnClickListener(this);
        btnLive.setOnClickListener(this);
        mOttButton.setOnClickListener(this);
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
            case R.id.ott:
                goOTTActivity();
                break;
            default:
                break;
        }

    }


    private void goOTTActivity() {
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, OTTActivity.class);
        startActivity(intent);
    }


    private void goDianboActivity(){
        Intent intent=new Intent();
        intent.setClass(WelcomeActivity.this, VideoOsActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }


    private void goLiveActivity(){

        View selectRoom = getLayoutInflater().inflate(R.layout.layout_select_room, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("设置房间号")
                .setView(selectRoom);
        mDialog = builder.create();
        mDialog.show();

        setRoomId(selectRoom);
    }

    private void setRoomId(final View selectRoom) {

        final Intent intent=new Intent(WelcomeActivity.this, LiveOsActivity.class);

        final EditText et = (EditText) selectRoom.findViewById(R.id.edittext);

        selectRoom.findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomId = et.getText().toString();
                intent.putExtra("roomId", roomId);
                startActivity(intent);
                mDialog.dismiss();
            }
        });

    }


}
