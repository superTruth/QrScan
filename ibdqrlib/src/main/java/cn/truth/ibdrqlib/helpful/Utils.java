package cn.truth.ibdrqlib.helpful;

import android.content.Context;
import android.content.res.Configuration;

public class Utils {

    // 获取屏幕方向
    public static boolean isPortrait(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        if (mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }
}
