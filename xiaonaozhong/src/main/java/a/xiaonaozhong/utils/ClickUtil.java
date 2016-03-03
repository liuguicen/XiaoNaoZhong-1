package a.xiaonaozhong.utils;

import android.widget.Toast;

/**
 * 检测快速双击的类，机器反应有一定时间，在响应之前多次点机会形成多次事件
 * 用这个类处理较好
 * 也有直接对组件setClickble（false),然后setClickble（ture)的，这样麻烦一点，测试时不怎么灵
 * @author acm_lgc
 *
 */
public class ClickUtil {
	private static long lastClickTime = 0;
	private static long lastExitClick = 0;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {//设定双击间隔的时间
			return true;
		}
		lastClickTime = time;
		return false;
	}
	public static boolean isEffectExitClick() {
		long time = System.currentTimeMillis();

		long timeD = time - lastExitClick;
		if (0 < timeD && timeD < 3000) {//设定双击间隔的时间
			return true;
		}
		lastExitClick = time;
		return false;
	}
}
