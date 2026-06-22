package com.example.volumetile;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
        rootLayout.setGravity(Gravity.CENTER); // همه چیز وسط
        rootLayout.setPadding(60, 60, 60, 60);
        
        // پس‌زمینه گرادینت دارک شیک
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#0F172A"), Color.parseColor("#020617")});
        rootLayout.setBackground(gd);

        // آیکون
        speakerIcon = new ImageView(this);
        speakerIcon.setImageResource(R.drawable.ic_volume_up);
        speakerIcon.setColorFilter(Color.parseColor("#60A5FA"));
        rootLayout.addView(speakerIcon, new LinearLayout.LayoutParams(200, 200));

        // درصد
        percentText = new TextView(this);
        percentText.setTextColor(Color.WHITE);
        percentText.setTextSize(30);
        percentText.setPadding(0, 30, 0, 30);
        rootLayout.addView(percentText);

        // SeekBar
        volumeSeekBar = new SeekBar(this);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.getProgressDrawable().setColorFilter(Color.parseColor("#3B82F6"), PorterDuff.Mode.SRC_IN);
        volumeSeekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        LinearLayout.LayoutParams seekParams = new LinearLayout.LayoutParams(800, 100);
        rootLayout.addView(volumeSeekBar, seekParams);

        // دکمه‌های شیک
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setGravity(Gravity.CENTER);
        btnLayout.setPadding(0, 50, 0, 50);

        Button btnDown = createStyledButton("-");
        Button btnUp = createStyledButton("+");
        btnLayout.addView(btnDown); btnLayout.addView(btnUp);
        rootLayout.addView(btnLayout);

        // Footer
        TextView footer = new TextView(this);
        footer.setText("Produced by MKH");
        footer.setTextColor(Color.parseColor("#475569"));
        footer.setGravity(Gravity.CENTER);
        footer.setOnClickListener(v -> showMkhToast());
        rootLayout.addView(footer);

        setContentView(rootLayout);
        updateUI();

        // منطق دکمه‌ها
        btnDown.setOnClickListener(v -> adjustVolume(-1));
        btnUp.setOnClickListener(v -> adjustVolume(1));
        
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean f) { if(f) { audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p, 0); updateUI(); } }
            public void onStartTrackingTouch(SeekBar s) {} public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setTextColor(Color.WHITE);
        btn.setTextSize(25);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor("#1E293B"));
        btn.setBackground(shape);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
        params.setMargins(30, 0, 30, 0);
        btn.setLayoutParams(params);
        return btn;
    }

    private void adjustVolume(int delta) {
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current + delta, AudioManager.FLAG_SHOW_UI);
        volumeSeekBar.setProgress(current + delta);
        updateUI();
        playWaveAnimation();
    }

    private void playWaveAnimation() {
        ObjectAnimator.ofFloat(speakerIcon, "scaleX", 1f, 1.2f, 1f).setDuration(200).start();
        ObjectAnimator.ofFloat(speakerIcon, "scaleY", 1f, 1.2f, 1f).setDuration(200).start();
    }

    private void updateUI() {
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        percentText.setText((current * 100 / max) + "%");
    }

    private void showMkhToast() { Toast.makeText(this, "MKH", Toast.LENGTH_SHORT).show(); }

    private void hideSystemBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
