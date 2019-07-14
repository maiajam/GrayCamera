package com.maiajam.camera2app.Helper;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements Runnable{

    Context context ;
    Boolean mRunnable ;
    private Thread mGameThread;

    public CameraSurfaceView(Context context) {
        super(context);
        this.context = context ;
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        HelperMethods.DrawFingersShapre(context,getHolder());
    }

    @Override
    public void run() {

        Canvas canvas ;
        while (mRunnable)
        {
            if(getHolder().getSurface().isValid())
            {
                 canvas = getHolder().lockCanvas();
                 canvas.save();

            }
        }

    }

    public void pause() {
        mRunnable = false;
        try {
            // Stop the thread (rejoin the main thread)
            mGameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        mRunnable = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }


}
