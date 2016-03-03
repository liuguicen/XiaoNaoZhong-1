package a.xiaonaozhong.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import a.xiaonaozhong.R;
import a.xiaonaozhong.dateAndLogic.NaozhongManager;
import a.xiaonaozhong.utils.P;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ShowNaozhongListFragment extends Fragment {

    Context context;
    NaozhongManager nzManager;
    List<Map<String,Object>> naozhongInfoList;


    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
        P.le("onAttached");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        nzManager=NaozhongManager.getInstance(context);
        naozhongInfoList =nzManager.getDataList();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_show_naozhong,null);
        ListView naozhongList= (ListView) view.findViewById(R.id.list_show_naozhong);
        naozhongList.setAdapter(new NaozhongAdapter());
        setClick(view);
        return view;
    }

    private void setClick(View view) {
        ImageView plus= (ImageView) view.findViewById(R.id.imageView_show_nzlist_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                P.le(this.getClass().getName(),"PLUS.onclick()成功");
                Intent intent = new Intent(context,SetNaozhongActivity.class);
                intent.putExtra("position",-1);
                startActivityForResult(intent,1);
            }
        });
    }
    class NaozhongAdapter extends BaseAdapter{

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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=convertView==null?convertView.inflate(context,
                    R.layout.item_show_naozhong,parent):convertView;
            TextView time= (TextView) view.findViewById(R.id.item_naozhong_time);
            WiperSwitch shake= (WiperSwitch) view.findViewById(R.id.item_naozhong_shake);
            time.setText((String)naozhongInfoList.get(position).get("time"));
            shake.setChecked((Boolean)naozhongInfoList.get(position).get("shake"));
            return view;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0)//增加
        {
            nzManager.insert(data.getIntExtra("id",0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
