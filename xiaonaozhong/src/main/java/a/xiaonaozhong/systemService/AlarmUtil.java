package a.xiaonaozhong.systemService;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import a.xiaonaozhong.utils.P;
import a.xiaonaozhong.dateAndLogic.SimpleNaozhong;
import a.xiaonaozhong.dateAndLogic.Tixing;

public class AlarmUtil {
    Context context;
    String continueTime;
    public AlarmUtil(Context context) {
        this.context = context;
    }

    /**
     * 启动一个处理闹钟的service
     *
     * @param tx 在intent之中写入要传给showNaozhongActivity显示和处理的数据
     * @param sn 用时间区分是哪一个闹钟 ，不能重复
     */
    public void setNaozhong(Tixing tx, SimpleNaozhong sn) {
        Intent intent = new Intent(context, StartNaozhongService.class);
        intent.putExtra("name", tx.getName());
        intent.putExtra("lable", tx.getLable());
        intent.putExtra("time", sn.getTime());
        intent.putExtra("picturePath", tx.getPicturePath());
        intent.putExtra("musicPath", tx.getMusicPath());
        intent.putExtra("shake", tx.getShake());

        PendingIntent pendingIntent = PendingIntent.getService(context,
                sn.getId() + tx.getId() * 500, intent, 0);

        // 此处加判断是否有相同的时间
        AlarmManager am = (AlarmManager) context
                .getSystemService(Service.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, Long.valueOf(sn.getTime()),
                pendingIntent);
        P.le("AlarmUtil.setNaozhong():AlarmManager设置时间成功",sn.getTime());
    }

    /**
     * 用时间区分是哪一个闹钟，不能重复
     *
     * @param tx 闹钟所属的提醒
     * @param sn 要设定的闹钟
     */
    public void cancelNaozhong(Tixing tx, SimpleNaozhong sn) {
        Intent intent = new Intent(context, StartNaozhongService.class);
        intent.putExtra("name", tx.getName());
        intent.putExtra("lable", tx.getLable());
        intent.putExtra("time", sn.getTime());
        intent.putExtra("picturePath", tx.getPicturePath());
        intent.putExtra("musicPath", tx.getMusicPath());
        intent.putExtra("shake", tx.getShake());


        PendingIntent pendingIntent = PendingIntent.getService(context,
                sn.getId() + tx.getId() * 500, intent, 0);

        ((AlarmManager) context.getSystemService(Service.ALARM_SERVICE))
                .cancel(pendingIntent);
        P.le("AlarmUtil.cancelNaozhong(): 取消闹钟已成功","时间是"+sn.getTime());
    }
}
