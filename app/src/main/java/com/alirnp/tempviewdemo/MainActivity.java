package com.alirnp.tempviewdemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alirnp.tempview.TempView;

public class MainActivity extends AppCompatActivity {

    TempView mTempView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTempView1 = findViewById(R.id.tempView1);
        TextView textView = findViewById(R.id.statusTempView1);


        // setIndicatorModeValues(mTempView1);


        mTempView1.setOnSeekCirclesListener(new TempView.OnSeekChangeListener() {
            @Override
            public void onSeekChange(int value) {
                textView.setText(String.format("onSeekChange : value = %s", value));
            }

            @Override
            public void onSeekComplete(int value) {
                textView.setText(String.format("onSeekComplete : value = %s", value));
            }
        });

    }

    private void setIndicatorModeValues(TempView mTempView) {
        mTempView.setMinValue(10);
        mTempView.setMaxValue(20);
        mTempView.setTemp(15);
    }
}
