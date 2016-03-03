package a.xiaonaozhong.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import a.xiaonaozhong.R;
import a.xiaonaozhong.dateAndLogic.TixingManager;
import a.xiaonaozhong.utils.BitmapUtil;
import a.xiaonaozhong.utils.ClickUtil;
import a.xiaonaozhong.utils.P;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ShowTixingListFragment  extends Fragment{
    TixingManager tm;
    Activity context;
    GridView tixingGridView;
    TixinAdapter tixingAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        P.le("ShowTixingListFragment onCreate()","oncreat() tm创建被调用");
        context=getActivity();
        tm = TixingManager.getInstance(context);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_show_tixing_list,null);
        tixingGridView = (GridView) view.findViewById(R.id.grid_view_tingxing);
        tixingAdapter = new TixinAdapter();
        tixingGridView.setAdapter(tixingAdapter);
        setClick();
        P.le("ShowTixingListFragment onCreateView()", "onCreateView() tm创建被调用");
        return view;
    }
    private void setClick() {
        tixingGridView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (ClickUtil.isFastDoubleClick()) {
                            return;
                        }
                        if (position == tm.positionList.size()) {
                            Intent intent = new Intent(context,
                                    SetTixingActivity.class);
                            intent.putExtra("txPosition", -1);
                            P.le("MainActivity.btnAddTixing.setOnClickListener():正在启动创建启动SetTixingACtivity");
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(context,
                                    SetTixingActivity.class);
                            intent.putExtra("txPosition", position);
                            startActivityForResult(intent, 2);
                            P.le("启动SetTixingACtivity成功");
                        }
                    }
                });
    }

    class TixinAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tm.positionList.size() + 1;
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

            View item = convertView != null ? convertView : View.inflate(
                    context, R.layout.item_show_tixing_list, null);

            // 设置图片
            ImageView icon = (ImageView) item
                    .findViewById(R.id.image_tixing_icon);

            TextView nameText = (TextView) item
                    .findViewById(R.id.text_tixing_item_name);
            ;
            Bitmap iconBitmap;
            String text;
            float alpha;
            if (position < tm.positionList.size()) {
                iconBitmap = new BitmapUtil(context).getIconInApp(null, tm.nameList.get(position),
                        tm.openList.get(position));
                text = tm.nameList.get(position);
                alpha = tm.openList.get(position) ? 1f : 0.5f;
            } else {
                iconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.add_tixing);
                text = " ";
                alpha = 0f;
            }
            icon.setImageBitmap(iconBitmap);
            nameText.setText(text);
            nameText.setAlpha(alpha);
            return item;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0)
            return;// 就是返回或者取消了
        tixingAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
