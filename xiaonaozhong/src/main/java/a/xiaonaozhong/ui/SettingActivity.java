package a.xiaonaozhong.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import a.xiaonaozhong.R;
import a.xiaonaozhong.dateAndLogic.AllData;

/**
 * Created by Administrator on 2016/1/29.
 */
public class SettingActivity extends AppCompatActivity {
    String defaultMusicPath;
    String defaultPicturePath;
    Boolean defaulltShake;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    TextView musicView;
    TextView pictureView;
    WiperSwitch wiperSwitch;
    TextView guanyuView;

    void getView() {
        musicView = (TextView) findViewById(R.id.tv_setting_music);
        pictureView = (TextView) findViewById(R.id.tv_setting_picture);
        wiperSwitch = (WiperSwitch) findViewById(R.id.wiperSwitch1_setting);
        guanyuView = (TextView) findViewById(R.id.tv_setting_guanyu);
    }
    void initDate() {
        defaulltShake = AllData.getDefaultShake(this);
        defaultMusicPath = AllData.getDefaultMusicPath(this);
        defaultPicturePath = AllData.getDefaultPicturePath(this);
    }

    void setView() {
        wiperSwitch.setChecked(defaulltShake);
        musicView.setText(AllData.getFileName(defaultMusicPath));
        pictureView.setText(AllData.getFileName(defaultPicturePath));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setTitle("设置");
        toolbar.setNavigationIcon(R.mipmap.return_icon);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });

        getView();
        initDate();
        setView();
        setOnClick();
    }

    void setOnClick() {
        musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SettingActivity.this, ChoseMusicActivity.class)
                        .putExtra("musicPath", defaultMusicPath), 1);
            }
        });
        pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(SettingActivity.this,ChosePictureActivity.class),1);

            }
        });
        wiperSwitch
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        defaulltShake = isChecked;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        myFinish();
        super.onBackPressed();
    }


    void myFinish() {
        AllData.saveSettings(this, defaulltShake, defaultMusicPath, defaultPicturePath);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            defaultMusicPath = data.getStringExtra("musicPath");
            musicView.setText(AllData.getFileName(defaultMusicPath));
        } else if (requestCode == 2 && resultCode == 1) {
            defaultPicturePath = data.getStringExtra("picturePath");
            pictureView.setText(AllData.getFileName(defaultPicturePath));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
