package a.xiaonaozhong.ui;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.utils.MusicUtil;
import a.xiaonaozhong.R;

public class RingNaozhongActivity extends Activity {
    MusicUtil mu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

    //锁屏启动并且打开屏幕
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_ring_naozhong);
        Bundle bundle =getIntent().getExtras();
        TextView tvTime = (TextView) findViewById(R.id.tv_show_naozhong_time);
        TextView tvName = (TextView) findViewById(R.id.tv_show_naozhong_name);
        TextView tvLable = (TextView) findViewById(R.id.tv_show_naozhong_lable);

        String musicPath = bundle.getString("musicPath");
        Long time = bundle.getLong("time");
        String name = bundle.getString("name");
        String lable = bundle.getString("lable");

        tvTime.setText(AllData.getFormatTime(time));
        tvName.setText(name);
        tvLable.setText(lable);
        musicPath = "/storage/emulated/0" + "/往前一步-白鹤.mp3";
        try {
            mu = new MusicUtil(this);
            mu.startMusic(musicPath);
        } catch (IllegalArgumentException | SecurityException
                | IllegalStateException | IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        setClick();
    }

    public void setClick() {
        Button processNaozhong = (Button) findViewById(R.id.btn_show_naozhong_process);
        processNaozhong.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mu.stopMusic();
                finish();
            }
        });

    }
}
