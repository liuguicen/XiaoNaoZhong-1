package a.xiaonaozhong.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import a.xiaonaozhong.R;

public class BitmapUtil {
    Context context;
    private float rwidth;

    public BitmapUtil(Context context) {
        this.context = context;
    }

    /**
     * @param bitmap 可以为null,使用默认图片
     * @param title
     * @return
     */
    public Bitmap getShortCutIcon(Bitmap bitmap, String title) {
        //获取已有的Drawable图片
        Resources res = context.getResources();
        if (bitmap == null)
            bitmap = BitmapFactory.decodeResource(res, R.mipmap.naozhong_icon);

        // 初始化画宽度
        int bitmapSize = (int) context.getResources().getDimension(
                android.R.dimen.app_icon_size);
        Bitmap bitmapCanvas = Bitmap.createBitmap(bitmapSize, bitmapSize,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCanvas);

        Paint bitmapPaint = new Paint();//画笔
        bitmapPaint.setAlpha(150);
        bitmapPaint.setDither(true);// 防抖??
        bitmapPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());//原图片的大小
        Rect dst = new Rect(0, 0, bitmapSize, bitmapSize);//底板的大??
        canvas.drawBitmap(bitmap, src, dst, bitmapPaint);//????用于缩放

        // 启用抗锯齿和使用设备的文本字??
        Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        countPaint.setColor(0xffFF6633);

        //屏幕适配尺寸
        if (title.length() > 4) title = title.substring(0, 4);
        //屏幕适配尺寸
        float textSize = 45f;
        countPaint.setTextSize(textSize);
        //rwidth=()
        if (title.length() == 1) {
            //while (countPaint.measureText(String.valueOf(title))<) {

            //}
        }
        float x, y;
        switch (10) {
            case 10:
                break;
            default:
                break;
        }
        countPaint.setTextSize(45f);
        countPaint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText(title, 2, 11 * bitmapSize / 16,
                countPaint);
        return bitmapCanvas;
    }

    /**
     * 将文字写到图片上
     *
     * @param bitmap 可以为空,默认提供的图片图片大小会变成桌面图标的大小
     * @param title
     * @return
     */
    public Bitmap getIconInApp(Bitmap bitmap, String title, Boolean open) {
        Paint bitmapPaint = new Paint();//用来设置要填充的图片的效??
        if (!open)
            bitmapPaint.setAlpha(120);
        bitmapPaint.setDither(true);// 防抖??
        bitmapPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果

        //获取已有的Drawable图片
        Resources res = context.getResources();
        int width = (int) (((int) context.getResources().getDimension(
                android.R.dimen.app_icon_size)) * 1.8);

        Bitmap bitmapCanvas = Bitmap.createBitmap(width, width,
                Config.ARGB_8888);//位图画板// 初始化画宽度
        Canvas canvas = new Canvas(bitmapCanvas);
        if (bitmap == null)
            bitmap = BitmapFactory.decodeResource(res, R.mipmap.naozhong_icon);
        bitmap = createFramedPhoto(width, width, bitmap, 25);
        //设置用于缩放
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());//原图片的大小
        Rect objRect = new Rect(0, 0, width, width);//底板的大??
        canvas.drawBitmap(bitmap, srcRect, objRect, bitmapPaint);

        // 启用抗锯齿和使用设备的文本字??
        Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        countPaint.setColor(0xffFF6633);
        if (!open) countPaint.setAlpha(120);
        if (title.length() > 4) title = title.substring(0, 4);
        //屏幕适配尺寸
        float textSize = 30f;
        countPaint.setTextSize(textSize);
        float rwidth=0f;
        switch (title.length()) {
            case 1:
                rwidth= (float) width * 3 / 5;
                break;
            case 2:
                rwidth = (float) width * 6 / 8;
                break;
            case 3:
                rwidth = (float) width * 6 / 8;
                break;
            case 4:
                rwidth = (float) width * 6 / 8;
                break;
            default:
                title=title.substring(0,4);
                rwidth = (float) width * 6 / 8;
                break;
        }
        while (countPaint.measureText(String.valueOf(title)) < rwidth) {
            textSize += 1f;
            countPaint.setTextSize(textSize);
        }
        Log.e("textSize"," " + textSize);
        countPaint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText(title, 2, 11 * width / 16,
        		countPaint);
        return bitmapCanvas;
    }

    private Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
        //根据源文件新建一个darwable对象
        Drawable imageDrawable = new BitmapDrawable(image);

        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);

        // 产生一个红色的圆角矩形
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);


        // 将源图片绘制到这个圆角矩形上
        //详解见http://lipeng88213.iteye.com/blog/1189452
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();

        return output;
    }
}
