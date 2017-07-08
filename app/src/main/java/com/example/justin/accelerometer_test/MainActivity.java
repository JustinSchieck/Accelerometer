package com.example.justin.accelerometer_test;

        import android.hardware.*;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.TextView;
        import java.io.*;
        import java.text.*;
        import java.util.*;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Private Variables
    private Sensor accelerometer;
    private SensorManager sm;
    private TextView acceleration;
    private String data;
//    //    DateFormat time = new SimpleDateFormat("HH:mm");
//    Calendar cal = Calendar.getInstance();
//
//    SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
//    Date currentLocalTime = new Date();
//    // you can get seconds by adding  "...:ss" to it
//    String localTime = date.format(currentLocalTime);



    int i = 0;


    //Android onCreate Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        acceleration = (TextView)findViewById(R.id.acceleration);
    }

    //Android Sensor Change Method, Runs Every time the sensor Changes accuracy
    @Override
    public void onSensorChanged(SensorEvent event) {

        float valueX, valueY, valueZ;

        valueX = event.values[0];
        valueY = event.values[1];
        valueZ = event.values[2];
//        long timestamp = System.currentTimeMillis();

        acceleration.setText("X: " + valueX +
                "\nY: " + valueY + "\nZ: " + valueZ);

        Calendar c = Calendar.getInstance();
        int Seconds = c.get(Calendar.SECOND);
        int Minutes =  c.get(Calendar.MINUTE);
        int Hours = c.get(Calendar.HOUR);

        String Sec = String.valueOf(Seconds);
        String Min = String.valueOf(Minutes);
        String Hour = String.valueOf(Hours);

        String localTime = Hour + ":" + Min + ":" + Sec;

        data = (localTime + "," + valueX +
                "," + valueY + "," + valueZ + "," + "\n");

        //if statement for delay in writeData

        if (i != 10) {
            i++;
        } else {
            writeData();
            i = 0;
        }
    }

       public void writeData(){
        try {
            writeToFile(data);
        }catch(Exception e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    //Writes the String to the File
    public void writeToFile(String data) {
        //Finds proper Directory with available permissions, If use anything
        //Other then DCIM, Android will error out
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DCIM + "/Accel_Data/"
                        );

        //If file path does not exist, create it
        if (!path.exists()) {
            path.mkdirs();
        }

        //creates the new file for the data
        File file = new File(path, "Accel_Data.csv");
        //if file isn't there, creates one
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //tries to write
        try {
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
//            fOut.write(data);
            fOut.flush();
            fOut.close();
            //catches if data was unable to write
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }



}