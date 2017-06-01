package uwaterloo.ca.lab02_202_02;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;
import ca.uwaterloo.sensortoy.LineGraphView;

public class accelSensorEventListener implements SensorEventListener {//Creates the class to handle the accelerometer and all values associated
    TextView output;//a text view to display current x,y,z values of the accelerometer
    LineGraphView graph;// creates a graph variable to be manipulated
    TextView currentStateOuput;//outputs the state to show

    //creates a 2 dimensional array to store the smmothed values
    private float[] smoothedValues = new float[3];

    public float[][] prevOneHundred= new float[100][3];//stores the previous 100 readings to be outputted to csv

    public accelSensorEventListener(TextView outputView, LineGraphView graphIn, TextView inputTextView){//constructor
        //assigns the parameters to variables within the objects class
        graph=graphIn;
        output=outputView;
        currentStateOuput=inputTextView;
    }
    public void onAccuracyChanged(Sensor s, int i){}// required function for the sensor event listener


    public void onSensorChanged(SensorEvent se){//function for when a reading of the acceleromter values change

        //smoothes values of the accelerometer
        smoothedValues[0] += (se.values[0]-smoothedValues[0])/5;
        smoothedValues[1] += (se.values[1]-smoothedValues[1])/5;
        smoothedValues[2] += (se.values[2]-smoothedValues[2])/5;

        graph.addPoint(smoothedValues);//adds current smoothed values to the graph

        //updates the previous 100 values to include the current values and to exclude the 101st values
        for (int j=0; j<3; j++){
            for (int i = 99; i>=0; i-- ){
                if (i > 0) {
                    prevOneHundred[i][j] = prevOneHundred[i - 1][j];
                } else {
                    prevOneHundred[0][j] = smoothedValues[j];
                }
            }
        }

        //checks current state and creates appropriate text for it
        switch (stateMachine.getCurrentState()) {

            case UP:
                currentStateOuput.setText("UP");
                break;

            case DOWN:
                currentStateOuput.setText("DOWN");
                break;

            case LEFT:
                currentStateOuput.setText("LEFT");
                break;

            case RIGHT:
                currentStateOuput.setText("RIGHT");
                break;

            default:
                break;
        }

        //looks to see how since last gesture and if to long replaces text
        if(stateMachine.waitingTime>100){
            currentStateOuput.setText("NO INPUT");
        }

        stateMachine.checkChange();//checks to see if the state should change

        //updates the counter to check if gesture happens within reason able time
        stateMachine.counter++;


    }
}
