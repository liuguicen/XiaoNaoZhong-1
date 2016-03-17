package a.xiaonaozhong.ui;

import a.xiaonaozhong.R;

import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.dateAndLogic.SimpleNaozhong;
import a.xiaonaozhong.dateAndLogic.Tixing;
import a.xiaonaozhong.systemService.AlarmUtil;
import a.xiaonaozhong.utils.ClickUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int TAB_CONUT = 2;
    private ViewPager viewPager;
    TextView tabTixing, tabNaozhong;
    Toolbar toolbar;
    ShowTixingListFragment showTxFragment;
    ShowNaozhongListFragment showNzFragment;
    RelativeLayout relativeLayout;

    public void test() {
        AlarmUtil alarmUtil = new AlarmUtil(this);
        alarmUtil.setNaozhong(new Tixing(this, 0, true),
                new SimpleNaozhong(0, 0, System.currentTimeMillis() + 1000 * 5));
        //alarmUtil.cancelNaozhong(new Tixing(this,0,true),
        //new SimpleNaozhong(0,0,System.currentTimeMillis()+1000*5));
    }

    public void init() {
        if(AllData.NAOZHONG_NUMBER>0)
            startService(new Intent(AllData.BACK_SERVICE));
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon);//设置Navigation 图
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        tabTixing = (TextView) findViewById(R.id.tab_tixing);
        tabNaozhong = (TextView) findViewById(R.id.tab_naozhong);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        showTxFragment = new ShowTixingListFragment();
        showNzFragment = new ShowNaozhongListFragment();
        tabTixing.setTextColor(Color.WHITE);
        tabNaozhong.setTextColor(Color.BLACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getActionBar().show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        viewPager.setAdapter(new MyFPAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tabTixing.setTextColor(Color.WHITE);
                        tabNaozhong.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        relativeLayout.setBackgroundResource(R.mipmap.back_b);
                        tabTixing.setTextColor(Color.BLACK);
                        tabNaozhong.setTextColor(Color.WHITE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabTixing.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(0);
                    }
                }
        );
        tabNaozhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        // setClick();
        //test();
        //isRestartNaozhong("addActivity");
    }

    class MyFPAdapter extends FragmentPagerAdapter {

        public MyFPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_CONUT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return showTxFragment;
                case 1:
                    return showNzFragment;
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!ClickUtil.isEffectExitClick()) {
            Toast.makeText(this, "再按一次我就退出", Toast.LENGTH_LONG).show();
            return;
        }
        super.onBackPressed();
    }
}