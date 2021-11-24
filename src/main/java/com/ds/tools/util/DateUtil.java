package com.ds.tools.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(date);
        return time;
    }
}
