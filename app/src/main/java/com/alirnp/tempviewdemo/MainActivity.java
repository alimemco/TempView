package com.alirnp.tempviewdemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alirnp.tempview.TempView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TempView tempView = findViewById(R.id.tempView);
        TextView textView = findViewById(R.id.statusTemp);

       // tempView.setMinValue(10);
       // tempView.setMaxValue(20);
       // tempView.setCurrentValue(15);
       // tempView.setTemp(10);

        tempView.setOnSeekCirclesListener(new TempView.OnSeekChangeListener() {
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
}
