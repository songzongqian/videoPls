package videodemo.com.cn.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.com.venvy.common.utils.VenvyReflectUtil;


/**
 * 我的订单
 * Created by mac on 17/10/13.
 */

public class MyOrderAcitvity extends Activity {
    private static final String orderView = "cn.com.venvy.mall.test.MallOrderWebViewFactory";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }

    private void initView() {
        //屏幕的宽
        View view = (View)
                VenvyReflectUtil.invokeStatic(VenvyReflectUtil.getClass(orderView),
                        "initOrder", new Class[]{Context.class}, new Object[]{this});
        setContentView(view);

    }

}

