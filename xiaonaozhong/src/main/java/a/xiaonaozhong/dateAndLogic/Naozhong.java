package a.xiaonaozhong.dateAndLogic;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/2/25.
 * 所有的闹钟存到一个sp naozhong中，格式id+key+id
 */
public class Naozhong {
    int id;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    boolean open=true;
    public void setTime(Long time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepeat(boolean[] repeat) {
        this.repeat = repeat;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public void setShake(Boolean shake) {
        this.shake = shake;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public void setSoundLong(int soundLong) {
        this.soundLong = soundLong;
    }

    public void setResoundCishu(int resoundCishu) {
        this.resoundCishu = resoundCishu;
    }

    public void setResoundInterval(int resoundInterval) {
        this.resoundInterval = resoundInterval;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public Boolean getShake() {
        return shake;
    }

    public String getLable() {
        return lable;
    }

    public int getSoundLong() {
        return soundLong;
    }

    public int getResoundCishu() {
        return resoundCishu;
    }

    public int getResoundInterval() {
        return resoundInterval;
    }

    Long time;
    String name;

    public Long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean[] getRepeat() {
        return repeat;
    }

    boolean[] repeat;
    String musicPath;

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    String picturePath;
    Boolean shake;
    String lable;
    int soundLong;
    int resoundCishu;
    int resoundInterval;

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    private void init(Context context, int id) {
        this.id = id;
        this.context = context;
        sp = context.getSharedPreferences("naozhong", Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    /**
     * 取出以前的Naozhong，
     *
     * @param context
     * @param id
     */
    private Naozhong(Context context, int id) {
        init(context, id);

        open=sp.getBoolean(id+AllData.OPEN,true);

        time = sp.getLong(id + AllData.TIME, 0);
        name = sp.getString(id + AllData.NAME, " ");
        repeat=new boolean[7];
        for (int i = 0; i < 7; i++) {
            repeat[i] = sp.getBoolean(id + AllData.REPEAT + i, true);
        }
        musicPath = sp.getString(id + AllData.MUSIC_PATH, "　");
        picturePath = sp.getString(id + AllData.PICTURE_PATH, "　");
        shake = sp.getBoolean(id + AllData.SHAKE, true);
        lable = sp.getString(id + AllData.LABLE, " ");
        soundLong = sp.getInt(id + AllData.SOUND_LONG, 0);
        resoundCishu = sp.getInt(id + AllData.RESOUND_CISHU, 0);
        resoundInterval = sp.getInt(id + AllData.RESOUND_INTERVAL, 0);
    }

    /**
     * 创建一个新的闹钟
     *
     * @param context
     */
    private Naozhong(Context context) {
        NaozhongManager nzManager = NaozhongManager.getInstance(context);
        AllData.getSetting(context);
        id = nzManager.getNextId();
        init(context, id);
        open=AllData.DEFAUL_OPEN;
        time = System.currentTimeMillis();
        name = AllData.DEFLAUL_NAME;
        repeat = new boolean[]{true, true, true, true, true, true, true};
        musicPath = AllData.DEFAULT_MUSIC_PATH;
        picturePath = AllData.DEFAULT_PICTURE_PATH;

        shake = AllData.DEFAULT_SHAKE;
        lable = AllData.DEFAUL_LABLE;
        soundLong = AllData.DEFAUL_SOUND_LONG;
        resoundCishu = AllData.DEFAUL_RESOUND_CISHU;
        resoundInterval = AllData.DEFAUL_RESOUND_INTERVAL;
    }

    /**
     * 创建一个新的闹钟
     */
    public static Naozhong createNaozhong(Context context) {
        return new Naozhong(context);
    }

    /**
     * 获取已存在的闹钟
     *
     * @param context
     * @param id
     * @return
     */
    public static Naozhong getNaozhong(Context context, int id) {
        return new Naozhong(context, id);
    }

    public void save() {
        init(context, id);
        spEditor.putInt(id + "id", id);
        spEditor.putBoolean(id+AllData.OPEN,open);
        spEditor.putLong(id + AllData.TIME,time);
        spEditor.putString(id + AllData.NAME,name);
        for (int i = 0; i < 7; i++) {
            spEditor.putBoolean(id + AllData.REPEAT + i,repeat[i]);
        }
        spEditor.putString(id + AllData.MUSIC_PATH,musicPath);
        spEditor.putString(id + AllData.MUSIC_PATH,picturePath);
        spEditor.putBoolean(id + AllData.SHAKE,shake);
        spEditor.putString(id + AllData.LABLE,lable);
        spEditor.putInt(id + AllData.SOUND_LONG,soundLong);
        spEditor.putInt(id + AllData.RESOUND_CISHU,resoundCishu);
        spEditor.putInt(id + AllData.RESOUND_INTERVAL,resoundInterval);
        spEditor.commit();
    }
    public void delete(){
        spEditor.remove(id + "id");
        spEditor.remove(id + AllData.TIME);
        spEditor.remove(id + AllData.OPEN);
        spEditor.remove(id + AllData.NAME);
        for(int i = 0; i < 7; i++) {
            spEditor.remove(id + AllData.REPEAT + i);
        }
        spEditor.remove(id + AllData.MUSIC_PATH);
        spEditor.remove(id + AllData.PICTURE_PATH);
        spEditor.remove(id + AllData.SHAKE);
        spEditor.remove(id + AllData.LABLE);
        spEditor.remove(id + AllData.SOUND_LONG);
        spEditor.remove(id + AllData.RESOUND_CISHU);
        spEditor.remove(id + AllData.RESOUND_INTERVAL);
        spEditor.commit();
    }

    /**
     * 静态的方法，直接放入某个闹钟的某个值
     * @param context
     * @param id 闹钟的id
     * @param key sp的key
     * @param value sp的value
     */
    public static void update(Context context, int id, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("naozhong", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putBoolean(id+key,value);
        spEditor.commit();
    }
}
