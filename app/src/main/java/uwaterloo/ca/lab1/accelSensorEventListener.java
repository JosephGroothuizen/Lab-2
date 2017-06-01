package uwaterloo.ca.lab1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import ca.uwaterloo.sensortoy.LineGraphView;

public class accelSensorEventListener implements SensorEventListener {//Creates the class to handle the accelerometer and all values associated
    TextView output;//a text view to display current x,y,z values of the accelerometer
    TextView Max;//a text viex to display he historical  maxs of the accelerometer
    LineGraphView graph;// creates a graph variable to be manipulated
    float[] maxValues= new float[3];//stores the historical max values
    public float[][] prevOneHundred= new float[100][3];//stores the previous 100 readings to be outputted to csv

    public accelSensorEventListener(TextView outputView, LineGraphView graphIn, TextView Maxs){//constructor
        //assigns the parameters to variables within the objects class
        graph=graphIn;
        output=outputView;
        Max=Maxs;
    }
    public void clearMaxValues(){//used to clear the max values
        maxValues[0]=0;
        maxValues[1]=0;
        maxValues[2]=0;
    }
    public void onAccuracyChanged(Sensor s, int i){}// required function for the sensor event listener


    public void onSensorChanged(SensorEvent se){//function for when a reading of the acceleromter values change
        graph.addPoint(se.values);//adds current values to the graph

        //checks if the current values are historical maxs and if so updating the maxs
        if (Math.abs(maxValues[0])<Math.abs(se.values[0])){
            maxValues[0]=se.values[0];
        }
        if (Math.abs(maxValues[1])<Math.abs(se.values[1])){
            maxValues[1]=se.values[1];
        }
        if (Math.abs(maxValues[2])<Math.abs(se.values[2])){
            maxValues[2]=se.values[2];
        }

        //updates the previous 100 values to include the current values and to exclude the 101st values
        for (int j=0; j<3; j++){
            for (int i = 99; i>=0; i-- ){
                if (i > 0) {
                    prevOneHundred[i][j] = prevOneHundred[i - 1][j];
                } else {
                    prevOneHundred[0][j] = se.values[j];
                }
            }
        }

        //updates the text views associated with the accelerometer
        if (se.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            String value=String.format("\n\nAccelerometer Sensor Values: \n(%.6g,%.6g,%.6g)",se.values[0],se.values[1],se.values[2]);
            String outputMax=String.format("The Highest Recorded Accelerometer Values Are: \n (%.6g,%.6g,%.6g) ", maxValues[0],maxValues[1],maxValues[2]);
            output.setText(value);
            Max.setText(outputMax);
        }

    }
}
