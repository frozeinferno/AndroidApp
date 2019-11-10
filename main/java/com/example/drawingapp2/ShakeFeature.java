package com.example.drawingapp2;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ShakeFeature extends Service implements SensorEventListener {

    //I create variables to hold the current acceleration and previous acceleration of the phone
    //to keep track of it at all times
    private float xAccel, yAccel, zAccel;
    private float xPreviousAccel, yPreviousAccel, zPreviousAccel;

    //these are created to dictate when the phone has begun to accelerate
    private boolean firstUpdate = true;
    private boolean shakeInitiated = false;
    private float shakeThreshold = 15f; //this is a variable created to set a minimum speed at which the shake action is triggered

    //these are the two sensor variables needed to create the accelerometer
    private Sensor accelerometer ;
    private SensorManager sm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //inside the onCreate() method it is necessary to initialize the sensors as well as the sensorManager
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent evt) {
        //this method checks whether the phone has been shaken
        //within the method it constantly updates the acceleration parameters
        updateAccelParameters(evt.values[0], evt.values[1], evt.values[2]);

        if((!shakeInitiated) && isAccelerationChanged()) {
            shakeInitiated = true;
        } else if((shakeInitiated) && isAccelerationChanged()) {
            executeShakeAction();
        } else if((shakeInitiated) && !isAccelerationChanged()) {
            shakeInitiated = false;
        }
    }

    private void executeShakeAction() {
        //this is the code that will be run once the phone has been shaken enough for it to be detected
        Vibrator vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final long pattern[] = {0, 500};
        vibrate.vibrate(pattern, -1);

        //this displays a toast at the bottom of the user's device to increase usability
        DrawingView.startNew();
        Toast startNew = Toast.makeText(getApplicationContext(), "Touch screen to start new drawing!", Toast.LENGTH_SHORT);
        startNew.show();
    }

    private boolean isAccelerationChanged() {
        //this method checks whether the acceleration has gone above the shake threshold in at least 1 axis

        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);

        return (deltaX > shakeThreshold) || (deltaY > shakeThreshold) || (deltaZ > shakeThreshold);
    }

    private void updateAccelParameters(float xNewAccel, float yNewAccel, float zNewAccel) {
        /*
        this essentially is a loop which is always checking and updating the speed of the phone
        this is done by making the previous acceleration equal to the current acceleration, which
         itself is made equal to the new acceleration
         */
        if(firstUpdate) {
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        } else {
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }

        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }
}
