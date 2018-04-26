package videodemo.com.cn.myapplication;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by qinpc on 2018/3/19
 */
@IntDef({HyRoomType.ROOM_ANCHOR_LANDSCAPE, HyRoomType.ROOM_ANCHOR_VERTICAL, HyRoomType.ROOM_USER, HyRoomType.ROOM_ANCHOR_CONFIG})
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface HyRoomType {
    int ROOM_ANCHOR_LANDSCAPE = 1;
    int ROOM_ANCHOR_VERTICAL = 2;
    int ROOM_USER = 3;
    int ROOM_ANCHOR_CONFIG = 4;
}
