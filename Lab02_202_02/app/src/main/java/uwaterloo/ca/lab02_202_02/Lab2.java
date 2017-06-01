package uwaterloo.ca.lab02_202_02;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import ca.uwaterloo.sensortoy.LineGraphView;


public class Lab2 extends AppCompatActivity {
    LineGraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);


        LinearLayout r1=(LinearLayout) findViewById(R.id.layout1);//Creating linear Layout
        graph=new LineGraphView(getApplicationContext(), 100, Arrays.asList("x","y","z"));//Creating lineGraphView Object
        r1.addView(graph);//Adding the linegraph to the linear layout view
        graph.setVisibility(View.VISIBLE);
        r1.setOrientation(LinearLayout.VERTICAL);//Setting the linear layout as vertical orientation


        final File[] file = {null};//Creating a new file object
        final PrintWriter[] printWriter = {null};//Creating a printwriter for doc output

        /*
        Button recordValues= new Button(getApplicationContext());//Creating a button for recordValues
        recordValues.setText("Record the Data");
        r1.addView(recordValues);//adding the two buttons to the layout
        */

        TextView accelS=new TextView(getApplicationContext());//Creating the textviews for accelerometer
        r1.addView(accelS);
        accelS.setTextColor(Color.WHITE);

        //creates text view to show the gesture and assigns the characteristics such as colour text size and initial text
        TextView currentStateOuput= new TextView(getApplicationContext());
        r1.addView(currentStateOuput);
        currentStateOuput.setTextColor(Color.WHITE);
        currentStateOuput.setTextSize(75);
        currentStateOuput.setText("NO INPUT");

        //sets up accelerometer handler
        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);//Creating sensor manager
        Sensor accelSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final accelSensorEventListener accel=new accelSensorEventListener(accelS, graph, currentStateOuput);
        sensorManager.registerListener(accel,accelSensor, SensorManager.SENSOR_DELAY_GAME);

        //Assigns the accelerometer within the stateMachine class to be the accelerometer handler created above
        stateMachine.accel=accel;
    }
}
