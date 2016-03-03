package a.xiaonaozhong.ui;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.dateAndLogic.SimpleNaozhong;
import a.xiaonaozhong.utils.ClickUtil;
import a.xiaonaozhong.utils.P;
import a.xiaonaozhong.R;

public class TixingNaozhongFragment extends android.support.v4.app.Fragment {

    private AddNaozhongInteraction listenner;


    /**
     * 当FRagmen被加载到activity的时候会被回调
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AddNaozhongInteraction) {
            listenner = (AddNaozhongInteraction) activity;
        } else {
            throw new IllegalArgumentException(
                    "activity must implements AddNaozhongInteraction");
        }
    }

    /**
     * 定义了所有activity必须实现的接口
     */
    public interface AddNaozhongInteraction {
        /**
         * Fragment 向Activity传递指令，这个方法可以根据需求来定义
         *
         * @param
         */
        void NaozhongHasChanged();
    }

    Activity context;

    private List<SimpleNaozhong> snList;
    private List<Long> disList;
    ImageView addNaozhong;

    NaozhongAdapter naozhongAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        P.le("TiXingNaozhongFragment" + "TiXingNaozhongFragment的onCreate成功");
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tixing_naozhong,
                container, false);

        ListView naozhongList = (ListView) view
                .findViewById(R.id.lv_tixing_naozhong);

        naozhongAdapter = new NaozhongAdapter();
        naozhongList.setAdapter(naozhongAdapter);
        P.le("Naozhong的List执行相应成功");

        addNaozhong = (ImageView) view
                .findViewById(R.id.btn_tixing_add_naozhong);

        setOnclick();
        return view;
    }

    private void setOnclick() {
        addNaozhong.setOnClickListener(new OnClickListener() {

            class MyTimePickerDialog extends TimePickerDialog {
                public MyTimePickerDialog(Context context,
                                          OnTimeSetListener listener, int hourOfDay, int minute,
                                          boolean is24HourView) {
                    super(context, listener, hourOfDay, minute, is24HourView);
                }

                @Override
                protected void onStop() {
                    // TODO Auto-generated method stub
                    // super.onStop();
                }
            }

            @Override
            public void onClick(View source) {
                P.le("TixingNaozhongFragment. 添加闹钟的点击成功");
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                TimeZone.setDefault(TimeZone.getTimeZone(AllData.TIME_ZONE));
                final Calendar c = Calendar.getInstance();
                // 创建一个TimePickerDialog实例，并把它显示出来。
                MyTimePickerDialog mtp = new MyTimePickerDialog(context,
                        new MyTimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int hourOfDay,
                                                  int minute) {
                                P.le("添加闹钟的时间选择器启动成功");
                                listenner.NaozhongHasChanged();
                                Long dis = Long.valueOf(hourOfDay * 3600 * 1000 + minute * 60 * 1000);
                                long ctime=System.currentTimeMillis()/60000*60000;
                                SimpleNaozhong sn = new SimpleNaozhong(-1, -1,
                                        ctime + dis);
                                sn.setId(snList.size());
                                if (snList.size() == 0) {
                                    snList.add(sn);
                                    disList.add(dis);
                                } else {
                                    int i = 0;
                                    for (; i < disList.size(); i++) {
                                        if (dis < disList.get(i)) {
                                            disList.add(i, dis);
                                            snList.add(i, sn);
                                            break;
                                        }
                                    }
                                    if (i >= snList.size()) {
                                        disList.add(dis);
                                        snList.add(sn);
                                    }
                                }
                                naozhongAdapter.notifyDataSetChanged();
                            }
                        }
                        , 0, 0
                        , true);/// 设置初始时间/ true表示采用24小时制
                mtp.setTitle("请设置间隔时间");
                mtp.show();
            }
        });
    }

    public void setAllNaozhong(List<SimpleNaozhong> lsn, List<Long> dsn) {
        snList = lsn;
        disList = dsn;
    }

    public void fresh() {
        naozhongAdapter.notifyDataSetChanged();
    }

    private class NaozhongAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO 自动生成的方法存根
            return snList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO 自动生成的方法存根
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO 自动生成的方法存根
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final SimpleNaozhong sn = snList.get(position);
            LayoutInflater li = LayoutInflater.from(context);
            View view = convertView == null ? li.inflate(
                    R.layout.item_tixing_naozhong_list, parent, false)
                    : convertView;
            TextView tixingNaozhongDis = (TextView) view
                    .findViewById(R.id.tv_tixing_naozhong_dis);
            TextView tixingNaozhongTime = (TextView) view
                    .findViewById(R.id.tv_tixing_naozhong_time);
            Button deltxingNaozhong = (Button) view
                    .findViewById(R.id.btn_del_tixing_naozhong);
            tixingNaozhongDis.setText(AllData.getFormatDis(disList.get(position)));
            tixingNaozhongTime.setText(sn.getFormatTime());

            deltxingNaozhong.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    snList.remove(position);
                    disList.remove(position);
                    listenner.NaozhongHasChanged();
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
