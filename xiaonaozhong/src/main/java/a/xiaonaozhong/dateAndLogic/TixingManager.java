package a.xiaonaozhong.dateAndLogic;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * （1）从SharedPreference中获取所有的提醒，包括ID，name，open是否打开
 * 并将所有的提醒存到list之中，让GridView显示出来
 * （2）其中tixing的id会一直增加，删除时id不减少，一直增加下去
 * （3）提醒数目和信息在应用运行过程中动态的变化，这些信息会写入list之中，同时写入sharedPreference之中
 * 显示和操作时用的信息是从内存list中获取的
 *
 * @author acm_lgc
 */
public class TixingManager {
    private Context context;
    /**
     * 第position位置对应的ID
     */
    public List<Integer> positionList = new ArrayList<Integer>();
    public List<String> nameList = new ArrayList<String>();
    public List<Boolean> openList = new ArrayList<Boolean>();
    /**
     * 存储信息的格式是
     * count->count
     * lastId->lastId
     * position:+位置+txId或者txName或者txOpen->对应的数据
     * postion从0开始，和list保持一致
     */
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    /**
     * 就是当前的提醒的数目，
     */
    int count = 0;
    /**
     * 最后一个闹钟的ID，会存入系统内存的
     */
    int lastId = 0;
    private static TixingManager tm;

    /**
     * ???????????????????????
     * ??????????????????????
     *
     * @param context
     */
    private TixingManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("txManager",
                context.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();

        count = sharedPreferences.getInt("count", 0);
        lastId = sharedPreferences.getInt("lastId", -1);
        getAll();
    }

    public static TixingManager getInstance(Context context) {
        if (tm == null) tm = new TixingManager(context);
        return tm;
    }

    /**
     * ?????????????????????
     */
    public void getAll() {
        for (int i = 0; i < count; i++) {
            if (sharedPreferences.contains("position:" + i + ":txId")) {
                positionList.add(sharedPreferences.getInt("position:" + i
                        + ":txId", 0));
                nameList.add(sharedPreferences.getString("position:" + i
                        + ":txName", "????"));
                openList.add(sharedPreferences.getBoolean("position:" + i
                        + ":txOpen", false));
            }
        }
    }

    /**
     * 将tixing存入sharedpreference中，count和lastId都加1，
     *
     * @param tx
     */
    public void addTixing(Tixing tx) {
        //
        count++;
        lastId++;
        spEditor.putInt("lastId", lastId);
        spEditor.putInt("count", count);

        positionList.add(tx.getId());

        nameList.add(tx.getName());

        openList.add(tx.getOpen());

        int tposition = count - 1;
        spEditor.putInt("position:" + tposition + ":txId", tx.getId());
        spEditor.putString("position:" + tposition + ":txName", tx.getName());
        spEditor.putBoolean("position:" + tposition + ":txOpen", tx.getOpen());

        spEditor.commit();
    }

    /**
     * 提醒的id不会变
     *
     * @param tx
     * @param position 提醒在内部list，也是提醒listVIew中的位置
     */
    public void updateTixing(Tixing tx, int position) {
        //提醒的id不会变,更新name和open
        nameList.remove(position);
        openList.remove(position);

        nameList.add(position, tx.getName());
        openList.add(position, tx.getOpen());

        // ?????SD????
        spEditor.putString("position:" + position + ":txName", tx.getName());
        spEditor.putBoolean("position:" + position + ":txOpen", tx.getOpen());
        spEditor.commit();
    }

    /**
     * 删除时先删除对应的项，相应的position消失了，
     * 然后为了position的连续，以后能查找数据，要将所有的数据前移一位
     * 在内存list中自动前移，但在sharedpreference中要手动前移
     *
     * @param position ?????????
     */
    public void deleteTixing(int position) {

        // ???tixingManager????????
        // ??????????
        positionList.remove(position);
        nameList.remove(position);
        openList.remove(position);
        // ??sd???????
        for (; position < count; position++) {
            spEditor.putInt(
                    "position:" + position + ":txId",
                    sharedPreferences.getInt("position:" + position + 1
                            + ":txId", 0));
            spEditor.putString(
                    "position:" + position + ":txName",
                    sharedPreferences.getString("position:" + position + 1
                            + ":txName", "????"));
            spEditor.putBoolean(
                    "position:" + position + ":txOpen",
                    sharedPreferences.getBoolean("position:" + position + 1
                            + ":txOpen", false));
        }
        count--;
        spEditor.putInt("count", count);
        spEditor.commit();
    }

    public void test() {
        // getAll();
        deleteTixing(0);
    }

    /**
     * 根据提醒的ID获取其在tixinglist中的位置，用于以前发送的图标，list变化之后，找到对应的position
     * 这里一定能找到，因为删除的id，图标一定会删除
     * 这里可见ID数据库的思想，一个项目的ID号一般是不能变的，因为数据会经过各种变化，这是变化之后识别他的唯一方法
     * @return
     */
    public int getPositionByid(int id) throws NoSuchElementException{
        for(int i=0;i<positionList.size();i++){
            if(positionList.get(i)==id)
                return i;
        }
        throw new NoSuchElementException("没有数据");
    }
    public int getCount() {
        return count;
    }

    /**
     * 获取下一个即将新建的提醒的ID
     *
     * @return
     */
    public int getNextId() {
        return lastId + 1;
    }
}
