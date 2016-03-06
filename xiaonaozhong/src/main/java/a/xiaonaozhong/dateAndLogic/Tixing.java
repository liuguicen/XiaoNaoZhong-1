package a.xiaonaozhong.dateAndLogic;

import java.util.ArrayList;
import java.util.List;

import a.xiaonaozhong.systemService.AlarmUtil;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 提醒的各个设置一定是有值的
 */
public class Tixing {
    /**
     *
     */
    int id;
    /**
     * 闹钟的数目
     */
    private int count = 0;
    /**
     * 存储闹钟信息的sharedPreferences
     */
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    String name;
    String musicPath;
    Boolean shake;
    String lable;
    String picturePath;
    Context context;

    Boolean open;
    /**
     * 将所有的闹钟存放到一个List之中
     */
    public List<SimpleNaozhong> naozhongList = new ArrayList<SimpleNaozhong>();
    /**
     * 这里有两个list，注意两个的保持一致性
     */
    public List<SimpleNaozhong> newList = new ArrayList<SimpleNaozhong>();
    /**
     * 将所有的间隔时间存放到一个List之中
     */
    public List<Long> disTimeList = new ArrayList<Long>();
    /**
     * 所有的新的间隔时间存放到一个List之中
     */
    public List<Long> newDisTimeList = new ArrayList<Long>();

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;

    }

    /**
     *
     * @return boolean 是否震动
     */
    public Boolean getShake() {
        return shake;
    }

    public void setShake(Boolean shake) {
        this.shake = shake;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;

    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    /**
     * 每个tixing自带一个sharedPreference 创建提醒，每次创建会到sd卡中去寻找数据，如果没有数据则会返回一个默认的值
     * 会获取所有的闹钟，并放入list和newlist中
     *
     * @param context
     * @param id       新建提醒的ID号，和位置不是同一个概念
     * @param isCreate 是否是添加一个提醒
     */
    public Tixing(Context context, int id, Boolean isCreate) {

        this.context = context;

        sharedPreferences = context.getSharedPreferences("tixing:" + id,
                context.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();

        if (isCreate) {
            setId(id);
            setName(AllData.DEFLAUL_NAME + (id % 10000 + 1));
            setLable(AllData.DEFAUL_LABLE);
            setMusicPath(AllData.getDefaultMusicPath(context));

            setPicturePath(AllData.getDefaultPicturePath(context));
            setShake(AllData.getDefaultShake(context));
            setOpen(AllData.DEFAUL_OPEN);
            setCount(count);
        } else {// 从sharedPreference里面获取
            setId(id);
            setName(sharedPreferences.getString(AllData.NAME,
                    AllData.DEFLAUL_NAME));

            setMusicPath(sharedPreferences.getString(AllData.MUSIC_PATH,
                    AllData.getDefaultMusicPath(context)));
            setShake(sharedPreferences.getBoolean(AllData.SHAKE,
                    AllData.getDefaultShake(context)));

            setLable(sharedPreferences.getString(AllData.LABLE,
                    AllData.DEFAUL_LABLE));
            setPicturePath(sharedPreferences.getString(AllData.PICTURE_PATH,
                    AllData.getDefaultPicturePath(context)));

            setOpen(sharedPreferences.getBoolean(AllData.OPEN,
                    AllData.DEFAUL_OPEN));

            setCount(sharedPreferences.getInt("count", count));
            selectAllNaozhong();//
            selectAllDis();
        }
    }

    private void setCount(int count) {
        this.count = count;
    }

    /**
     * 按时间先后顺序将闹钟插入到List之中
     *
     * @param
     */
    public void insertNaozhong(SimpleNaozhong sn) {
        if (newList.size() == 0)
            newList.add(sn);
        else {
            int i = 0;
            for (; i < newList.size(); i++) {
                if (sn.time < newList.get(i).time) {
                    newList.add(i, sn);
                    break;
                }
            }
            if (i >= newList.size())
                newList.add(sn);
        }
    }

    /**
     * 按时间先后顺序将时间间隔插入到List之中
     *
     * @param
     */
    public void insertDis(Long dis) {
        if (newDisTimeList.size() == 0)
            newDisTimeList.add(dis);
        else {
            int i = 0;
            for (; i < newDisTimeList.size(); i++) {
                if (dis < newDisTimeList.get(i)) {
                    newDisTimeList.add(i, dis);
                    break;
                }
            }
            if (i >= newDisTimeList.size())
                newDisTimeList.add(dis);
        }
    }

    public void deleteNaozhong(SimpleNaozhong sn) {
        newList.remove(sn.id);
    }

    /**
     * 删除指定序号的闹钟
     *
     * @param position 在list中即ListView中的序号
     */
    public void deleteNaozhong(int position) {
        newList.remove(position);
    }

    /**
     * 删除指定序号的时间间隔
     *
     * @param position 在list中即ListView中的序号
     */
    public void deleteDis(int position) {
        newDisTimeList.remove(position);
    }

    private void selectAllNaozhong() {
        for (int i = 0; i < count; i++) {
            SimpleNaozhong sn = new SimpleNaozhong();
            sn.setId(i);
            long time = sharedPreferences.getLong("naozhong:" + i + ":time",
                    -1l);
            sn.setTime(time);
            naozhongList.add(sn);
            newList.add(sn);
        }
    }

    private void selectAllDis() {
        for (int i = 0; i < count; i++) {
            long dis = sharedPreferences.getLong("naozhong:" + i + ":dis",
                    -1l);
            disTimeList.add(dis);
            newDisTimeList.add(dis);
        }
    }

    /**
     * 重新保存提醒的所有配置
     * 删除oldlist中所有闹钟，再重新保存newlist中所有闹钟一次
     * 并且设定所有的闹钟
     * 只要设定了定时器，那么就保存所有的设置
     * 重新设定所有的闹钟，
     *
     * @param isPause 控制只保存数据，不更改闹钟
     */
    public void save(Boolean isPause) {
        spEditor.putString(AllData.NAME, name);
        spEditor.putBoolean(AllData.SHAKE, shake);
        spEditor.putString(AllData.MUSIC_PATH, musicPath);
        spEditor.putString(AllData.LABLE, lable);
        spEditor.putString(AllData.PICTURE_PATH, picturePath);
        spEditor.putBoolean(AllData.OPEN, open);
        spEditor.putInt(AllData.COUNT, newList.size());

        AlarmUtil alarmUtil = new AlarmUtil(context);
        for (int i = 0; i < naozhongList.size(); i++) {
            SimpleNaozhong sn = naozhongList.get(i);
            spEditor.remove("naozhong:" + i + ":time");
            spEditor.remove("naozhong:" + i + ":dis");
            if (!isPause)
                alarmUtil.cancelNaozhong(this, sn);
        }
        for (int i = 0; i < newList.size(); i++) {
            SimpleNaozhong sn = newList.get(i);
            sn.setId(i);
            Long time = sn.getTime();
            spEditor.putLong("naozhong:" + i + ":time", time);
            spEditor.putLong("naozhong:" + i + ":dis", newDisTimeList.get(i));
            if (open && !isPause)
                alarmUtil.setNaozhong(this, sn);
        }
        spEditor.commit();
    }

    /**
     * 将sd卡中的所有数据，配置以及所有闹钟都删除
     * 并且取消所有的闹钟
     */
    public void clear() {
        // 将所有闹钟取消
        AlarmUtil alarmUtil = new AlarmUtil(context);
        for (SimpleNaozhong sn : naozhongList) {
            alarmUtil.cancelNaozhong(this, sn);
        }
        spEditor.clear();
        spEditor.commit();
    }

    public static void test(Context context) {
        // 测试，增加和删除基本的数据
        Tixing tx = new Tixing(context, 0, true);
        tx.setCount(0);
        tx.setLable("第二个");
        tx.setMusicPath("音乐路径");
        tx.setName("第二个");
        tx.setOpen(true);
//        tx.insertNaozhong(new SimpleNaozhong(0, 0, 11));
//        tx.insertNaozhong(new SimpleNaozhong(0, 0, 12));
//        tx.insertNaozhong(new SimpleNaozhong(0, 0, 13));
        tx.deleteNaozhong(2);
        tx.save(false);
    }
}
