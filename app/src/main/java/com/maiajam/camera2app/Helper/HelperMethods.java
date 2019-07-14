package com.maiajam.camera2app.Helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.View;

public class HelperMethods {



    public static Size[] getScreenSize(Context context) {

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        Size[] ScreenSizes = null;
        try {
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            if (characteristics != null) {
                ScreenSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
        }catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return ScreenSizes;
    }

    public static String[] getCameraId(Context context)
    {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String[] cameraIdList = null;

        try {
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);

        }catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return cameraIdList;   
    }


    public static void DrawFingersShapre(Context context, SurfaceHolder holder)
    {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.e("", "Cannot draw onto the canvas as it's null");
        }else {

            Size[] size = HelperMethods.getScreenSize(context);
            Paint myPaint = new Paint();
            myPaint.setColor(Color.YELLOW);
            myPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            myPaint.setAntiAlias(true);
            myPaint.setStrokeWidth(10);

            myPaint.setStyle(Paint.Style.STROKE);
            int recWidth = size[0].getWidth()/4 ;
            int recHight = size[0].getHeight() ;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

           canvas.drawRect(20, recHight/2 +100,  (recWidth+30)*2,  recHight, myPaint);
           canvas.drawRect(recWidth+30, recHight/4 + 100  ,  (recWidth+30)*2, recHight, myPaint);
           canvas.drawRect((recWidth+30)*2,100 ,  (recWidth+30)*3, recHight, myPaint);
            canvas.drawRect((recWidth+30)*3, recHight/4 + 100 ,  (recWidth+30)*4, recHight, myPaint);
            //canvas.drawRect(20, recHight - (recHight/4)*2 ,  recWidth/4, 200, myPaint);
            //canvas.drawRect(20, recHight - (recHight/4)*2 ,  recWidth/4, 200, myPaint);


            Log.d("hight",String.valueOf(recHight));
            Log.d("hight",String.valueOf(recWidth));

            holder.unlockCanvasAndPost(canvas);
        }

    }
}
