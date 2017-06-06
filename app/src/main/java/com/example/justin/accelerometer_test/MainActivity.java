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

    //variables
    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration;


    String data;
//    DateFormat time = new SimpleDateFormat("HH:mm");
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
    Date currentLocalTime = cal.getTime();
    DateFormat date = new SimpleDateFormat("HH:mm:ss a");
    // you can get seconds by adding  "...:ss" to it
    String localTime = date.format(currentLocalTime);



    //methods
    String path =
            Environment.getExternalStorageDirectory() + File.separator  + "Accel_Data";
    // Create the folder.
    File folder = new File(path);

    // Create the file.
    File file = new File(folder, "Data.txt");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        acceleration = (TextView)findViewById(R.id.acceleration);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

       int i=0;
//
//        for (i = 0; i < 4; i++)
//            if (i != 4) {
        float valueX, valueY, valueZ;
        double x,y,z;
        valueX = event.values[0];
        valueY = event.values[1];
        valueZ = event.values[2];

        x = Double.parseDouble(String.valueOf(valueX));
        y = Double.parseDouble(String.valueOf(valueY));
        z = Double.parseDouble(String.valueOf(valueZ));


        acceleration.setText("X: " + x +
                "\nY: " + y +"\nZ: " + z);
        data = (localTime + "," + x +
                        "," + y + "," + z + "," + "\n" );
        i++;
        writeData();



        //if statement for delay in writeData

//        if(i == 10){
//
//                    writeData();
//                    i = 0;
//                }

//                 writeData();
//                i = 0;
//            } else {
//                acceleration.setText("X: " + event.values[0] +
//                        "\nY: " + event.values[1] +
//                        "\nZ: " + event.values[2]);
//                data = (localTime + "," + event.values[0] +
//                        "," + event.values[1] +
//                        "," + event.values[2]);
//            }
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void writeToFile(String data)
    {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DCIM + "/Accel_Data/"
                        );

        if(!path.exists())
        {
            path.mkdirs();
        }

        File file = new File(path, "Data.csv");
        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

           // fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void Pedometer(){

    }

}