package a.xiaonaozhong.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.dateAndLogic.TixingManager;
import a.xiaonaozhong.utils.ClickUtil;
import a.xiaonaozhong.utils.P;
import a.xiaonaozhong.R;

public class TxingSetFragment extends android.support.v4.app.Fragment {
    /**
     * 组件
     */
    TextView musicView;
    WiperSwitch wsOpenShake;
    TextView lableView;
    TextView pictureView;
    /**
     * 组件内元素的值
     */
    String musicPath;
    Boolean shake;
    String lable;
    String picturePath;
    Boolean open;
    /**
     * 其它工具
     */
    ShortcutUtil shortcutUtil;

    /**
     * 数据
     */
    TixingManager tm;
    Activity context;

    /**
     * 用来与外部activity交互的
     */
    private FragmentInteraction listenner;

    /**
     * 当FRagmen被加载到activity的时候会被回调
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentInteraction) {
            listenner = (FragmentInteraction) activity;
        } else {
            throw new IllegalArgumentException(
                    "activity must implements FragmentInteraction");
        }
    }

    /**
     * 定义了所有activity必须实现的接口
     */
    public interface FragmentInteraction {
        /**
         * Fragment 向Activity传递指令，这个方法可以根据需求来定义
         *
         * @param
         */
        void updateDate(String key, String value);
    }

    private void initComponent(View view) {
        context = getActivity();

        tm = TixingManager.getInstance(context);

        shortcutUtil = new ShortcutUtil(context);

        musicView = (TextView) view.findViewById(R.id.edit_text_tixing_music);
        wsOpenShake = (WiperSwitch) view.findViewById(R.id.wiperSwitch1);
        pictureView = (TextView) view
                .findViewById(R.id.edit_text_tixing_picture);
        lableView = (TextView) view.findViewById(R.id.tv_tixing_lable);

        Bundle bu = getArguments();
        // 放入提醒的其它的信息
        musicPath = bu.getString(AllData.MUSIC_PATH);
        shake = bu.getBoolean(AllData.SHAKE);
        lable = bu.getString(AllData.LABLE);
        picturePath = bu.getString(AllData.PICTURE_PATH);
        open = bu.getBoolean(AllData.OPEN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        P.le("onCreate()");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tixing_set, container, false);
        initComponent(view);
        setContent();
        setClick();
        return view;
    }

    private void setContent() {
        musicView.setText(musicPath);
        wsOpenShake.setChecked(shake);
        lableView.setText(lable);
        pictureView.setText(picturePath);
        P.le("setTixngFragment", "填充View成功");
    }

    public void setClick() {
        lableView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                final EditText et = new EditText(context);
                if (lable.equals(AllData.DEFAUL_LABLE))
                    et.setHint("输入名称");
                else
                    et.setText(lable);
                et.setHint("输入标签");
                new AlertDialog.Builder(context)
                        .setTitle("标签")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        Toast.makeText(context,
                                                et.getText().toString(),
                                                Toast.LENGTH_LONG).show();
                                        lable = et.getText().toString();
                                        lableView.setText(lable);
                                        listenner.updateDate("lable", lable);
                                    }
                                }).setNegativeButton("取消", null).show();
            }
        });
        musicView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listenner.updateDate("isHome","false");
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                startActivityForResult(new Intent(context,
                        ChoseMusicActivity.class).putExtra("musicPath", musicPath), 0);
            }
        });
        wsOpenShake
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        listenner.updateDate("shake",
                                wsOpenShake.isChecked() ? "1" : "0");
                    }
                });
        pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.updateDate("isHome","false");
                if (ClickUtil.isFastDoubleClick()) {

                    return;
                }
                //startActivityForResult(new Intent(context,
                //ChoseMusicActivity.class).putExtra("picturePath",picturePath), 0);
                Toast.makeText(context, "对比起，暂不支持此功能", Toast.LENGTH_LONG)
                        .show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0)
            return;
        if (requestCode == 0 && resultCode == 1) {
            musicPath = data.getExtras().getString("musicPath");
            musicView.setText(AllData.getFileName(musicPath));// 注意只是设置了名字
            listenner.updateDate("musicPath", musicPath);
        }
        if (requestCode == 0 && resultCode == 2) {
            String picturePath = data.getExtras().getString("picturePath");
            musicView.setText(AllData.getFileName(picturePath));// 注意只是设置了名字
            listenner.updateDate("picturePath", picturePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}