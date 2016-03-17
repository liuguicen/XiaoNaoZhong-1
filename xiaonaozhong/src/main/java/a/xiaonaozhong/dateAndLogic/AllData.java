package a.xiaonaozhong.dateAndLogic;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;

public class AllData {
    public static final int DEFAUL_SOUND_LONG = 5;
    public static final int DEFAUL_RESOUND_CISHU = 10;
    public static final int DEFAUL_RESOUND_INTERVAL = 10;
    public static final String REPEAT = "REPEAT";
    public static final String RESOUND_CISHU = "resoundCishu";
    public static final String RESOUND_INTERVAL ="resoundInterval";
    public static final String SOUND_LONG ="soundLong" ;
    public static final String POSITION ="position" ;
    public static final String[] XINGQI = new String[]{"周一","周二",
            "周三","周四","周五","周六","周日"} ;
    public static String MAX_ID="maxId";
    public static String BACK_SERVICE="LGC_BACK_SERVICE";
    public static int NAOZHONG_NUMBER=1;

    public static void setDefaultShake(Boolean v) {
        DEFAULT_SHAKE=v;
    }

    public static String getDefaultMusicPath(Context context) {
        if(DEFAULT_MUSIC_PATH==null)
            getSetting(context);
        return DEFAULT_MUSIC_PATH;
    }
    public static void setDefaultMusicPath(String s) {
        DEFAULT_MUSIC_PATH=s;
    }

    public static String getDefaultPicturePath(Context context) {
        if(DEFAULT_PICTURE_PATH==null)
            getSetting(context);
        return DEFAULT_PICTURE_PATH;
    }
    public static void setDefaultPicturePath(String s) {
        DEFAULT_PICTURE_PATH=s;
    }

    public static  String DEFAULT_MUSIC_PATH;
    public static final String TIME_ZONE = "GMT+8";

    public static final String NAME = "name";
    public static final String DEFLAUL_NAME = "提醒";
    public static final String MUSIC_PATH = "musicPath";
    public static final String SHAKE = "shake";
    public static Boolean DEFAULT_SHAKE;
    public static Boolean getDefaultShake(Context context) {
        if(DEFAULT_SHAKE==null)
            getSetting(context);
        return DEFAULT_SHAKE;
    }
    public static final String LABLE = "lable";
    public static final String DEFAUL_LABLE = "标签";
    public static final String PICTURE_PATH = "/Wallpaper";
    public static String DEFAULT_PICTURE_PATH ;
    public static final String OPEN = "open";
    public static final String TIME="time";
    public static final Boolean DEFAUL_OPEN = true;
    public static final String COUNT = "count";

    public static final String ANOZHONG_NAME = "闹钟";
    public static final String OPEN_TEXT = "点击关闭";
    public static final String CLOSE_TEXT = "点击开启";
    public static final String ADD_ICON_TEXT = "发送图标";
    public static final String DEL_ICON_TEXT = "删除图标";

    public static final String SHORTCUT_ACTION = "com.android.action.test";
    public static final String TIXING_NAME = "提醒";
    public static final String BTN_OPEN_COLOR = "gray";
    public static final String BTN_CLOSE_COLOR = "blue";
    public static final String ACTION_SET_ALL_NAOZHONG = "xiaonaozhong.action.set.all.naozhong.activity";

    public static final String SETTINGS_SP_SHAKE="settings_sp_shake";
    public static final String SETTINGS_SP_MUSIC="settings_sp_music";
    public static final String SETTINGS_SP_PICTURE="settings_sp_picture";

    public static String getFileName(String s) {
        return s.substring(s.lastIndexOf("/") + 1, s.length());
    }

    public static String getFormatTime(String time) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr.substring(11, 16);
    }

    /**
     *
     * @param time timemils
     *
     * @return 24小时制，小时:分钟
     */
    public static String getFormatTime(Long time) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr.substring(11, 16);
    }

    public static String getFormatDis(Long time) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(Long.valueOf(time));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ds = sdf.format(date);
        StringBuilder chinese=new StringBuilder();
        int hour = Integer.valueOf(ds.substring(11, 13));
        if (hour < 10) chinese.append("  " + hour);
        else chinese.append(hour);
        chinese.append("小时");
        int minut = Integer.valueOf(ds.substring(14, 16));
        if (minut < 10) chinese.append("  " + minut);
        else chinese.append(minut);
        chinese.append("分钟");
        return chinese.toString();
    }

    /**
     * 开始启动应用是获取数据，以后运行过程中不在获取，数据会同时到内存和外存中
     * @param context
     */
    public static void getSetting(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("setting", context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        DEFAULT_SHAKE = sp.getBoolean(AllData.SETTINGS_SP_SHAKE, true);
        DEFAULT_MUSIC_PATH = sp.getString(AllData.SETTINGS_SP_MUSIC, "default/defaultAlarm");
        DEFAULT_PICTURE_PATH = sp.getString(AllData.SETTINGS_SP_PICTURE, "picturePath");
    }
    /**
     * 注意要同时保存到内存和sd卡即sp中
     */
    public static void saveSettings(Context context,Boolean defaulltShake, String defaultMusicPath, String defaultMusicPath1) {
        SharedPreferences sp = context.getSharedPreferences("setting", context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        DEFAULT_SHAKE=defaulltShake;
        DEFAULT_MUSIC_PATH=defaultMusicPath;
        DEFAULT_PICTURE_PATH=defaultMusicPath1;

        spEditor.putBoolean(AllData.SETTINGS_SP_SHAKE, defaulltShake);
        spEditor.putString(AllData.SETTINGS_SP_MUSIC, defaultMusicPath);
        spEditor.putString(AllData.SETTINGS_SP_PICTURE, defaultMusicPath1);
        spEditor.commit();

    }

}
