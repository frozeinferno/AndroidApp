package com.example.drawingapp2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

public class DrawingView extends View {

    //variables created for the canvas
    private Path drawPath;
    private Paint drawPaint;
    private Paint canvasPaint;
    private int paintColour = 0xFF660000;
    private static Canvas drawCanvas;
    private Bitmap canvasBitMap;

    //boolean created to check whether the eraser is on or off
    private boolean erase = false;

    //this is what creates the canvas where the user can draw on. this is placed directly onto the XML layout
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    //this sets up all the technicalities, like brush size and paint style
    //as well as instantiating the variables in charge of keeping track where the user's finger is
    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColour);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    //this is the method which decides the size of the canvas based on the phone
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitMap);
    }

    //this method creates the canvas
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitMap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    //this method keeps track of where the user's finger is once it makes contact with the screen
    //and this method is what updates the drawPath variable previously created based on where the user
    //moves their finger
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        float touchX = evt.getX();
        float touchY = evt.getY();

        switch(evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate(); //like a flag. marks the container as invalid, essentially resets the touch utility
                      //once the user stops touching the screen
        return true;
    }

    //this is what sets the colour of the brush once the user presses one of the colour buttons
    public void setColour(String newColour) {
        invalidate();
        paintColour = Color.parseColor(newColour);
        drawPaint.setColor(paintColour);
    }

    //this is what changes the mode to 'eraser mode' it does this by using the PorterDuff and
    //Xfermode libraries
    public void setErase(boolean isErase) {
        this.erase = isErase;

        if(erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint.setXfermode(null);
        }
    }
    //a simple get method
    public boolean getErase() {
        return erase;
    }

    //this is what wipes the screen. it is made to be static so it can be directly accessed from the
    //sShakeFeature class. this is why the method doesn't contain the initialize() flag, since it can't
    //be accessed from a static context
    public static void startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }
}

