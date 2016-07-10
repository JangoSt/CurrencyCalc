package hsulm.ulm.de.kompassapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ImageView mCompassView;
    SensorManager sman;
    TextView mInfo;
    float rotationZ;
    Sensor orientationSensor;
//    Sensor accelerometer;
//    Sensor magnetometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCompassView= (ImageView) findViewById(R.id.main_compass);
        mInfo= (TextView) findViewById(R.id.textView);

        sman = (SensorManager) getSystemService(SENSOR_SERVICE);
        //SENSOR_ORIENTATION is deprecated, using combination of magnetic and accelorator sensor
            List<Sensor> sensorList = sman.getSensorList(SensorManager.SENSOR_ORIENTATION);
        if (sensorList.size() == 0) {
            finish();
            return;
        }
        orientationSensor = sensorList.get(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //using slowest Sensor delay to save energy
        sman.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sman.unregisterListener(this);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

    float[] mValues;
    public void onSensorChanged(SensorEvent event) {
           mValues = event.values.clone();
            rotationZ = -mValues[0];
            mCompassView.setRotation(rotationZ);
            mInfo.setText(rotationZ +"");


    }
}
