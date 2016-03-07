package a.xiaonaozhong.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

import a.xiaonaozhong.R;
import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.dateAndLogic.Naozhong;
import a.xiaonaozhong.dateAndLogic.NaozhongManager;
import a.xiaonaozhong.utils.P;

/**
 * Created by Administrator on 2016/2/27.
 */
public class SetNaozhongActivity extends AppCompatActivity {
    Boolean hasChanged = false;
    long time = 0;
    Calendar calendar;
    private Naozhong naozhong;


    private EditText nameView;
    private TextView repeatView;
    private TextView alarmView;
    private TextView resoundView;
    private EditText lableView;
    private WiperSwitch shakeView;
    private TimePicker timePicker;
    private TextView deleteView;
    private Toolbar toolbar;


    String name;
    boolean[] repeat;
    String alarm;
    String lable;
    boolean shake;
    String resound;
    int resoundInterval;
    int resoundCishu;
    private String repeatString;


    public void getView() {
        toolbar= (Toolbar) findViewById(R.id.set_naozhong_toolbar);
        timePicker = (TimePicker) findViewById(R.id.set_naozhong_time_picker);
        timePicker.setIs24HourView(true);
        nameView = (EditText) findViewById(R.id.set_naozhong_name_content);
        repeatView = (TextView) findViewById(R.id.set_naozhong_repeat_content);
        alarmView = (TextView) findViewById(R.id.set_naozhong_alarm_content);
        resoundView = (TextView) findViewById(R.id.set_naozhong_resound_content);
        lableView = (EditText) findViewById(R.id.set_naozhong_lable_content);
        shakeView = (WiperSwitch) findViewById(R.id.set_naozhong_shake_content);
        deleteView = (TextView) findViewById(R.id.set_naozhong_delete);
    }

    public void setViewData() {
        toolbar.setNavigationIcon(R.mipmap.cancel);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        calendar.setTimeInMillis(time);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

        nameView.setText(name);
        StringBuilder sb = new StringBuilder();

        boolean allday = true;
        for (int i = 0; i < 7; i++) {
            if (repeat[i]) sb.append(AllData.XINGQI[i] + " ");
            else allday = false;
        }
        if (allday) repeatString = "每天";
        else
            repeatString = sb.toString();
        repeatView.setText(repeatString);
        alarmView.setText(AllData.getFileName(alarm));
        shakeView.setChecked(shake);
        lableView.setText(lable);

        resound = resoundCishu + "次," + "每次间隔" + resoundInterval + "分钟";
        resoundView.setText(resound);
    }

    void getData() {
        Intent intent = getIntent();
        int naoZhongId = intent.getIntExtra("id", 0);
        if (naoZhongId == -1) {
            naozhong = Naozhong.createNaozhong(this);
        }
        else {
            naozhong = Naozhong.getNaozhong(this, naoZhongId);
            time = naozhong.getTime();
        }

        repeat = naozhong.getRepeat();
        time=naozhong.getTime();
        alarm = naozhong.getMusicPath();
        shake = naozhong.getShake();
        lable = naozhong.getLable();
        resoundCishu = naozhong.getResoundCishu();
        resoundInterval = naozhong.getResoundInterval();
        calendar=Calendar.getInstance();
    }

    void init() {
        getData();
        getView();
        setViewData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        P.le(this.getClass().getName(), "has inter");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_naozhong);
        init();
        setClick();
        P.le(this.getClass().getName(), "has start");
    }
    void setClick(){

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetNaozhongActivity.this.finish();
            }
        });

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(AllData.TIME_ZONE));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });

        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRepeat();
            }

            private void setRepeat() {

            }

        });
        alarmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent(SetNaozhongActivity.this,
                        ChoseMusicActivity.class);
                intent.putExtra(AllData.MUSIC_PATH, naozhong.getMusicPath());
                startActivityForResult(intent, 0);
            }
        });

        resoundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResound();
            }

            private void setResound() {

            }
        });
        time = calendar.getTimeInMillis();

        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naozhong.delete();
                setResult(1);
            }
        });
    }
    void save(){
        naozhong.setTime(time);
        naozhong.setName(name);
        naozhong.setRepeat(repeat);
        naozhong.setMusicPath(alarm);
        naozhong.setShake(shake);
        naozhong.setLable(lable);
        naozhong.setResoundCishu(resoundCishu);
        naozhong.setResoundInterval(resoundInterval);
        naozhong.save();
        setResult(0,new Intent().putExtra("id",naozhong.getId()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_naozhong,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.set_naozhong_menu_item_tick:
            save();
            break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==1){//返回了音乐
            alarm=data.getStringExtra(AllData.MUSIC_PATH);
            alarmView.setText(AllData.getFileName(alarm));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


