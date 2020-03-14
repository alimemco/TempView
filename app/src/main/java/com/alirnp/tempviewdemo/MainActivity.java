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

         //tempView.setCurrentValue(6);

        tempView.setOnSeekCirclesListener(new TempView.OnSeekChangeListener() {
            @Override
            public void onSeekChange(int value) {
                textView.setText(String.format("onSeekChange : value = %s",value));
            }

            @Override
            public void onSeekComplete(int value) {
                textView.setText(String.format("onSeekComplete : value = %s",value));
            }
        });

    }
}
