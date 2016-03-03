package a.xiaonaozhong.ui;

import java.util.ArrayList;
import java.util.List;


import a.xiaonaozhong.dateAndLogic.AllData;
import a.xiaonaozhong.systemService.GetAllMusic;
import a.xiaonaozhong.R;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

class ChoiseView extends FrameLayout implements Checkable {
    private TextView mTextView;
    private RadioButton mRadioButton;

    public ChoiseView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_music, this);
        mTextView = (TextView) findViewById(R.id.music_name);
        mRadioButton = (RadioButton) findViewById(R.id.music_checked);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    @Override
    public void setChecked(boolean checked) {
        mRadioButton.setChecked(checked);

    }

    @Override
    public boolean isChecked() {

        return mRadioButton.isChecked();
    }

    @Override
    public void toggle() {

        mRadioButton.toggle();
    }

}

/**
 * 选择音乐的Activtiy,需要传入已有的音乐路径，定位到那里，如果没有音乐可选，则返回已有的音乐路径，
 * 返回形式
 * resultIntent.putExtra("musicPath", musicPath);
 * setResult(1, resultIntent);
 * 注意已有的音乐路径如果失效，
 */
public class ChoseMusicActivity extends AppCompatActivity {
    ImageButton verifyBtn;
    ImageButton cancelBtn;
    String musicPath;
    List<String> musicPathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_music);
        verifyBtn = (ImageButton) findViewById(R.id.music_verify);
        cancelBtn = (ImageButton) findViewById(R.id.music_cancel);
        musicPath = getIntent().getStringExtra("musicPath");
        // 显示所有音乐music的ListView
        musicPathList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        //必定有一个音乐，就不用考虑为0的特殊情况啦，
        if(AllData.getDefaultMusicPath(this)=="default/defaultAlarm")
            musicPathList.add(AllData.getDefaultMusicPath(this));
        new GetAllMusic(this).getAllMusicPath(musicPathList);
        for (String s : musicPathList) {
            nameList.add("   "
                    + s.substring(s.lastIndexOf("/") + 1, s.length()));
        }
        ListView listView = (ListView) findViewById(R.id.listview_chose_music);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ListAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.item_music, nameList) {
            public View getView(int position, View convertView,
                                android.view.ViewGroup parent) {
                final ChoiseView view;
                if (convertView == null) {
                    view = new ChoiseView(ChoseMusicActivity.this);
                } else {
                    view = (ChoiseView) convertView;
                }
                view.setText(getItem(position));
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                musicPath = musicPathList.get(position);
            }
        });
        //设置已选定的music
        int i = 0;
        for (; i < musicPathList.size(); i++) {
            if (musicPath.equals(musicPathList.get(i))) {
                listView.setItemChecked(i, true);
                break;
            }
        }
        //传入的已有路径失效
        if (i >= musicPathList.size()) {
            listView.setItemChecked(0, true);
            musicPath = musicPathList.get(0);
        }
        verifyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent resultIntent = getIntent();
                resultIntent.putExtra("musicPath", musicPath);
                setResult(1, resultIntent);
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent resultIntent = getIntent();
                resultIntent.putExtra("musicPath", musicPath);
                ChoseMusicActivity.this.setResult(0, resultIntent);
                ChoseMusicActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = getIntent();
        resultIntent.putExtra("musicPath", musicPath);
        setResult(1, resultIntent);
        super.onBackPressed();
    }
}
