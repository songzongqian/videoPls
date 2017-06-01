package videodemo.com.cn.myapplication;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by lgf on 2017/3/22.
 */

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
        Fresco.initialize(this, config);
    }
}
