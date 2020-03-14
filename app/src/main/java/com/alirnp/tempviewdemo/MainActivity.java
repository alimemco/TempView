package com.alirnp.tempviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alirnp.tempview.TempView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TempView tempView = findViewById(R.id.tempView);
        TextView textView = findViewById(R.id.statusTemp);

        // tempView.setCurrentValue(0);

        tempView.setOnSeekCirclesListener(new TempView.OnSeekChangeListener() {
            @Override
            public void onSeekChange(int value) {
                textView.setText(String.valueOf(value));
            }

            @Override
            public void onSeekComplete(float value) {
                textView.setText("complete "+value);
            }
        });

    }
}
