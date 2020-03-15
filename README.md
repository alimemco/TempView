# TempViewDemo
show temp in smart home


in XML :
```xml
<com.alirnp.tempview.TempView
        android:id="@+id/tempView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:tv_color_background_progress="#92949C"
        app:tv_color_progress="#1a8dff"
        app:tv_color_value="#1a8dff"
        app:tv_current_value="2"
        app:tv_is_indicator="false"
        app:tv_max_value="8"
        app:tv_min_value="0"
        app:tv_stroke_width_background_progress="25dp"
        app:tv_text_color="#1a8dff" />
```
        
        

indicator Mode
```xml
 app:tv_is_indicator="true"
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
    ```    
        
        

