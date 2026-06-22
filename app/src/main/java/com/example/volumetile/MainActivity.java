package com.example.volumetile;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

public class MainActivity extends Activity {
    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private TextView percentText;
    private ImageView speakerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemBars();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        rootLayout.setPadding(48, 70, 48, 36);
        rootLayout.setBackgroundColor(Color.parseColor("#020617")); // دارک ساده

        // عنوان
        TextView title = new TextView(this);
        title.setText("Volume Control");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        rootLayout.addView(title);

        // آیکون بلندگو
        speakerIcon = new ImageView(this);
        speakerIcon.setImageResource(R.drawable.ic_volume_up);
        speakerIcon.setColorFilter(Color.parseColor("#60A5FA")); // رنگ آبی اولیه
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(150, 150);
        iconParams.setMargins(0, 50, 0, 50);
        rootLayout.addView(speakerIcon, iconParams);

        // SeekBar با افکت Glow
        volumeSeekBar = new SeekBar(this);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        // افکت Glow با استفاده از رنگ روشن‌تر
        volumeSeekBar.getProgressDrawable().setColorFilter(Color.parseColor("#3B82F6"), PorterDuff.Mode.SRC_IN);
        volumeSeekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        rootLayout.addView(volumeSeekBar);

        percentText = new TextView(this);
        percentText.setTextColor(Color.WHITE);
        percentText.setTextSize(20);
        rootLayout.addView(percentText);

        // دکمه‌ها
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setGravity(Gravity.CENTER);
        btnLayout.setPadding(0, 50, 0, 0);
        
        Button btnDown = new Button(this); btnDown.setText("-");
        Button btnUp = new Button(this); btnUp.setText("+");
        btnLayout.addView(btnDown); btnLayout.addView(btnUp);
        rootLayout.addView(btnLayout);

        // تنظیمات Produced by MKH
        View spacer = new View(this);
        rootLayout.addView(spacer, new LinearLayout.LayoutParams(0, 0, 1f));
        TextView footer = new TextView(this);
        footer.setText("Produced by MKH");
        footer.setTextColor(Color.GRAY);
        footer.setOnClickListener(v -> showMkhToast());
        rootLayout.addView(footer);

        setContentView(rootLayout);
        updateUI();

        // عملیات صدا با انیمیشن
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean fromUser) {
                if(fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p, AudioManager.FLAG_SHOW_UI);
                    playWaveAnimation();
                    updateUI();
                }
            }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    private void playWaveAnimation() {
        // انیمیشن کوچک و بزرگ شدن آیکون (حس موج صدا)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(speakerIcon, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(speakerIcon, "scaleY", 1f, 1.2f, 1f);
        scaleX.setDuration(200);
        scaleY.setDuration(200);
        scaleX.start();
        scaleY.start();
    }

    private void updateUI() {
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        percentText.setText((current * 100 / max) + "%");
    }

    private void showMkhToast() {
        Toast toast = Toast.makeText(this, "MKH", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void hideSystemBars() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
