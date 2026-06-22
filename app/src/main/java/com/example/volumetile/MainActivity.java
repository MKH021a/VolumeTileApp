package com.example.volumetile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.Gravity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText(
                "Volume Tile App\n\n" +
                "بعد از نصب برنامه، کنترل سنتر را پایین بکشید، وارد بخش Edit شوید و Tileهای Volume Up و Volume Down را اضافه کنید."
        );
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(40, 40, 40, 40);

        setContentView(textView);
    }
}
