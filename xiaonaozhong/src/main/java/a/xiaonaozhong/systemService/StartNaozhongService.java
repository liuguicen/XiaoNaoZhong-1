package a.xiaonaozhong.systemService;

import a.xiaonaozhong.ui.RingNaozhongActivity;
import a.xiaonaozhong.utils.P;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
		return 1;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
