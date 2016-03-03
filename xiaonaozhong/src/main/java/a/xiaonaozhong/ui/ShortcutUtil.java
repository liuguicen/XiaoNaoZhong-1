package a.xiaonaozhong.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toolbar;

import java.util.List;

import  a.xiaonaozhong.utils.BitmapUtil;
/**
 * Function: LauncherUtil Create date on 15/8/17.
 *
 * @version 1.0
 */
class LangchUtil {

    private static String mBufferedValue = null;

    /**
     * get the current Launcher's Package Name
     */
    public static String getCurrentLauncherPackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager()
                .resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    /**
     * default permission is "com.android.launcher.permission.READ_SETTINGS"<br/>
     * {@link #getAuthorityFromPermission(Context, String)}<br/>
     *
     * @param context context
     */
    public static String getAuthorityFromPermissionDefault(Context context) {
        if (TextUtils.isEmpty(mBufferedValue))// we get value buffered
            mBufferedValue = getAuthorityFromPermission(context,
                    "com.android.launcher.permission.READ_SETTINGS");
        return mBufferedValue;
    }

    /**
     * be cautious to use this, it will cost about 500ms
     * 姝ゅ嚱鏁颁负璐规椂鍑芥暟锛屽ぇ姒傚崰鐢?00ms宸﹀彸鐨勬椂闂?br/>
     * android绯荤粺妗岄潰鐨勫????俊鎭敱涓?釜launcher.db鐨凷qlite鏁版嵁搴撶鐞嗭紝閲岄潰鏈変笁寮犺??<br/>
     * 鍏朵腑涓?紶琛ㄥ氨鏄痜avorites銆傝繖涓猟b鏂囦欢涓?埇????湪data/data/com.android.launcher(
     * launcher2)鏂囦欢鐨刣atabases涓?br/> 浣嗘槸??逛簬涓嶅悓鐨剅om浼氭斁鍦ㄤ笉鍚岀殑鍦版柟<br/>
     * 渚嬪MIUI????湪data/data/com.miui.home/databases涓嬮??br/>
     * htc????湪data/data/com.htc.launcher/databases涓嬮??br
     * /
     *
     * @param context    context
     * @param permission 璇诲彇璁剧疆鐨勬潈闄??READ_SETTINGS_PERMISSION
     * @return permission
     */
    public static String getAuthorityFromPermission(Context context,
                                                    String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }
        try {
            List<PackageInfo> packs = context.getPackageManager()
                    .getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs == null) {
                return "";
            }
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)
                                || permission.equals(provider.writePermission)) {
                            return provider.authority;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

/**
 * 进行桌面快捷方式创建，删除，????是否存在的类 注意要填加的权限,为了能够????图标是否存在
 * <p/>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * <uses-permission
 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
 * <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS"
 * /> <uses-permission
 * android:name="com.android.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.android.launcher2.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.android.launcher2.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.android.launcher3.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.android.launcher3.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="org.adw.launcher.permission.READ_SETTINGS" />
 * <uses-permission android:name="org.adw.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="com.htc.launcher.permission.READ_SETTINGS" />
 * <uses-permission android:name="com.htc.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.qihoo360.launcher.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.qihoo360.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="com.lge.launcher.permission.READ_SETTINGS" />
 * <uses-permission android:name="com.lge.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="net.qihoo.launcher.permission.READ_SETTINGS"
 * /> <uses-permission
 * android:name="net.qihoo.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="org.adwfreak.launcher.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="org.adwfreak.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="org.adw.launcher_donut.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="org.adw.launcher_donut.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="com.huawei.launcher3.permission.READ_SETTINGS"
 * /> <uses-permission
 * android:name="com.huawei.launcher3.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="com.fede.launcher.permission.READ_SETTINGS" />
 * <uses-permission android:name="com.fede.launcher.permission.WRITE_SETTINGS"
 * /> <uses-permission
 * android:name="com.sec.android.app.twlauncher.settings.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.sec.android.app.twlauncher.settings.WRITE_SETTINGS" />
 * <uses-permission android:name="com.anddoes.launcher.permission.READ_SETTINGS"
 * /> <uses-permission
 * android:name="com.anddoes.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.tencent.qqlauncher.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.tencent.qqlauncher.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="com.huawei.launcher2.permission.READ_SETTINGS"
 * /> <uses-permission
 * android:name="com.huawei.launcher2.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.android.mylauncher.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.android.mylauncher.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.ebproductions.android.launcher.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.ebproductions.android.launcher.permission.WRITE_SETTINGS"
 * /> <uses-permission android:name="com.oppo.launcher.permission.READ_SETTINGS"
 * /> <uses-permission
 * android:name="com.oppo.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="com.miui.mihome2.permission.READ_SETTINGS" />
 * <uses-permission android:name="com.miui.mihome2.permission.WRITE_SETTINGS" />
 * <uses-permission
 * android:name="com.huawei.android.launcher.permission.READ_SETTINGS" />
 * <uses-permission
 * android:name="com.huawei.android.launcher.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="telecom.mdesk.permission.READ_SETTINGS" />
 * <uses-permission android:name="telecom.mdesk.permission.WRITE_SETTINGS" />
 * <uses-permission android:name="dianxin.permission.ACCESS_LAUNCHER_DATA" />
 * <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS"
 * /> <uses-permission
 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 * <uses-permission
 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
 *
 * @author acm_lgc
 */
public class ShortcutUtil {
    Context context;

    public ShortcutUtil(Context context) {
        this.context = context;
    }


    /**
     * 创建????快捷方式??	 *
     *
     * @param icon       此处可以为空null，使用默认图片
     *                   否则必须是用 Parcelable iconRes = Intent.ShortcutIconResource.
     *                    fromContext(this, R.drawable.ic_launcher);
     *                    或??是 用Bitmap创??的一个尺寸和桌面图标大小一样 的图片
     *
     * @param startIntent 这个intent必须要和删除intent的启动（this）与 被启动组件，action相同??	 *            且这三个元素都必须要有，才能进行删除
     * @param title       这会作为区分????应用中不同icon??
     * @see ShortcutUtil
     */
    public void creatShortCut(String title, Bitmap icon, Intent startIntent) {
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                new BitmapUtil(context).getShortCutIcon(icon, title));//使用已有图片，不用再传??
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
        context.sendBroadcast(shortcutIntent);
    }

    /**
     * 删除桌面的Shortcut
     *
     * @param title intent 只需要使用与创建时相同的即可
     */
    public void delShortcut(String title, Intent startIntent) {
        Intent delShortcut = new Intent(
                "com.android.launcher.action.UNINSTALL_SHORTCUT");
        delShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        delShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
        context.sendBroadcast(delShortcut);
    }

    /**
     * 判断快捷方式是否存在，使用名字?? 过程十分复杂，目前只使用API 不允许重复创??????使用{ isShortCutExist(Context,
     * String, Intent) } * 进行判断，因为可能有些应用生成的快捷方式名称是一样的??
     */
    public boolean isShortCutExist(Context context, String title) {
        boolean result = false;
        try {
            ContentResolver cr = context.getContentResolver();
            Uri uri = getUriFromLauncher(context);
            Cursor c = cr.query(uri, new String[]{"title"}, "title=? ",
                    new String[]{title}, null);
            if (c != null && c.getCount() > 0) {
                result = true;
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 使用名字和intent
     * 不一定所有的手机都有效，因为国内大部分手机的桌面不是系统原生??更多请参考{isShortCutExist(Context,String)}
     * 桌面有两种，系统桌面(ROM自带)与第三方桌面，一般只考虑系统自带 第三方桌面如果没有实现系统响应的方法是无法判断的，比如GO桌面
     */

    public boolean isShortCutExist(Context context, String title,
                                   Intent intent) {
        boolean result = false;
        try {
            ContentResolver cr = context.getContentResolver();
            Uri uri = getUriFromLauncher(context);
            Cursor c = cr.query(uri, new String[]{"title", "intent"},
                    "title=?  and intent=?",
                    new String[]{title, intent.toUri(0)}, null);

            if (c != null && c.getCount() > 0) {
                result = true;
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
        return result;
    }

    private static Uri getUriFromLauncher(Context context) {
        StringBuilder uriStr = new StringBuilder();
        String authority = LangchUtil
                .getAuthorityFromPermissionDefault(context);
        if (authority == null || authority.trim().equals("")) {
            authority = LangchUtil.getAuthorityFromPermission(context,
                    LangchUtil.getCurrentLauncherPackageName(context)
                            + ".permission.READ_SETTINGS");
        }
        uriStr.append("content://");
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) {
                // Android 2.1.x(API 7)以及以下??				uriStr.append("com.android.launcher.settings");
            } else if (sdkInt < 19) { // Android 4.4以下
                // uriStr.append("com.android.launcher2.settings");
            } else {// 4.4以及以上 uriStr.append("com.android.launcher3.settings");

            }
        } else {
            uriStr.append(authority);
        }
        uriStr.append("/favorites?notify=true");
        return Uri.parse(uriStr.toString());
    }

}