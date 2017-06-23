package videodemo.com.cn.myapplication;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import cn.com.venvy.common.utils.VenvyDebug;

/**
 * Created by lgf on 2017/3/22.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
        Fresco.initialize(this, config);
        //强制设置为测试环境，如果要在正式环境测试，可删除下面代码
        if (!VenvyDebug.getInstance().isDebug()) {
            VenvyDebug.getInstance().toggle();
        }
    }
}
