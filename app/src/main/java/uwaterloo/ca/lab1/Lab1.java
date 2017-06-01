package uwaterloo.ca.lab1;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;

public class Lab1 extends AppCompatActivity {
    LineGraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);
        LinearLayout r1=(LinearLayout) findViewById(R.id.layout1);//Creating linear Layout
        graph=new LineGraphView(getApplicationContext(), 100, Arrays.asList("x","y","z"));//Creating lineGraphView Object
        r1.addView(graph);//Adding the linegraph to the linear layout view
        graph.setVisibility(View.VISIBLE);
        Button recordValues= new Button(getApplicationContext());//Creating a button for recordValues
        recordValues.setText("Record the Data");

        Button clearMax = new Button(getApplicationContext());//Creating a button to clear max
        clearMax.setText("Clear Historical Maximums");

        /*
        Section below is creating all the textviews for the sensor labels and reading labels to be used in the listeners
        Then the textviews are added to our linear layout r1
         */


        r1.addView(recordValues);//adding the two buttons to the layout
        r1.addView(clearMax);
        r1.setOrientation(LinearLayout.VERTICAL);//Setting the linear layout as vertical orientation
        TextView lightS=new TextView(getApplicationContext());//Creating a textView for light sensor
        r1.addView(lightS);
        lightS.setTextColor(Color.WHITE);

        TextView accelS=new TextView(getApplicationContext());//Creating the textviews for accelerometer
        r1.addView(accelS);
        accelS.setTextColor(Color.WHITE);
        TextView accelMaxs= new TextView(getApplicationContext());
        r1.addView(accelMaxs);
        accelMaxs.setTextColor(Color.WHITE);

        TextView magS=new TextView(getApplicationContext());//creating the textviews for magnetic sensor
        r1.addView(magS);
        magS.setTextColor(Color.WHITE);
        TextView magMax=new TextView(getApplicationContext());
        r1.addView(magMax);
        magMax.setTextColor(Color.WHITE);

        TextView rotS=new TextView(getApplicationContext());//creating the textviews for rotational sensor
        r1.addView(rotS);
        rotS.setTextColor(Color.WHITE);
        TextView rotMax= new TextView((getApplicationContext()));
        r1.addView(rotMax);
        rotMax.setTextColor(Color.WHITE);

        final File[] file = {null};//Creating a new file object
        final PrintWriter[] printWriter = {null};//Creating a printwriter for doc output

        /*
            Following section below creates the sensor Manager.

            Each sensor has three lines of code with the order of function
            1) Creating the sensor object through sensorManager
            2) Creating the sensorListener objects from our custom classes and assigning the textviews
            3) Registering our custom sensor listeners to the sensor in sensorManager so it knows to use ours

         */

        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);//Creating sensor manager

        Sensor lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener light=new LightSensorEventListener(lightS);
        sensorManager.registerListener(light,lightSensor,SensorManager.SENSOR_DELAY_GAME);

        Sensor accelSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        final accelSensorEventListener accel=new accelSensorEventListener(accelS,graph, accelMaxs);
        sensorManager.registerListener(accel,accelSensor,SensorManager.SENSOR_DELAY_GAME);

        Sensor magSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        final magSensorEventListener mag=new magSensorEventListener(magS,magMax);
        sensorManager.registerListener(mag,magSensor,SensorManager.SENSOR_DELAY_GAME);

        Sensor rotSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        final rotSensorEventListener rot=new rotSensorEventListener(rotS,rotMax);
        sensorManager.registerListener(rot,rotSensor,SensorManager.SENSOR_DELAY_GAME);


             /*
         Section below is used for the file output.
         The new file object is assigned a name and directory, and a print writer is created with that file.
         Try blocks used for the data printing and error catching in event of no file made.
         Finally block used to flush the buffer writer and close the file.
        */

        recordValues.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                   file[0] =new File(getExternalFilesDir("RecordedValueFolder"),"recorded.csv");//assigning the output file and it's directory
                   printWriter[0] =new PrintWriter(file[0]);//assigning file to the print writer object
                    for(int i=0; i<100; i++){ //loop has printwriter write out the 100 values
                        printWriter[0].println(String.format("%f,%f,%f",accel.prevOneHundred[i][0],accel.prevOneHundred[i][1],accel.prevOneHundred[i][2]));
                    }
                }
                catch (IOException e){//Catch case for no file
                    Log.v("Write Error","The file was no written to.");
                }
                finally {//Cleans printWriter buffer and closes file
                    Log.d("Goes to Finally", "Here");
                    if (printWriter[0] !=null){
                        printWriter[0].flush();
                        printWriter[0].close();
                    }
                }

            }
        });
        clearMax.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){//Clearmax function calls all the listeners to clear their max values
                rot.clearMaxValues();
                mag.clearMaxValues();
                accel.clearMaxValues();
            }
        });
    }
}


class LightSensorEventListener implements SensorEventListener{ //Light sensor listener for displaying the readings from the hardware light sensor
    TextView output;

    public LightSensorEventListener(TextView outputView){
        output=outputView;
    }

    public void onAccuracyChanged(Sensor s, int i){}


    public void onSensorChanged(SensorEvent se){
        if (se.sensor.getType()==Sensor.TYPE_LIGHT){

            String value=String.format("Light Sensor Value: \n(%.6g)",(se.values[0]));//Assigning the reading to the output textview
            output.setText(value);
        }
    }
}

