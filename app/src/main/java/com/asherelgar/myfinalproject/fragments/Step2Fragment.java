package com.asherelgar.myfinalproject.fragments;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asherelgar.myfinalproject.R;
import com.dd.CircularProgressButton;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Step2Fragment extends Fragment implements SensorEventListener {


    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;
    @BindView(R.id.tvSteps)
    TextView tvSteps;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvAverage)
    TextView tvAverage;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.circularButton1)
    CircularProgressButton circularButton1;
    Unbinder unbinder;
    @BindView(R.id.pieGraph)
    PieChart pieGraph;
    private Sensor mSensor;
    private int mSensorType;
    private long mShakeTime = 0;
    private long mRotationTime = 0;
    private SensorManager mSensorManager;

    public static Step2Fragment newInstance(int sensorType) {

        Step2Fragment f = new Step2Fragment();
        Bundle args = new Bundle();
        args.putInt("sensorType", sensorType);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mSensorType = args.getInt("sensorType");
        }
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(mSensorType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step2, container, false);
        unbinder = ButterKnife.bind(this, view);


        final CircularProgressButton circularButton1 = view.findViewById(R.id.circularButton1);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                } else {
                    circularButton1.setProgress(100);
                }
            }
        });


        pieGraph.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
        pieGraph.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
        pieGraph.addPieSlice(new PieModel("Work", 30, Color.parseColor("#CDA67F")));
        pieGraph.addPieSlice(new PieModel("Eating", 22, Color.parseColor("#FED70E")));

        pieGraph.startAnimation();

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
// If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        tvAverage.setText(
                "x = " + Float.toString(event.values[0]) + "\n" +
                        "y = " + Float.toString(event.values[1]) + "\n" +
                        "z = " + Float.toString(event.values[2]) + "\n"
        );

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            detectRotation(event);
        }
    }

    private void detectShake(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            // Change background color if gForce exceeds threshold;
            // otherwise, reset the color
            if (gForce > SHAKE_THRESHOLD) {
                pieGraph.setBackgroundColor(Color.rgb(0, 100, 0));
            } else {
                pieGraph.setBackgroundColor(Color.BLACK);
            }
        }
    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if (Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                pieGraph.setBackgroundColor(Color.rgb(0, 100, 0));
            } else {
                pieGraph.setBackgroundColor(Color.BLACK);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.circularButton1)
    public void onViewClicked() {
    }
}
