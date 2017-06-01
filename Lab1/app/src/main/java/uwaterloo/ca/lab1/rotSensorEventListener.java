package uwaterloo.ca.lab1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;


//My Rotational Sensor Event Listener, listens to the events from the sensor.
public class rotSensorEventListener implements SensorEventListener {
    //Declaring variables used in the class
    TextView output;
    TextView maxOutput;
    float[] maxValues= new float[3];

    //Constructor
    //Passing in TextViews where the readings will be displayed
    public rotSensorEventListener(TextView outputView,TextView rotMax){
        output=outputView;
        maxOutput=rotMax;
    }

    //Clearing all of the maximum rotational sensor readings
    public void clearMaxValues(){
        maxValues[0]=0;
        maxValues[1]=0;
        maxValues[2]=0;
    }

    //Accuracy change, but we really don't care about it
    public void onAccuracyChanged(Sensor s, int i){}

    //Reading changes detected from the sensor
    public void onSensorChanged(SensorEvent se){
        if (se.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
            //Setting up the Strings and formatting the decimal places
            String value=String.format("\n\nRotation Vector Values: \n(%.6g,%.6g,%.6g)", se.values[0],se.values[1],se.values[2]);
            String maxS=String.format("The Highest Recorded Rotation Sensor Values Are: \n (%.6g,%.6g,%.6g) \n\n", maxValues[0],maxValues[1],maxValues[2]);

            //Setting the values to the TextViews
            maxOutput.setText(maxS);
            output.setText(value);
        }

        //Following if statements below are used to find the maximum rotational sensor readings
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
