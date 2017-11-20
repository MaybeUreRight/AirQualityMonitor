package lb.demo.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by liubo on 2017/11/20.
 */

public class TimeUtil {

    /**
     * 将字符串格式的时间转为毫秒值
     *
     * @param dateTime 字符串时间（格式：2015-12-31 23:59:53）
     *
     * @return
     */
    public static Long getLongFromString(String dateTime) {
        if (TextUtils.isEmpty(dateTime)) {
            return null;
        } else {
            try {
                /**
                 * 将字符串数据转化为毫秒数
                 */
                StringBuffer buffer = new StringBuffer();
                char[] charArray = dateTime.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                    if (Character.isDigit(charArray[i])) {
                        buffer.append(charArray[i]);
                    }
                }
                dateTime = buffer.toString();
                buffer = null;
                Calendar c = Calendar.getInstance();
                c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(dateTime));
                return c.getTimeInMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
