package a.xiaonaozhong.systemService;

import a.xiaonaozhong.ui.RingNaozhongActivity;
import a.xiaonaozhong.utils.P;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import a.xiaonaozhong.R;

public class StartNaozhongService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		P.le("StartNaozhongService.onStartCommand():"+"NaozhongService开始执行");
		Intent acIntent = new Intent(getBaseContext(),
				RingNaozhongActivity.class);
		acIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		acIntent.putExtras(intent.getExtras());//将传入的intent的所有数据，传到acintent之中
		startActivity(acIntent);
		P.le("StartNaozhongService.onStartCommand():"+"NaozhongService执行结束，已发送Activity");
		//stopSelf();
		/*Notification notification = new Notification(R.mipmap.icon,
				"wf update service is running",
				System.currentTimeMillis());
		PendingIntent pintent= PendingIntent.getService(this, 0, intent, 0);
		notification.setLatestEventInfo(this, "WF Update Service",
				"wf update service is running！", pintent);

		//让该service前台运行，避免手机休眠时系统自动杀掉该服务
		//如果 id 为 0 ，那么状态栏的 notification 将不会显示。
		startForeground(startId, notification);*/
		return 1;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
