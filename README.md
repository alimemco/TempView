# TempViewDemo
show temp in smart home


in XML :

Flexible Mode
```xml
    <com.alirnp.tempview.TempView
        android:id="@+id/tempView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:tv_color_background_progress="#92949C"
        app:tv_color_value="#1a8dff"
        app:tv_current_value="6"
        app:tv_is_indicator="false"
        app:tv_max_value="14"
        app:tv_min_value="-2"
        app:tv_stroke_width_background_progress="25dp" />
```
 
Indicator Mode
```xml 
    <com.alirnp.tempview.TempView
        android:id="@+id/tempView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tempView1"
        app:tv_color_background_progress="#92949C"
        app:tv_color_progress="#1a8dff"
        app:tv_color_value="#1a8dff"
        app:tv_current_value="6"
        app:tv_is_indicator="true"
        app:tv_max_value="14"
        app:tv_min_value="-2"
        app:tv_stroke_width_background_progress="25dp"
        app:tv_text_center_size="30sp"
        app:tv_text_color="#1a8dff"
        app:tv_text_status="Freezer Temp"
        app:tv_text_top_size="16sp" />  

``` 
        
in java :
```java
   TempView mTempView = findViewById(R.id.tempView);
   
   mTempView.setOnSeekCirclesListener(new TempView.OnSeekChangeListener() {
            @Override
            public void onSeekChange(int value) {
                textView.setText(String.format("value = %s", value));
            }

            @Override
            public void onSeekComplete(int value) {
                textView.setText(String.format("value = %s", value));
            }
        });
