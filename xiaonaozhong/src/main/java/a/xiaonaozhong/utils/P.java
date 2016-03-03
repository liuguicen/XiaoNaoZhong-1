package a.xiaonaozhong.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
/**
 * 简化代码的类，
 * 这是关于输出打印的类
 * @author acm_lgc
 *
 */
public class P {
	public static void mt(Context context,String s){
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}
	public static void le(Object s)
	{
		Log.e(s.toString(),"------");
	}
	public static void le(Object s1,Object s2)
	{
		Log.e(s1.toString(),s2.toString());
	}
	public void lgd(String s)
	{
		Log.d(s,"------");
	}
	public void lgd(String s1,String s2)
	{
		Log.d(s1,s2);
	}
	public void taost(Context context,Object s){
		Toast.makeText(context, s.toString(), Toast.LENGTH_LONG).show();
	}
}
