package a.xiaonaozhong.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import a.xiaonaozhong.R;
import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.dateAndLogic.Tixing;
import a.xiaonaozhong.dateAndLogic.TixingManager;
import a.xiaonaozhong.utils.ClickUtil;
import a.xiaonaozhong.utils.P;

public class SetTixingActivity extends AppCompatActivity implements
        TxingSetFragment.FragmentInteraction,
        TixingNaozhongFragment.AddNaozhongInteraction {
    /**
     * 数据 Activity等
     */
    TixingManager tm;
    Boolean hasChanged = false;
    Boolean startOpenState;
    int txPosition = -1;
    Tixing tx;

    /**
     * 确保一开始就获取了提醒
     */
    String musicPath;
    Boolean open;
    Intent intent;
    String lastShortcutName;


    /**
     * 提醒在原GridView中的位置
     */
    Bundle intentBundle;
    /**
     * 组件
     */
    Button btnOpen;
    TextView nameView;
    ViewPager viewPager;
    Button btnReset;
    Button btnShortcut;
    int fraId;
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    /**
     * 快捷方式
     */
    ShortcutUtil shortcutUtil;
    shortCutManager shortCutManager = new shortCutManager();

    private Intent intentInShortcut;
    private ManagerDate md = new ManagerDate();
    private boolean isShortcutStart = false;
    private boolean isHome=true;

    private void initData() {
        // 数据
        intent = getIntent();
        intentBundle = intent.getExtras();

        /**
         * 提醒和提醒manager的数据
         */
        tm = TixingManager.getInstance(this);
        txPosition = intentBundle.getInt("txPosition");
        int txId;
        if (txPosition == -1) txId = tm.getNextId();
            //针对于图标方式进来的
        else if (txPosition == -2) {
            isShortcutStart = true;
            txId = intentBundle.getInt("txId");
            txPosition = tm.getPositionByid(txId);
        } else txId = tm.positionList.get(txPosition);
        tx = new Tixing(this, txId, txPosition == -1 ? true
                : false);

        shortcutUtil = new ShortcutUtil(this);
        lastShortcutName = tx.getName();
        intentInShortcut = new Intent(SetTixingActivity.this, SetTixingActivity.class);
        intentInShortcut.setAction(AllData.SHORTCUT_ACTION);
        intentInShortcut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentInShortcut.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intentInShortcut.putExtra("txPosition", -2);
        intentInShortcut.putExtra("txId", tx.getId());

        //fragment等的数据
        TxingSetFragment tixingSetFragment = new TxingSetFragment();
        tixingSetFragment.setArguments(md.setFraBundle());
        TixingNaozhongFragment tixingNaozhongFragment = new TixingNaozhongFragment();
        tixingNaozhongFragment.setAllNaozhong(tx.newList, tx.newDisTimeList);
        fragmentList.add(tixingNaozhongFragment);
        fragmentList.add(tixingSetFragment);

        setDataAndShortcut();
        P.le("SetTixingActivity.initData()" + "initData成功");
    }

    /**
     * 初始化所有组件
     */

    private void initView() {
        nameView = (TextView) findViewById(R.id.et_tixing_set_name);
        btnShortcut = (Button) findViewById(R.id.btn_send_icon);
        btnOpen = (Button) findViewById(R.id.btn_open_tixing);
        btnReset = (Button) findViewById(R.id.btn_reset_tixing);

        viewPager = (ViewPager) findViewById(R.id.viewPager_set_tixing);
    }

    /**
     * 设置一些参数和快捷方式
     */
    private void setDataAndShortcut() {
        if (txPosition == -1) {// 创建的东西
            btnShortcut.setText(AllData.ADD_ICON_TEXT);
            open = true;
        } else {
            // 不是创建的，就获取Tixing并往里面填入相应的数据
            open = tx.getOpen();
            // 这里在判断一次是否存在快捷方式
            btnShortcut
                    .setText(shortcutUtil.isShortCutExist(this, tx.getName()) ? AllData.DEL_ICON_TEXT
                            : AllData.ADD_ICON_TEXT);
            nameView.setText(tx.getName());
        }
        md.setOpenText();
        startOpenState = open;
        //btnOpen.setBackgroundColor(Color.parseColor(AllData.BTN_OPEN_COLOR));
        //btnOpen.setBackgroundColor(Color.parseColor(AllData.BTN_CLOSE_COLOR));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_tixing);
        initView();
        initData();
        setDataAndShortcut();
        setOnclick();
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        setFragment(0);
    }

    /**
     * 再对AddButton设置监听器处理监听的事件，添加快捷方式，对话框，以及按钮上的文字
     */

    private void setOnclick() {

        nameView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                hasChanged = true;
                final EditText et = new EditText(SetTixingActivity.this);
                if (tx.getName().equals(AllData.DEFLAUL_NAME + ((tx.getId()) % 10000 + 1)))
                    et.setHint("输入名称");
                else
                    et.setText(tx.getName());
                new AlertDialog.Builder(SetTixingActivity.this)
                        .setTitle("请输入名称")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        Toast.makeText(SetTixingActivity.this,
                                                et.getText().toString(),
                                                Toast.LENGTH_LONG).show();
                                        String name = et.getText().toString();
                                        nameView.setText(name);
                                        updateDate("name", name);
                                    }
                                }).setNegativeButton("取消", null).show();
            }
        });
        btnShortcut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }

                if (shortcutUtil.isShortCutExist(SetTixingActivity.this,
                        lastShortcutName)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            SetTixingActivity.this);
                    AlertDialog alertDialog = builder
                            .setTitle("删除")
                            .setMessage("是否删除桌面快捷方式")
                            .setNegativeButton("删除",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            shortcutUtil.delShortcut(
                                                    tx.getName(), intentInShortcut);
                                            btnShortcut.setText(AllData.ADD_ICON_TEXT);
                                        }
                                    }).setPositiveButton("取消", null).create();
                    alertDialog.show();
                } else {
                    shortcutUtil.creatShortCut(tx.getName(), null, intentInShortcut);
                    lastShortcutName = tx.getName();
                    btnShortcut.setText(AllData.DEL_ICON_TEXT);
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                md.resetAllNaozhong();
                ((TixingNaozhongFragment) fragmentList.get(0)).fresh();
                open = true;
                hasChanged = true;
                md.setOpenText();
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtil.isFastDoubleClick()) {
                    return;
                }
                open = !open;
                hasChanged = true;
                md.setOpenText();
               /* if (open)
                    btnOpen.setBackgroundColor(Color.parseColor(AllData.BTN_OPEN_COLOR));
                else
                    btnOpen.setBackgroundColor(Color.parseColor(AllData.BTN_CLOSE_COLOR));*/
                tx.setOpen(open);
            }
        });

        Button fragmentNaozhong = (Button) findViewById(R.id.btn_set_tixing_naozhong);
        Button fragmentSet = (Button) findViewById(R.id.btn_set_tixing_set);

        fragmentNaozhong.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 提醒内的数据已更新，切换fragment时会重启fragment的所有周期，不能填入原来的数据，要将新的数据填进去

                if (fraId == 0)
                    return;
                else setFragment(0);
            }
        });
        fragmentSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fraId == 1)
                    return;
                else setFragment(1);
            }
        });
    }

    private void setFragment(int currentId) {
        fraId = currentId;
        viewPager.setCurrentItem(fraId);
    }

    @Override
    protected void onResume() {
        isHome=true;
        md.setOpenText();
        if (shortcutUtil.isShortCutExist(this, lastShortcutName))
            btnShortcut.setText(AllData.DEL_ICON_TEXT);
        else
            btnShortcut.setText(AllData.ADD_ICON_TEXT);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (hasChanged&&isHome)
            md.saveChange(true);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (hasChanged) {
            isHome=false;
            md.saveChange(false);
        }
        else if(isShortcutStart){
            myFinish();
        }
        super.onBackPressed();

        /*//新建的时候不需要提醒是否保存，已经创建的才提示，或者根本就不需要提醒
        if (hasChanged&&txPosition!=-1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    SetTixingActivity.this);
            AlertDialog alertDialog = builder
                    .setTitle("保存")
                    .setMessage("请选择是否保存" + AllData.TIXING_NAME)
                    .setNegativeButton("保存",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    saveChange();
                                }
                            })
                    .setPositiveButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                     注意tixing可能已经保存了，所以这你都要删除
                                     deleteTixing();
                                    finish();
                                }
                            }).create();
            alertDialog.show();

        } else {

            finish();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set_tixing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_set_tixing_save:
                isHome=false;
                md.saveChange(false);
                break;
            case R.id.menu_set_tixing_delete:
                md.deleteTixing();
                break;
        }
        return false;
    }

    @Override
    public void updateDate(String key, String value) {
        hasChanged = true;
        switch (key) {
            case AllData.NAME:
                tx.setName(value);
                break;
            case AllData.MUSIC_PATH:
                tx.setMusicPath(value);
                break;
            case AllData.SHAKE:
                tx.setShake(value.equals('1') ? true : false);
                break;
            case AllData.LABLE:
                tx.setLable(value);
                break;
            case AllData.PICTURE_PATH:
                tx.setPicturePath(value);
                break;
            case "isHome":
                isHome=false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    void myFinish() {
        if (isShortcutStart) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else {
            Intent resultIntent = getIntent();
            //      resultIntent.putExtra("musicPath", musicPath);
            setResult(1, resultIntent);
        }
        finish();
        P.le("SetTixingActivity.myFinish()" + "本任务结束");
    }

    @Override
    public void NaozhongHasChanged() {
        hasChanged = true;
    }


    private class ManagerDate {
        public void updateTixing(Boolean isPause) {
            tx.save(isPause);
            tm.updateTixing(tx, txPosition);
        }

        public void addTixing(Boolean isPause) {
            tx.save(isPause);
            tm.addTixing(tx);
        }

        /**
         * 重设所有的闹钟，从现在的时间开始
         * 然后更新到TixingNaozhongFragment之中
         */
        void resetAllNaozhong() {
            Long currentTime = System.currentTimeMillis();
            for (int i = 0; i < tx.newList.size(); i++) {
                tx.newList.get(i).setTime(currentTime + tx.newDisTimeList.get(i));
            }
            ((TixingNaozhongFragment) fragmentList.get(0)).
                    setAllNaozhong(tx.newList, tx.newDisTimeList);
        }

        Bundle setFraBundle() {
            // 设置Fragment
            Bundle fragmentBundle = new Bundle();

            // 放入提醒的其它的信息
            fragmentBundle.putString(AllData.MUSIC_PATH,
                    AllData.getFileName(tx.getMusicPath()));
            fragmentBundle.putBoolean(AllData.SHAKE, tx.getShake());
            fragmentBundle.putString(AllData.LABLE, tx.getLable());
            fragmentBundle.putString(AllData.PICTURE_PATH,
                    AllData.getFileName(tx.getPicturePath()));
            fragmentBundle.putBoolean(AllData.OPEN, tx.getOpen());
            // 放入所有的闹钟
            return fragmentBundle;
        }

        /**
         * 删除提醒，同时快捷方式存在时，就删除快捷方式
         */
        private void deleteTixing() {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    SetTixingActivity.this);
            AlertDialog alertDialog = builder
                    .setTitle("删除" + AllData.TIXING_NAME)
                    .setMessage("请选择是否删除" + AllData.TIXING_NAME
                            + ":" + tx.getName())
                    .setNegativeButton("取消", null)
                    .setPositiveButton("删除",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (shortcutUtil.isShortCutExist(
                                            SetTixingActivity.this,
                                            lastShortcutName))
                                        shortcutUtil.delShortcut(
                                                lastShortcutName, intentInShortcut);
                                    if (txPosition != -1) {
                                        tx.clear();
                                        tm.deleteTixing(txPosition);
                                    }
                                    isHome=false;
                                    myFinish();
                                }
                            }).create();
            alertDialog.show();
        }

        /**
         * 保存新建的提醒或者保存修改，
         * 保存退出时必须要检测快捷方式名称改变进行处理
         * 检测不合法情况
         */

        private void saveChange(Boolean isPause) {
            shortCutManager.adptShortcutChange();
            if (txPosition == -1) {// 创建提醒
                String defName = AllData.DEFLAUL_NAME + (tx.getId() % 10000 + 1);
                if (tx.getName().equals(defName)) {
                    Toast.makeText(SetTixingActivity.this, "名称为空，使用默认名称！" + defName,
                            Toast.LENGTH_LONG).show();
                }
                md.addTixing(isPause);
                Toast.makeText(SetTixingActivity.this, AllData.TIXING_NAME + "创建成功",
                        Toast.LENGTH_SHORT).show();
            } else {// 修改提醒
                md.updateTixing(isPause);
                Toast.makeText(SetTixingActivity.this, AllData.TIXING_NAME + "修改成功",
                        Toast.LENGTH_SHORT).show();
            }
            myFinish();
        }

        public void setOpenText() {
            btnOpen.setText(open ? AllData.OPEN_TEXT : AllData.CLOSE_TEXT);
        }
    }

    class shortCutManager {
        void adptShortcutChange() {
            // 以前的快捷方式存在，并且名字发生了改变,就删除旧的，重新创建一个快捷方式
            if (shortcutUtil.isShortCutExist(
                    SetTixingActivity.this,
                    lastShortcutName)
                    && !lastShortcutName.equals(tx.getName())) {
                shortcutUtil.delShortcut(
                        lastShortcutName, intentInShortcut);
                shortcutUtil.creatShortCut(
                        tx.getName(), null, intentInShortcut);
                lastShortcutName = tx.getName();
            }
        }
    }
}