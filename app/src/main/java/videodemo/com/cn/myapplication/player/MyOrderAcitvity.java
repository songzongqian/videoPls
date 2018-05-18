package videodemo.com.cn.myapplication.player;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import cn.com.venvy.common.bean.PlatformUserInfo;
import cn.com.venvy.common.exception.LoginException;
import cn.com.venvy.common.interf.IPlatformLoginInterface;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.venvy.keep.MallConfig;
import cn.com.venvy.mall.view.MallWebView;


/**
 * 我的订单
 * Created by mac on 17/10/13.
 */

public class MyOrderAcitvity extends Activity {
    private MallWebView mallWebView;
    private IPlatformLoginInterface loginInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initLogin();
        initView();
    }

    private void initView() {
        //屏幕的宽
        int width = VenvyUIUtil.getScreenWidth(this);
        //屏幕的高
        int height = VenvyUIUtil.getScreenHeight(this);
        mallWebView = new MallWebView(this);
        mallWebView.setSsId(System.currentTimeMillis()+"");
        mallWebView.setPlatformLoginInterface(loginInterface);

        FrameLayout.LayoutParams
                params = new FrameLayout.LayoutParams(width, height);
        mallWebView.setLayoutParams(params);

        setContentView(mallWebView);
        mallWebView.loadUrl(MallConfig.getOrderUrl());
    }

    private void initLogin() {
        loginInterface = new IPlatformLoginInterface() {

            @Override
            public PlatformUserInfo getLoginUser() {
                //代码事例
                PlatformUserInfo platformUserInfo = new PlatformUserInfo();
                platformUserInfo.setUid("必填");
                platformUserInfo.setNickName("nullable");
                platformUserInfo.setUserName("nullable");
                platformUserInfo.setPhoneNum("nullable");
                platformUserInfo.setUserToken("nullable");
                return platformUserInfo;
            }


            @Override
            public void userLogined(PlatformUserInfo userInfo) {
                if (userInfo != null) {
                    String token = userInfo.getUserToken();
                }
                //此接口为h5页面完成登录操作后通知app更新登录状态

            }

            @Override
            public void login(LoginCallback loginCallback) {
                //此接口方法为h5请求app打开自己的登录页面，app登录成功后将用户idd等信息回调给h5
                if(true) {//登录成功回调
                    PlatformUserInfo platformUserInfo = new PlatformUserInfo();
                    platformUserInfo.setUid("必填");
                    platformUserInfo.setNickName("nullable");
                    platformUserInfo.setUserName("nullable");
                    platformUserInfo.setPhoneNum("nullable");
                    platformUserInfo.setUserToken("nullable");
                    loginCallback.loginSuccess(platformUserInfo);
                }else {
                    loginCallback.loginError(new LoginException("登录失败"));
                }
            }

            @Override
            public void screenChanged(ScreenChangedInfo changedInfo) {

            }
        };
    }

    /**
     * 处理订单h5的返回逻辑
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mallWebView.canGoBack()) {
            mallWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}