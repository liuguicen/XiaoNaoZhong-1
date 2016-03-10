package a.xiaonaozhong.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import a.xiaonaozhong.R;
import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.dateAndLogic.Naozhong;
import a.xiaonaozhong.dateAndLogic.NaozhongManager;
import a.xiaonaozhong.systemService.AlarmUtil;
import a.xiaonaozhong.utils.P;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ShowNaozhongListFragment extends Fragment {

    Context context;
    NaozhongManager nzManager;
    /**
     * 存储listview对应数据的list map...
     * 格式map
     * id ...
     * time ...
     * open ....
     */
    List<Map<String, Object>> naozhongInfoList;
    int selectPosition;
    private ListView naozhongList;
    private NaozhongAdapter nzadAdapter;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        P.le("onAttached");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        nzManager = NaozhongManager.getInstance(context);
        naozhongInfoList = nzManager.getDataList();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_naozhong, null);
        naozhongList = (ListView) view.findViewById(R.id.list_show_naozhong);
        nzadAdapter = new NaozhongAdapter();
        naozhongList.setAdapter(nzadAdapter);
        setClick(view);
        return view;
    }

    private void setClick(View view) {
        ImageView plus = (ImageView) view.findViewById(R.id.imageView_show_nzlist_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P.le(this.getClass().getName(), "PLUS.onclick()成功");
                Intent intent = new Intent(context, SetNaozhongActivity.class);
                intent.putExtra("id", -1);
                startActivityForResult(intent, 1);
            }
        });
        naozhongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                int naozhongId = (int) naozhongInfoList.get(position).get("id");
                Intent intent = new Intent(context, SetNaozhongActivity.class);
                intent.putExtra("id", naozhongId);
                startActivityForResult(intent, 2);
            }
        });
    }

    class NaozhongAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return naozhongInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = (convertView == null ? View.inflate(context,
                    R.layout.item_show_naozhong, null) : convertView);
            TextView timeView = (TextView) view.findViewById(R.id.item_naozhong_time);

            final int mposition=position;
            final int mid = (int) (naozhongInfoList.get(mposition).get("id"));
            final WiperSwitch open = (WiperSwitch) view.findViewById(R.id.item_naozhong_shake);
            open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Naozhong.update(context, mid, AllData.OPEN, open.isChecked());
                    naozhongInfoList.get(position).put(AllData.OPEN,open.isChecked());

                    if (open.isChecked()) {
                        new AlarmUtil(context).setNaozhong(Naozhong.getNaozhong(context, mid));
                    } else {
                        new AlarmUtil(context).cancelNaozhong(Naozhong.getNaozhong(context, mid));
                    }
                }
            });
            long time=(long)(naozhongInfoList.get(position).get("time"));
            timeView.setText(String.valueOf(AllData.getFormatTime(time)));
            open.setChecked((Boolean) naozhongInfoList.get(position).get("open"));

            return view;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0)//增加，
        {
            int mid=data.getIntExtra("id", 0);
            nzManager.insert(mid);
            nzadAdapter.notifyDataSetChanged();
        } else if (resultCode == 1)//删除
        {
            nzManager.delete(selectPosition);
            nzadAdapter.notifyDataSetChanged();
        }//更改不会改变id与位置的对应关系
        else if(requestCode==2){
            nzManager.updata(selectPosition,data.getIntExtra("id",0));
            nzadAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
