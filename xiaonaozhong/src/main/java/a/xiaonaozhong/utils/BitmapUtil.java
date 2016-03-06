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
    private float inTextSize;
    private float shortTextSize;
    private Paint inP;
    private int inWidth;
    private float fInWidth;
    private int shortWidth;

    private float fshortWidth;
    private Paint shortP;

    public BitmapUtil(Context context) {
        this.context = context;
        inWidth = (int) (((int) context.getResources().getDimension(
                android.R.dimen.app_icon_size)) * 1.8);
        shortWidth = (int) context.getResources().getDimension(
                android.R.dimen.app_icon_size);
        fInWidth = (float) inWidth;
        fshortWidth = (float) shortWidth;

        // 启用抗锯齿和使用设备的文本字??
        inP = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        inP.setTypeface(Typeface.SANS_SERIF);
        inP.setColor(0xffFF6633);
        inP.setTypeface(Typeface.SANS_SERIF);

        shortP = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        shortP.setTypeface(Typeface.SANS_SERIF);
        shortP.setColor(0xffFF6633);

        inTextSize = 30f;
        inP.setTextSize(inTextSize);
        float rwidth = fInWidth * 21 / 32;
        while (inP.measureText("方圆") < rwidth) {
            inTextSize += 1f;
            inP.setTextSize(inTextSize);
        }

        shortTextSize = 20f;
        shortP.setTextSize(shortTextSize);
        rwidth = fshortWidth * 6 / 8;
        while (shortP.measureText("方圆") < rwidth) {
            shortTextSize += 1f;
            shortP.setTextSize(shortTextSize);
        }
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

        Paint bitmapPaint = new Paint();//用来设置要填充的图片的效??
        bitmapPaint.setDither(true);// 防抖??
        bitmapPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果

        Bitmap bitmapCanvas = Bitmap.createBitmap(shortWidth, shortWidth,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCanvas);

        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());//原图片的大小
        Rect dst = new Rect(0, 0, shortWidth, shortWidth);//底板的大??
        canvas.drawBitmap(bitmap, src, dst, bitmapPaint);//????用于缩放

        //屏幕适配尺寸
        int len=title.length();
        if (len > 4) title = title.substring(0, 4);

        float x;
        if (len == 1)
            x = (fshortWidth) * 5 / 16;
        else x = (fshortWidth) / 8;


        Paint.FontMetrics fm = shortP.getFontMetrics();
        float add = (fm.bottom - fm.top) / 2 - fm.descent;
        if (len <= 2) {
            float y = fshortWidth / 2 + add;
            canvas.drawText(title, x, y,
                    shortP);
        } else {
            float y = fshortWidth * 9 / 32 + add;
            String s1 = title.substring(0, 2);
            String s2 = title.substring(2, len);
            canvas.drawText(s1, x, y, shortP);
            if (s2.length() == 1)
                x = fshortWidth / 2 - add + 1f / 32f;
            y = fshortWidth * 21 / 32 + add;
            canvas.drawText(s2, x, y, shortP);
        }
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


        Bitmap bitmapCanvas = Bitmap.createBitmap(inWidth, inWidth,
                Config.ARGB_8888);//位图画板// 初始化画宽度
        Canvas canvas = new Canvas(bitmapCanvas);
        if (bitmap == null)
            bitmap = BitmapFactory.decodeResource(res, R.mipmap.naozhong_icon);
        bitmap = createFramedPhoto(inWidth, inWidth, bitmap, 25);
        //设置用于缩放
        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());//原图片的大小
        Rect objRect = new Rect(0, 0, inWidth, inWidth);//底板的大??
        canvas.drawBitmap(bitmap, srcRect, objRect, bitmapPaint);


        if (!open) inP.setAlpha(120);
        if (title.length() > 4) title = title.substring(0, 4);
        int len = title.length();

        //屏幕适配尺寸
        Paint.FontMetrics fm = inP.getFontMetrics();
        float add = (fm.bottom - fm.top) / 2 - fm.descent;
        float y = fInWidth / 2 + add;
        float y1 = fInWidth * 9 / 32 + add;
        float y2 = fInWidth * 22 / 32 + add;

        float x1=0, x2 = 0, x3 = 0, x4 = 0;
        if (len == 1) {
            x1 = (fInWidth) * 11 / 32;
            canvas.drawText(title.substring(0, 1), x1, y,inP);
        } else if (len == 2) {
            x1 = (fInWidth) * 4 / 32;
            x2 = (fInWidth) * 17 / 32;
            canvas.drawText(title.substring(0, 1), x1, y, inP);
            canvas.drawText(title.substring(1, 2), x2, y, inP);
        } else if (len == 3) {
            x1 = (fInWidth) * 4 / 32;
            x2 = (fInWidth) * 17 / 32;
            x3 = (fInWidth) * 11 / 32;
            canvas.drawText(title.substring(0, 1), x1, y1, inP);
            canvas.drawText(title.substring(1, 2), x2, y1, inP);
            canvas.drawText(title.substring(2, 3), x3, y2, inP);

        } else if (len == 4) {
            x1 = (fInWidth) * 4 / 32;
            x2 = (fInWidth) * 17 / 32;
            x3 = (fInWidth) * 4 / 32;
            x4 = (fInWidth) * 17 / 32;
            canvas.drawText(title.substring(0, 1), x1, y1, inP);
            canvas.drawText(title.substring(1, 2), x2, y1, inP);
            canvas.drawText(title.substring(2, 3), x3, y2, inP);
            canvas.drawText(title.substring(3, 4), x4, y2, inP);

        }
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
