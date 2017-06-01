package lab1_202_02.uwaterloo.ca.inclass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

public class Main extends AppCompatActivity {
    LineGraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);
        // TextView tv= (TextView) findViewById(R.id.label1);
        ///tv.setText("I've been replaced");
        LinearLayout r1=(LinearLayout) findViewById(R.id.layout1);
        graph=new LineGraphView(getApplicationContext(), 100, Arrays.asList("x","y","z"));
        r1.addView(graph);
        graph.setVisibility(View.VISIBLE);
        Button recordValues= new Button(getApplicationContext());
        recordValues.setText("Press here to record the data");

        final Button clearMax = new Button(getApplicationContext());
        clearMax.setText("Press here to clear historical maximums");

        r1.addView(recordValues);
        r1.addView(clearMax);
        r1.setOrientation(LinearLayout.VERTICAL);
        TextView lightS=new TextView(getApplicationContext());
        r1.addView(lightS);

        TextView accelS=new TextView(getApplicationContext());
        r1.addView(accelS);
        TextView accelMaxs= new TextView(getApplicationContext());
        r1.addView(accelMaxs);

        TextView magS=new TextView(getApplicationContext());
        r1.addView(magS);
        TextView magMax=new TextView(getApplicationContext());
        r1.addView(magMax);

        TextView rotS=new TextView(getApplicationContext());
        r1.addView(rotS);
        TextView rotMax= new TextView((getApplicationContext()));
        r1.addView(rotMax);

        final File[] file = {null};
        final PrintWriter[] printWriter = {null};

        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener light=new LightSensorEventListener(lightS);
        sensorManager.registerListener(light,lightSensor,SensorManager.SENSOR_DELAY_GAME);
        ///SensorManager accelSensorMAnager=(SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor accelSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        final accelSensorEventListener accel=new accelSensorEventListener(accelS,graph, accelMaxs);
        sensorManager.registerListener(accel,accelSensor,SensorManager.SENSOR_DELAY_GAME);

        Sensor magSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        final magSensorEventListener mag=new magSensorEventListener(magS,magMax);
        sensorManager.registerListener(mag,magSensor,SensorManager.SENSOR_DELAY_GAME);

        Sensor rotSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        final rotSensorEventListener rot=new rotSensorEventListener(rotS,rotMax);
        sensorManager.registerListener(rot,rotSensor,SensorManager.SENSOR_DELAY_GAME);
        recordValues.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Log.d("onCLick","We got here");
                try {
                    file[0] =new File(getExternalFilesDir("RecordedValueFolder"),"recorded.csv");
                    printWriter[0] =new PrintWriter(file[0]);
                    //Log.d("File Path", String.valueOf(file));
                    for(int i=0; i<100; i++){
                        printWriter[0].println(String.format("%f,%f,%f",accel.prevOneHundred[i][0],accel.prevOneHundred[i][1],accel.prevOneHundred[i][2]));
                    }
                }
                catch (IOException e){
                    Log.v("Write Error","The file was no written to.");
                }
                finally {
                    Log.d("Goes to Finally", "Here");
                    if (printWriter[0] !=null){
                        printWriter[0].flush();
                        printWriter[0].close();
                    }
                }

            }
        });
        clearMax.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                rot.clearMaxValues();
                mag.clearMaxValues();
                accel.clearMaxValues();
            }
        });
    }
}
class LightSensorEventListener implements SensorEventListener{
    TextView output;
    public LightSensorEventListener(TextView outputView){
        output=outputView;
    }

    public void onAccuracyChanged(Sensor s, int i){}


    public void onSensorChanged(SensorEvent se){
        if (se.sensor.getType()==Sensor.TYPE_LIGHT){
            int round =(int) (se.values[0]*3);
            float rounded=((float)round)/3;
            String value=String.format("Light Sensor Value: \n%f", rounded);
            output.setText(value);
        }
    }
}
import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.widget.TextView;

public class rotSensorEventListener implements SensorEventListener {
    TextView output;
    TextView maxOutput;
    float[] maxValues= new float[3];
    public rotSensorEventListener(TextView outputView,TextView rotMax){
        output=outputView;
        maxOutput=rotMax;
    }
    public void clearMaxValues(){
        maxValues[0]=0;
        maxValues[1]=0;
        maxValues[2]=0;
    }
    public void onAccuracyChanged(Sensor s, int i){}


    public void onSensorChanged(SensorEvent se){
        if (se.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
            int round =(int) (se.values[0]*3);
            float rounded=((float)round)/3;
            String value=String.format("Rotation Vector Values: \n%f,%f,%f", rounded,se.values[1],se.values[2]);
            String maxS=String.format("The Highest Recorded Rotation Sensor Values Are \n %f,%f,%f :", maxValues[0],maxValues[1],maxValues[2]);
            maxOutput.setText(maxS);
            output.setText(value);
        }
        if (Math.abs(maxValues[0])<Math.abs(se.values[0])){
            maxValues[0]=se.values[0];
        }
        if (Math.abs(maxValues[1])<Math.abs(se.values[1])){
            maxValues[1]=se.values[1];
        }
        if (Math.abs(maxValues[2])<Math.abs(se.values[2])){
            maxValues[2]=se.values[2];
        }
    }
}
