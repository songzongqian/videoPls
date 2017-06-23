package videodemo.com.cn.myapplication.weidget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import both.video.venvy.com.appdemo.R;
import cn.com.venvy.common.utils.VenvyUIUtil;
import io.vov.vitamio.widget.MediaController;

import static videodemo.com.cn.myapplication.weidget.CustomMediaController.Screen.LAND_SCAPE;
import static videodemo.com.cn.myapplication.weidget.CustomMediaController.Screen.PORTRAIT;
import static videodemo.com.cn.myapplication.weidget.CustomMediaController.Screen.PORTRAIT_FULL;


public class CustomMediaController extends MediaController {

    View mScreenChangeView;
    View mVerFullScreenChangeView;
    private boolean isLive = false;
    private Screen mCurrentScreenOrientation = Screen.PORTRAIT;  // 初始化竖屏
    private OnMediaScreenChangedListener mediaScreenChangedListener;

    public CustomMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMediaController(Context context) {
        super(context);
    }

    public CustomMediaController(Activity activity, boolean fromXml, View container) {
        super(activity);
        //此处初始化了AudioManager和Context
        initController(activity);
        //此处的activity是为后面获取当前屏幕方向而准备。接受Context类型再强制转换为activity也行。
        mContext = activity;
        //设置mFromXml为true
        mFromXml = fromXml;
        //初始化Controller布局文件
        mRoot = makeControllerView();
        if (container instanceof FrameLayout) {
            FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            p.gravity = Gravity.BOTTOM;
            mRoot.setLayoutParams(p);
            ((FrameLayout) container).addView(mRoot);
        }
        mCurrentScreenOrientation = VenvyUIUtil.isScreenOriatationPortrait(activity) ? Screen.PORTRAIT : Screen.LAND_SCAPE;
    }

    @Override
    protected View makeControllerView() {
        return ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.venvy_media_controller, this);
    }

    public void setMediaScreenChangedListener(OnMediaScreenChangedListener listener) {
        mediaScreenChangedListener = listener;
    }

    /**
     *
     */
    public void isLive(boolean isLive) {
        this.isLive = isLive;
        if (mVerFullScreenChangeView != null) {
            mVerFullScreenChangeView.setVisibility(isLive ? VISIBLE : GONE);
        }
        if (mProgress != null) {
            mProgress.setVisibility(!isLive ? VISIBLE : GONE);
        }
        if (mEndTime != null) {
            mEndTime.setVisibility(!isLive ? VISIBLE : GONE);
        }
        if (mPauseButton != null) {
            mPauseButton.setVisibility(!isLive ? VISIBLE : GONE);
        }
    }

    public void initView() {
        mEndTime = (TextView) this.findViewById(R.id.total_time);
        mCurrentTime = (TextView) this.findViewById(R.id.progress_text);
        mProgress = (SeekBar) this.findViewById(R.id.seekbar);
        if (mProgress != null) {
            mProgress.setOnSeekBarChangeListener(mSeekListener);
        }
        mPauseButton = (ImageButton) this.findViewById(R.id.start);
        mScreenChangeView = this.findViewById(R.id.screen);
        mVerFullScreenChangeView = this.findViewById(R.id.ver_full);

        mVerFullScreenChangeView.setVisibility(isLive ? VISIBLE : GONE);
        mProgress.setVisibility(!isLive ? VISIBLE : GONE);
        mEndTime.setVisibility(!isLive ? VISIBLE : GONE);
        mPauseButton.setVisibility(!isLive ? VISIBLE : GONE);


        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }
        if (mScreenChangeView != null) {
            mScreenChangeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeScreenOrientation();
                }
            });
        }

        if (mVerFullScreenChangeView != null) {
            mVerFullScreenChangeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeToFullVerticalScreen();
                }
            });
        }
    }

    @Override
    protected void initControllerView(View v) {
        initView();
    }

    @Override
    protected void updatePausePlay() {
        if (mPauseButton != null && mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPauseButton.setBackgroundResource(R.drawable.venvy_pause_player_icon);
            } else {
                mPauseButton.setBackgroundResource(R.drawable.venvy_start_player_icon);
            }
        }
    }

    private void changeToFullVerticalScreen() {

        Activity activity = null;
        if (getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }
        if (activity == null) {
            return;
        }
        //如果是竖屏
        if (mCurrentScreenOrientation == LAND_SCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mCurrentScreenOrientation = PORTRAIT;
        } else if (mCurrentScreenOrientation == PORTRAIT_FULL) {
            mCurrentScreenOrientation = PORTRAIT;
        } else if (mCurrentScreenOrientation == PORTRAIT) {
            mCurrentScreenOrientation = PORTRAIT_FULL;
        } else {
            return;
        }
        if (mediaScreenChangedListener != null) {
            mediaScreenChangedListener.screenChanged(mCurrentScreenOrientation);
        }
    }

    private void changeScreenOrientation() {
        Activity activity = null;
        if (getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }
        if (activity == null) {
            return;
        }
        //如果是竖屏
        if (!VenvyUIUtil.isScreenOriatationPortrait(activity)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mCurrentScreenOrientation = PORTRAIT;
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mCurrentScreenOrientation = LAND_SCAPE;
        }
        if (mediaScreenChangedListener != null) {
            mediaScreenChangedListener.screenChanged(mCurrentScreenOrientation);
        }
    }

    public interface OnMediaScreenChangedListener {
        void screenChanged(Screen screen);
    }

    public enum Screen {
        PORTRAIT,
        PORTRAIT_FULL,
        LAND_SCAPE
    }
}
