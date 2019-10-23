package com.example.print3.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BitmapTools {

    /**
     * 缩放图片 Zoom picture
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return Bitmap
     */
    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        }
        int width=bm.getWidth ();
        int height=bm.getHeight ();
        float scaleWidth=((float) newWidth) / width;
        float scaleHeight=((float) newHeight) / height;
        Matrix matrix=new Matrix ();
        matrix.postScale (scaleWidth, scaleHeight);
        Bitmap newbm=Bitmap.createBitmap (bm, 0, 0, width, height, matrix, true);
        if (bm != null & !bm.isRecycled ()) {
            bm.recycle ();// recycle bitmap
            bm=null;
        }
        return newbm;
    }


    /**
     * convert to black image
     * @param bmp
     * @return
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width=bmp.getWidth ();
        int height=bmp.getHeight ();
        int[] pixels=new int[width * height];
        byte b;
        bmp.getPixels (pixels, 0, width, 0, 0, width, height);
        int alpha=0xFF << 24;
        for (int i=0; i < height; i++) {
            for (int j=0; j < width; j++) {
                int grey=pixels[width * i + j];

                int red=((grey & 0x00FF0000) >> 16);
                int green=((grey & 0x0000FF00) >> 8);
                int blue=(grey & 0x000000FF);

                grey=(int) (red * 0.3 + green * 0.59 + blue * 0.11);
                //grey = alpha | (grey << 16) | (grey << 8) | grey;
                if (grey < 128) {
                    b=1;
                } else {
                    b=0;
                }
                //pixels[width * i + j] = grey;
                pixels[width * i + j]=b;
            }
        }
        Bitmap newBmp=Bitmap.createBitmap (width, height, Config.RGB_565);
        newBmp.setPixels (pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     *
     * Name bitmapGray
     * @param bmSrc
     * @return
     */
    public static Bitmap bitmapGray(Bitmap bmSrc) {
        // 得到图像的宽度和长度. Get the width and length of the image.
        int width=bmSrc.getWidth ();
        int height=bmSrc.getHeight ();
        // 创建线性拉升灰度图像. Create a linear pull-up grayscale image.
        Bitmap bmpGray=null;
        bmpGray=Bitmap.createBitmap (width, height, Config.RGB_565);
        // 创建画布 create Canvas
        Canvas c=new Canvas (bmpGray);
        Paint paint=new Paint ();
        ColorMatrix cm=new ColorMatrix ();
        cm.setSaturation (0);
        ColorMatrixColorFilter f=new ColorMatrixColorFilter (cm);
        paint.setColorFilter (f);
        c.drawBitmap (bmSrc, 0, 0, paint);
        return bmpGray;
    }

    /**
     *
     * Name lineGrey
     * @param image
     * @return
     */
    public static Bitmap lineGrey(Bitmap image) {
        // 得到图像的宽度和长度. Get the width and length of the image.
        int width=image.getWidth ();
        int height=image.getHeight ();
        // 创建线性拉升灰度图像. Create a linear pull-up grayscale image.
        Bitmap lineGray=null;
        lineGray=image.copy (Config.ARGB_8888, true);
        // 依次循环对图像的像素进行处理. Loop through the pixels of the image in turn.
        for (int i=0; i < width; i++) {
            for (int j=0; j < height; j++) {
                // 得到每点的像素值. Get the pixel value of each point
                int col=image.getPixel (i, j);
                int alpha=col & 0xFF000000;
                int red=(col & 0x00FF0000) >> 16;
                int green=(col & 0x0000FF00) >> 8;
                int blue=(col & 0x000000FF);
                // 增加了图像的亮度. Increased brightness of the image
                red=(int) (1.1 * red + 30);
                green=(int) (1.1 * green + 30);
                blue=(int) (1.1 * blue + 30);
                // 对图像像素越界进行处理. Processing image pixels out of bounds.
                if (red >= 255) {
                    red=255;
                }
                if (green >= 255) {
                    green=255;
                }
                if (blue >= 255) {
                    blue=255;
                }
                // 新的ARGB. New ARGB.
                int newColor=alpha | (red << 16) | (green << 8) | blue;
                // 设置新图像的RGB值. Set the RGB value of the new image.
                lineGray.setPixel (i, j, newColor);
            }
        }
        return lineGray;
    }

    /**
     *
     * Name grayBinary
     * @param bitmap
     * @return
     */
    public static Bitmap grayBinary(Bitmap bitmap) {
        int width=bitmap.getWidth ();
        int height=bitmap.getHeight ();
        Bitmap binarymap=null;
        binarymap=bitmap.copy (Config.ARGB_8888, true);
        for (int i=0; i < width; i++) {
            for (int j=0; j < height; j++) {
                int col=binarymap.getPixel (i, j);
                int alpha=col & 0xFF000000;
                int red=(col & 0x00FF0000) >> 16;
                int green=(col & 0x0000FF00) >> 8;
                int blue=(col & 0x000000FF);
                int gray=(int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                if (gray <= 95) {
                    gray=0;
                } else {
                    gray=255;
                }
                int newColor=alpha | (gray << 16) | (gray << 8) | gray;
                binarymap.setPixel (i, j, newColor);
            }
        }
        return binarymap;
    }

    /**
     *
     * 转黑白图 Turn black and white
     * @param bmp 原图 bitmap
     * @param w   转换后的宽 convert to width
     * @param h   转换后的高 convert to height
     * @return
     */
    public static Bitmap convertToBMW(Bitmap bmp, int w, int h, int tmp) {
        int width=bmp.getWidth (); // 获取位图的宽. Get the width of the bitmap.
        int height=bmp.getHeight (); // 获取位图的高. Get the height of the bitmap.
        int[] pixels=new int[width * height]; // 通过位图的大小创建像素点数组. Create an array of pixels by the size of the bitmap.
        // 设定二值化的域值，默认值为100. Set the binarized field value. The default value is 100.
        //tmp = 180;
        bmp.getPixels (pixels, 0, width, 0, 0, width, height);
        int alpha=0xFF << 24;
        for (int i=0; i < height; i++) {
            for (int j=0; j < width; j++) {
                int grey=pixels[width * i + j];
                // 分离三原色 Separating the three primary colors.
                alpha=((grey & 0xFF000000) >> 24);
                int red=((grey & 0x00FF0000) >> 16);
                int green=((grey & 0x0000FF00) >> 8);
                int blue=(grey & 0x000000FF);
                if (red > tmp) {
                    red=255;
                } else {
                    red=0;
                }
                if (blue > tmp) {
                    blue=255;
                } else {
                    blue=0;
                }
                if (green > tmp) {
                    green=255;
                } else {
                    green=0;
                }
                pixels[width * i + j]=alpha << 24 | red << 16 | green << 8
                        | blue;
                if (pixels[width * i + j] == -1) {
                    pixels[width * i + j]=-1;
                } else {
                    pixels[width * i + j]=-16777216;
                }
            }
        }
        // 新建图片 create new bitmap.
        Bitmap newBmp=Bitmap.createBitmap (width, height, Config.ARGB_8888);
        // 设置图片数据 setting bitmap data.
        newBmp.setPixels (pixels, 0, width, 0, 0, width, height);
        // Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, w, h);
        return newBmp;
    }

    /**
     *
     * 转黑白图 Turn black and white
     * @param img
     * @return
     */
    public static Bitmap binarization(Bitmap img) {
        int width=img.getWidth (); // 获取位图的宽.  Get the width of the bitmap.
        int height=img.getHeight (); // 获取位图的高. Get the height of the bitmap.
        int area=width * height;
        int gray[][]=new int[width][height];
        int average=0;
        int graysum=0;
        int graymean=0;
        int grayfrontmean=0;
        int graybackmean=0;
        int pixelGray;
        int front=0;
        int back=0;
        int[] pix=new int[width * height];
        img.getPixels (pix, 0, width, 0, 0, width, height);
        for (int i=1; i < width; i++) {
            for (int j=1; j < height; j++) {
                int x=j * width + i;
                int r=(pix[x] >> 16) & 0xff;
                int g=(pix[x] >> 8) & 0xff;
                int b=pix[x] & 0xff;
                pixelGray=(int) (0.3 * r + 0.59 * g + 0.11 * b);
                gray[i][j]=(pixelGray << 16) + (pixelGray << 8) + (pixelGray);
                graysum+=pixelGray;
            }
        }
        graymean=(int) (graysum / area);
        average=graymean;
        //Log.i(TAG,"Average:"+average);
        for (int i=0; i < width; i++) {
            for (int j=0; j < height; j++) {
                if (((gray[i][j]) & (0x0000ff)) < graymean) {
                    graybackmean+=((gray[i][j]) & (0x0000ff));
                    back++;
                } else {
                    grayfrontmean+=((gray[i][j]) & (0x0000ff));
                    front++;
                }
            }
        }
        int front_value=(int) (grayfrontmean / front);
        int back_value=(int) (graybackmean / back);
        float G[]=new float[front_value - back_value + 1];
        int s=0;
        //Log.i(TAG,"Front:"+front+"**Frontvalue:"+frontvalue+"**Backvalue:"+backvalue);
        for (int i1=back_value; i1 < front_value + 1; i1++) {
            back=0;
            front=0;
            grayfrontmean=0;
            graybackmean=0;
            for (int i=0; i < width; i++) {
                for (int j=0; j < height; j++) {
                    if (((gray[i][j]) & (0x0000ff)) < (i1 + 1)) {
                        graybackmean+=((gray[i][j]) & (0x0000ff));
                        back++;
                    } else {
                        grayfrontmean+=((gray[i][j]) & (0x0000ff));
                        front++;
                    }
                }
            }
            grayfrontmean=(int) (grayfrontmean / front);
            graybackmean=(int) (graybackmean / back);
            G[s]=(((float) back / area) * (graybackmean - average)
                    * (graybackmean - average) + ((float) front / area)
                    * (grayfrontmean - average) * (grayfrontmean - average));
            s++;
        }
        float max=G[0];
        int index=0;
        for (int i=1; i < front_value - back_value + 1; i++) {
            if (max < G[i]) {
                max=G[i];
                index=i;
            }
        }
        for (int i=0; i < width; i++) {
            for (int j=0; j < height; j++) {
                int in=j * width + i;
                if (((gray[i][j]) & (0x0000ff)) < (index + back_value)) {
                    pix[in]=Color.rgb (0, 0, 0);
                } else {
                    pix[in]=Color.rgb (255, 255, 255);
                }
            }
        }
        Bitmap temp=Bitmap.createBitmap (width, height, Config.ARGB_8888);
        temp.setPixels (pix, 0, width, 0, 0, width, height);
        return temp;
        //image.setImageBitmap(temp);
    }



    /**
     * bitmap转byte[] bitmap convert byte[]
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2PrinterBytes(Bitmap bitmap) {
        int width=bitmap.getWidth ();
        int height=bitmap.getHeight ();
        //Log.v("hello", "height?:"+height);
        int startX=0;
        int startY=0;
        int offset=0;
        int scanSize=width;
        int writeNo=0;
        int rgb=0;
        int colorValue=0;
        int[] rgbArray=new int[offset + (height - startY) * scanSize + (width - startX)];
        bitmap.getPixels (rgbArray, offset, scanSize, startX, startY, width, height);

        int iCount=(height % 8);
        if (iCount > 0) {
            iCount=(height / 8) + 1;
        } else {
            iCount=(height / 8);
        }
        byte[] mData=new byte[iCount * width];
        //Log.v("hello", "myiCount?:"+iCoun t);
        for (int l=0; l <= iCount - 1; l++) {
            //Log.v("hello", "iCount?:"+l);
            //Log.d("hello", "l?:"+l);
            for (int i=0; i < width; i++) {
                int rowBegin=l * 8;
                //Log.v("hello", "width?:"+i);
                int tmpValue=0;
                int leftPos=7;
                int newheight=((l + 1) * 8 - 1);
                //Log.v("hello", "newheight?:"+newheight);
                for (int j=rowBegin; j <= newheight; j++) {
                    //Log.v("hello", "width?:"+i+"  rowBegin?:"+j);
                    if (j >= height) {
                        colorValue=0;
                    } else {
                        rgb=rgbArray[offset + (j - startY) * scanSize + (i - startX)];
                        if (rgb == -1) {
                            colorValue=0;
                        } else {
                            colorValue=1;
                        }
                    }
                    //Log.d("hello", "rgbArray?:"+(offset + (j - startY)
                    //		* scansize + (i - startX)));
                    //Log.d("hello", "colorValue?:"+colorValue);
                    tmpValue=(tmpValue + (colorValue << leftPos));
                    leftPos=leftPos - 1;
                }
                mData[writeNo]=(byte) tmpValue;
                writeNo++;
            }
        }
        return mData;
    }


    /**
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height=options.outHeight;
        final int width=options.outWidth;
        int inSampleSize=1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width,计算出实际宽高和目标宽高的比率
            final int heightRatio=Math.round ((float) height / (float) reqHeight);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高一定都会大于等于目标的宽和高。
            // Select the minimum ratio of width and height as the value of inSampleSize, which will ensure that the width and height of the final image will be greater than or equal to the width and height of the target.
            final int widthRatio=Math.round ((float) width / (float) reqWidth);
            inSampleSize=heightRatio < widthRatio ? heightRatio : widthRatio;
            // Anything more than 2x the requested pixels we'll sample down
            final float totalPixels=width * height;
            // further
            final float totalReqPixelsCap=reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    /**
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // The first parse sets inJustDecodeBounds to true to get the size of the image
        final BitmapFactory.Options options=new BitmapFactory.Options ();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource (res, resId, options);
        // Call the method defined above to calculate the inSampleSize value
        options.inSampleSize=calculateInSampleSize (options, reqWidth, reqHeight);
        LogUtils.d ("inSampleSize", options.inSampleSize + "");
        // The image is parsed again using the obtained inSampleSize value
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource (res, resId, options);
    }


    /**
     *
     * 质量压缩
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream ();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中。 Quality compression method, where 100 means no compression, and the compressed data is stored in baos.
        image.compress (Bitmap.CompressFormat.JPEG, 100, baos);
        int options=100;
        while (baos.toByteArray ().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩。Loop judges if the compressed image is greater than 100kb, greater than continuous compression
            baos.reset ();// 重置baos即清空baos。Here compress options% and store the compressed data in baos
            image.compress (Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中。Here compress options% and store the compressed data in baos
            options-=10;// 每次都减少10。Decrease 10 each time
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中。Store the compressed data baos in a ByteArrayInputStream
        ByteArrayInputStream isBm=new ByteArrayInputStream (baos.toByteArray ());
        Bitmap bitmap=BitmapFactory.decodeStream (isBm, null, null); // 生成图片 create image
        return bitmap;
    }


    /**
     * @param bm
     * @param orientationDegree 旋转角度
     * @return
     */
    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m=new Matrix ();
        m.setRotate (orientationDegree, (float) bm.getWidth () / 2, (float) bm.getHeight () / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX=bm.getHeight ();
            targetY=0;
        } else {
            targetX=bm.getHeight ();
            targetY=bm.getWidth ();
        }

        final float[] values=new float[9];
        m.getValues (values);

        float x1=values[Matrix.MTRANS_X];
        float y1=values[Matrix.MTRANS_Y];

        m.postTranslate (targetX - x1, targetY - y1);

        Bitmap bm1=Bitmap.createBitmap (bm.getHeight (), bm.getWidth (), Config.ARGB_8888);

        Paint paint=new Paint ();
        Canvas canvas=new Canvas (bm1);
        canvas.drawBitmap (bm, m, paint);
        return bm1;
    }


    /**
     *
     * rotatedPicture 旋转图片
     * @param bitmap
     * @param rotation
     * @Return
     */
    public static Bitmap rotatedPicture(Bitmap bitmap, int rotation) {
        Matrix matrix=new Matrix ();
        matrix.postRotate (rotation);
        return Bitmap.createBitmap (bitmap, 0, 0, bitmap.getWidth (), bitmap.getHeight (), matrix, false);
    }

    /**
     *
     * mirrorFlipImage 镜像翻转图片.
     * @param bitmap
     * @Return
     */
    public static Bitmap mirrorFlipImage(Bitmap bitmap) {
        Matrix matrix=new Matrix ();
        matrix.setScale (-1, 1);
        matrix.postTranslate (bitmap.getWidth (), 0);
        return Bitmap.createBitmap (bitmap, 0, 0, bitmap.getWidth (),
                bitmap.getHeight (), matrix, false);
    }

    /**
     * Name splitImage
     * @param rawBitmap
     * @param row       切成几行. Cut into several lines.
     * @param column    切成几列. Cut into several columns.
     * @return
     */
    private ArrayList<Bitmap> splitImage(Bitmap rawBitmap, int row, int column) {
        ArrayList<Bitmap> partImagesArrayList=new ArrayList<Bitmap> (row * column);
        int rawBitmapWidth=rawBitmap.getWidth ();
        int rawBitmapHeight=rawBitmap.getHeight ();
        int perPartWidth=rawBitmapWidth / column;
        int perPartHeight=rawBitmapHeight / row;
        Bitmap perBitmap=null;
        for (int i=0; i < row; i++) {
            for (int j=0; j < column; j++) {
                int x=j * perPartWidth;
                int y=i * perPartHeight;
                Log.e ("splitImage", "splitImage: "+"i=" + i + ",j=" + j + ",x=" + x + ",y=" + y);
                perBitmap=Bitmap.createBitmap (rawBitmap, x, y, perPartWidth, perPartHeight);
                partImagesArrayList.add (perBitmap);
            }
        }
        return partImagesArrayList;
    }
}
