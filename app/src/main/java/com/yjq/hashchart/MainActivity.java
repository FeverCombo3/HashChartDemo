package com.yjq.hashchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    HashChartViewGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group = findViewById(R.id.hcv_group);

        Log.i("yjq","直接在onCreate中setData");
        group.setUnitAndData("zec",LinePointManager.getPoints("1h"));

        group.setOnDimensionChangeListener(new HashChartViewGroup.OnDimensionChangeListener() {
            @Override
            public void onDimensionChanged(String dimension) {
                group.setUnitAndData("zec",LinePointManager.getPoints(dimension));
            }
        });

        findViewById(R.id.set_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group.setUnitAndData("zec",LinePointManager.getPoints("1h"));
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //group.setUnitAndData("zec",LinePointManager.getPoints("1h"));

    }
}
