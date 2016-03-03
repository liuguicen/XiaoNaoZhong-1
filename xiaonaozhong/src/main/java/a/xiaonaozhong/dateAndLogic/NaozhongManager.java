package a.xiaonaozhong.dateAndLogic;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/2/26.
 */
public class NaozhongManager extends Manager {
    private static NaozhongManager naozhongManager = null;

    public int getNextId() {
        return maxId;
    }

    int maxId=0;

    private NaozhongManager(Context context) {
        this.context = context.getApplicationContext();
        dataList = new ArrayList<Map<String, Object>>();
        sp = context.getSharedPreferences("naozhongManager",
                Context.MODE_PRIVATE);
        maxId=sp.getInt(AllData.MAX_ID, 0);
        spEditor = sp.edit();
    }

    public static NaozhongManager getInstance(Context context) {
        if (naozhongManager == null) {
            return new NaozhongManager(context);
        }
        return naozhongManager;
    }

    @Override
    public void save() {
        int i=0;
        for(Map<String,Object> map:dataList){
            spEditor.putInt("id" + i++, (Integer) map.get("id"));
        }
        spEditor.putInt("count", dataList.size());
        spEditor.commit();
    }

    @Override
    public void insert(int id) {
        Map<String,Object> map=new HashMap<String ,Object>();
        Naozhong naozhong= Naozhong.getNaozhong(context, id);
        map.put("id", id);
        map.put("time",naozhong.getTime());
        map.put("open",naozhong.isOpen());
        dataList.add(map);
        spEditor.putInt("maxId", maxId++);
        save();
    }

    @Override
    public void delete(int position) {
        dataList.remove(position);
        save();
    }

    @Override
    public void update(int position) {
    }

    @Override
    public void selecteSimpleInfo() {
        int count=sp.getInt("count",0);//默认值为0，若没有保存过，值为0，就不用处理了
        for(int i=0;i<count;i++){
            SharedPreferences naozhongSp=context.getSharedPreferences(
                    "naozhong"+i,Context.MODE_PRIVATE
            );
            Map<String,Object> map=dataList.get(i);
            map.put(AllData.TIME,naozhongSp.getLong(AllData.TIME,0));
            map.put(AllData.OPEN,naozhongSp.getBoolean(AllData.OPEN,true));
        }
    }

}
