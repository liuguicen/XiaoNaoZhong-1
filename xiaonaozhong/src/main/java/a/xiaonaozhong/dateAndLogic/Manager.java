package a.xiaonaozhong.dateAndLogic;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/2/26.
 * 用来管理ListView所对应列表的数据
 */
public abstract class Manager {
    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    /**
     * 存储listview对应数据的list<map...
     * 格式map
     * id ...
     * time ...
     * open ....
     */
    List<Map<String, Object>> dataList;
    /**
     * SharedPreferences
     * 放入3种量，count，maxId，
     * 第i个值对应的的id id+i
     */
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    Context context;

    /**
     * 保存位置对应的id
     */
   public abstract void  save();

    /**
     * 插入数据，增添一个数据
     */
    public abstract void  insert(int id);

    /**
     * 删除position对应的数据
     *
     * @param position
     */
    public abstract void delete(int position);

    int findInsertPosition(long time) {
        if (dataList.size() == 0)
            return 0;
        int i = 0;
        for (Map<String, Object> map : dataList) {
            if (time < (Long) map.get("time")) {
                return i;
            }
            i++;
        }
        return dataList.size();
    }

    /**
     * 更新第position位置的项目的数据
     *
     * @param position
     */
    public abstract void updata(int position, int id);

    /**
     * 获取第positon位置个的id
     *
     * @param position
     * @return
     */
    public int getIdByPosition(int position) {
        return (Integer) dataList.get(position).get("id");
    }

    /**
     * 获取所有需要显示的数据
     */
    public abstract void selecteSimpleInfo();
}
