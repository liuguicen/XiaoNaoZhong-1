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
        selecteSimpleInfo();
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
        spEditor.putInt(AllData.COUNT, dataList.size());
        spEditor.commit();
    }

    @Override
    public void insert(int id) {
        Map<String,Object> map=new HashMap<String ,Object>();
        Naozhong naozhong= Naozhong.getNaozhong(context, id);
        map.put("id", id);
        map.put(AllData.TIME,naozhong.getTime());
        map.put(AllData.OPEN,naozhong.isOpen());
        dataList.add(findInsertPosition(naozhong.getTime()), map);
        spEditor.putInt("maxId", maxId++);
        save();
    }

    @Override
    public void delete(int position) {
        dataList.remove(position);
        save();
    }

    @Override
    public void updata(int position, int id) {
        dataList.remove(position);
        Map<String,Object> map=new HashMap<String ,Object>();
        Naozhong naozhong= Naozhong.getNaozhong(context, id);
        map.put("id", id);
        map.put(AllData.TIME, naozhong.getTime());
        map.put(AllData.OPEN, naozhong.isOpen());
        dataList.add(findInsertPosition(naozhong.getTime()), map);
        save();
    }

    @Override
    public void selecteSimpleInfo() {
        int count=sp.getInt("count", 0);//默认值为0，若没有保存过，值为0，就不用处理了
        SharedPreferences nzSp = context.getSharedPreferences("naozhong", Context.MODE_PRIVATE);
        for(int i=0;i<count;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            int nzId=sp.getInt("id"+i,0);
            map.put("id",nzId);
            map.put(AllData.TIME,nzSp.getLong(nzId+AllData.TIME,0));
            map.put(AllData.OPEN,nzSp.getBoolean(nzId+AllData.OPEN,true));
            dataList.add(map);
        }
    }
}
