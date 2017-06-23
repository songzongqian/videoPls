package videodemo.com.cn.myapplication.weidget;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.io.File;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by yanjiangbo on 2017/6/13.
 */

public class CustomVideoView extends VideoView {


    public CustomVideoView(Context context) {
        this(context, null, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }

    @Override
    public void setVideoPath(String path) {
        String locatePath = getCachePath(getContext()) + "/video++/cache/preload/preloadmedia/" + path.hashCode();
        File file = new File(locatePath);
        if (file.exists()) {
            super.setVideoPath(locatePath);
            return;
        }
        super.setVideoPath(path);
    }

    public static String getCachePath(Context context) {

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    @Override
    public void setMediaController(MediaController controller) {
        super.setMediaController(controller);
    }

    public void rebuildSize(int width, int height) {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.width = width;
        lp.height = height;
        this.setLayoutParams(lp);
    }

    public void setPlaybackSpeed(long position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setPlaybackSpeed(position);
        }
    }
}
