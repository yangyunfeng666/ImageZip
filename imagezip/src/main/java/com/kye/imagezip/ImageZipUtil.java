package com.kye.imagezip;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Author: yangyunfeng
 * Date: 公元2018-7-5 11:32
 * Description:this is ImageZip
 */

public class ImageZipUtil {
    static {
        System.loadLibrary("jpegbither");
        System.loadLibrary("bitherjni");
    }

    /**
     * 默认的图片质量
     */
    private static int DEFAULT_QUALITY = 30;

    /**
     * 压缩的方法
     *
     * @param bit bitmap 对象
     * @param w           宽
     * @param h           高
     * @param quality     压缩质量
     * @param fileNameBytes 输出的文件地址
     * @param optimize    压缩比例
     * @return
     */
    private static native String compressBitmap(Bitmap bit, int w, int h,
                                                int quality, byte[] fileNameBytes, boolean optimize);


    public static void compressByJNI(Bitmap bit, String fileName, boolean optimize) {
        saveBitmap(bit, DEFAULT_QUALITY, fileName, optimize);
    }

    private static void saveBitmap(Bitmap bit, int quality, String fileName, boolean optimize) {
        compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality, fileName.getBytes(), optimize);
    }

    public static void compressByQuality(Bitmap bmp, File file) {
        int options = DEFAULT_QUALITY;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compressBySize(Bitmap bmp, File file) {
        int ratio = 8;
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compressBySample(String filePath, File file) {
        int inSampleSize = 8;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2.混合终极方法（尺寸、质量、JNI压缩）
     *
     * @param image    bitmap对象
     * @param filePath 要保存的指定目录
     * @param maxSize 图片最大值
     * @param width 图片的理想宽
     * @param height 图片的理想高
     * @Description: 通过JNI图片压缩把Bitmap保存到指定目录
     */
    public static void mixCompress(Bitmap image, String filePath,int maxSize,int width,int height) {
        // 最大图片大小 1000KB
//        int maxSize = 1000;
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(image.getWidth(), image.getHeight(),width,height);
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio, image.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        // 循环判断如果压缩后图片是否大于最大值,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            quality -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        // JNI调用保存图片到SD卡 这个关键
        saveBitmap(result, quality, filePath, true);
        // 释放Bitmap
        if (result != null && !result.isRecycled()) {
            result.recycle();
            result = null;
        }
    }

    /**
     * 计算缩放比
     *
     * @param bitWidth  当前图片宽度
     * @param bitHeight 当前图片高度
     * @return
     * @Description:函数描述
     */
    public static int getRatioSize(int bitWidth, int bitHeight,int width,int height) {
        // 图片最大分辨率
        int imageHeight = 1920;
        int imageWidth = 1080;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageHeight) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageHeight;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }
}
