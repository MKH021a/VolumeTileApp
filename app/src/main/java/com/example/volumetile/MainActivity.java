package com.example.volumetile;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.res.ColorStateList;
import android.os.Build;

public class MainActivity extends Activity {

    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private TextView percentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(48, 48, 48, 48);

        GradientDrawable background = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#111827"), Color.parseColor("#1E3A8A")}
        );
        rootLayout.setBackground(background);

        TextView titleText = new TextView(this);
        titleText.setText("Volume Control");
        titleText.setTextColor(Color.WHITE);
        titleText.setTextSize(28);
        titleText.setTypeface(Typeface.DEFAULT_BOLD);
        titleText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, 36);
        rootLayout.addView(titleText, titleParams);

        ImageView speakerIcon = new ImageView(this);
        speakerIcon.setImageResource(R.drawable.ic_volume_up);
        speakerIcon.setColorFilter(Color.WHITE);

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(120, 120);
        iconParams.setMargins(0, 0, 0, 28);
        rootLayout.addView(speakerIcon, iconParams);

        percentText = new TextView(this);
        percentText.setTextColor(Color.WHITE);
        percentText.setTextSize(22);
        percentText.setTypeface(Typeface.DEFAULT_BOLD);
        percentText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams percentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        percentParams.setMargins(0, 0, 0, 22);
        rootLayout.addView(percentText, percentParams);

        volumeSeekBar = new SeekBar(this);

        if (audioManager != null) {
            volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            volumeSeekBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#38BDF8")));
            volumeSeekBar.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            volumeSeekBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#334155")));
        }

        LinearLayout.LayoutParams seekParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        seekParams.setMargins(0, 0, 0, 36);
        rootLayout.addView(volumeSeekBar, seekParams);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        Button downButton = new Button(this);
        downButton.setText("-");

        Button upButton = new Button(this);
        upButton.setText("+");

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(160, 120);
        buttonParams.setMargins(16, 0, 16, 0);

        buttonLayout.addView(downButton, buttonParams);
        buttonLayout.addView(upButton, buttonParams);

        rootLayout.addView(buttonLayout);

        setContentView(rootLayout);

        updatePercentText();

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && audioManager != null) {
                    audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            progress,
                            AudioManager.FLAG_SHOW_UI
                    );
                    updatePercentText();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        downButton.setOnClickListener(view -> {
            if (audioManager != null) {
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND
                );
                refreshVolumeBar();
            }
        });

        upButton.setOnClickListener(view -> {
            if (audioManager != null) {
                audioManager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND
                );
                refreshVolumeBar();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshVolumeBar();
    }

    private void refreshVolumeBar() {
        if (audioManager != null && volumeSeekBar != null) {
            volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            updatePercentText();
        }
    }

    private void updatePercentText() {
        if (audioManager == null || volumeSeekBar == null || percentText == null) {
            return;
        }

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        int percent = 0;

        if (maxVolume > 0) {
            percent = Math.round((currentVolume * 100f) / maxVolume);
        }

        percentText.setText(percent + "%");
    }
}
