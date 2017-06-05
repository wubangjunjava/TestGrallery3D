package com.zxl.grallery3d.second;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.zxl.grallery3d.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryViewAdapter extends BaseAdapter {
    private ImageView[] mImages;        // 保存倒影图片的数组

    private Context mContext;
    public List<Map<String, Object>> list;


    public Integer[] imgs = {R.drawable.image01, R.drawable.image02, R.drawable.image03,
            R.drawable.image04, R.drawable.image05, R.drawable.image06};
    public String[] titles = {"美图01", "美图02", "美图03", "美图04", "美图05", "美图06"};

    public GalleryViewAdapter(Context c) {
        this.mContext = c;
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < imgs.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", imgs[i]);
            list.add(map);
        }
        mImages = new ImageView[list.size()];
    }

    /**
     * 反射倒影
     */
    public boolean createReflectedImages() {
        final int reflectionGap = 4;
        final int Height = 200;
        int index = 0;
        for (Map<String, Object> map : list) {
            Integer id = (Integer) map.get("image");
            Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), id);    // 获取原始图片
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            float scale = Height / (float) height;

            Matrix sMatrix = new Matrix();
            sMatrix.postScale(scale, scale);
            Bitmap miniBitmap = Bitmap.createBitmap(originalImage, 0, 0,
                    originalImage.getWidth(), originalImage.getHeight(), sMatrix, true);

            int mwidth = miniBitmap.getWidth();
            int mheight = miniBitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);            // 图片矩阵变换（从低部向顶部的倒影）
            Bitmap reflectionImage = Bitmap.createBitmap(miniBitmap, 0, mheight / 2, mwidth, mheight / 2, matrix, false);    // 截取原图下半部分
            Bitmap bitmapWithReflection = Bitmap.createBitmap(mwidth, (mheight + mheight / 2), Config.ARGB_8888);            // 创建倒影图片（高度为原图3/2）

            Canvas canvas = new Canvas(bitmapWithReflection);    // 绘制倒影图（原图 + 间距 + 倒影）
            canvas.drawBitmap(miniBitmap, 0, 0, null);        // 绘制原图
            Paint paint = new Paint();
            canvas.drawRect(0, mheight, mwidth, mheight + reflectionGap, paint);        // 绘制原图与倒影的间距
            canvas.drawBitmap(reflectionImage, 0, mheight + reflectionGap, null);    // 绘制倒影图

            paint = new Paint();
            LinearGradient shader = new LinearGradient(0, miniBitmap.getHeight(), 0, bitmapWithReflection.getHeight()
                    + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
            paint.setShader(shader);    // 线性渐变效果
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));        // 倒影遮罩效果
            canvas.drawRect(0, mheight, mwidth, bitmapWithReflection.getHeight() + reflectionGap, paint);        // 绘制倒影的阴影效果

            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(bitmapWithReflection);        // 设置倒影图片
            imageView.setLayoutParams(new Gallery.LayoutParams(203, 400)
            );
            imageView.setScaleType(ScaleType.FIT_XY);
            mImages[index++] = imageView;
        }
        return true;
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return imgs[position % imgs.length];
    }

    @Override
    public long getItemId(int position) {
        return position % imgs.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //return mImages[position];
        return mImages[position % imgs.length];
        // 显示倒影图片（当前获取焦点）
    }

    public float getScale(boolean focused, int offset) {
        return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
    }

}
