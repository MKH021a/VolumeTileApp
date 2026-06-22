package com.example.volumetile;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.content.res.ColorStateList;
import android.os.Build;

public class MainActivity extends Activity {

    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private TextView percentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemBars();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        rootLayout.setPadding(48, 70, 48, 36);

        rootLayout.setBackground(createNightSkyBackground());

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
            volumeSeekBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#60A5FA")));
            volumeSeekBar.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            volumeSeekBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1E293B")));
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

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rootLayout.addView(buttonLayout, buttonLayoutParams);

        View spacer = new View(this);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
        );
        rootLayout.addView(spacer, spacerParams);

        TextView producedByText = new TextView(this);
        producedByText.setText("Produced by MKH");
        producedByText.setTextColor(Color.parseColor("#CBD5E1"));
        producedByText.setTextSize(15);
        producedByText.setGravity(Gravity.CENTER);
        producedByText.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        producedByText.setPadding(24, 18, 24, 18);
        producedByText.setClickable(true);

        GradientDrawable producedBackground = new GradientDrawable();
        producedBackground.setColor(Color.parseColor("#331E293B"));
        producedBackground.setCornerRadius(40);
        producedBackground.setStroke(1, Color.parseColor("#5577A7C7"));
        producedByText.setBackground(producedBackground);

        producedByText.setOnClickListener(view -> showMkhToast());

        LinearLayout.LayoutParams producedParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        producedParams.gravity = Gravity.CENTER_HORIZONTAL;
        rootLayout.addView(producedByText, producedParams);

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
        hideSystemBars();
        refreshVolumeBar();
    }

    private void hideSystemBars() {
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);

            WindowInsetsController controller = window.getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars());
                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    private Drawable createNightSkyBackground() {
        GradientDrawable baseGradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{
                        Color.parseColor("#020617"),
                        Color.parseColor("#0F172A"),
                        Color.parseColor("#1E1B4B")
                }
        );

        Drawable[] layers = new Drawable[]{
                baseGradient,
                createStarDrawable(20, 80, 5, "#E0F2FE"),
                createStarDrawable(90, 170, 3, "#FFFFFF"),
                createStarDrawable(160, 55, 4, "#BAE6FD"),
                createStarDrawable(250, 130, 3, "#F8FAFC"),
                createStarDrawable(330, 90, 5, "#DBEAFE"),
                createStarDrawable(420, 210, 3, "#FFFFFF"),
                createStarDrawable(520, 70, 4, "#E0F2FE"),
                createStarDrawable(650, 160, 3, "#F8FAFC"),
                createStarDrawable(760, 95, 5, "#FFFFFF"),
                createStarDrawable(120, 310, 3, "#DBEAFE"),
                createStarDrawable(300, 360, 4, "#FFFFFF"),
                createStarDrawable(580, 330, 3, "#BAE6FD"),
                createStarDrawable(810, 390, 4, "#E0F2FE")
        };

        LayerDrawable layerDrawable = new LayerDrawable(layers);

        layerDrawable.setLayerInset(1, 20, 80, 0, 0);
        layerDrawable.setLayerInset(2, 90, 170, 0, 0);
        layerDrawable.setLayerInset(3, 160, 55, 0, 0);
        layerDrawable.setLayerInset(4, 250, 130, 0, 0);
        layerDrawable.setLayerInset(5, 330, 90, 0, 0);
        layerDrawable.setLayerInset(6, 420, 210, 0, 0);
        layerDrawable.setLayerInset(7, 520, 70, 0, 0);
        layerDrawable.setLayerInset(8, 650, 160, 0, 0);
        layerDrawable.setLayerInset(9, 760, 95, 0, 0);
        layerDrawable.setLayerInset(10, 120, 310, 0, 0);
        layerDrawable.setLayerInset(11, 300, 360, 0, 0);
        layerDrawable.setLayerInset(12, 580, 330, 0, 0);
        layerDrawable.setLayerInset(13, 810, 390, 0, 0);

        return layerDrawable;
    }

    private Drawable createStarDrawable(int left, int top, int size, String color) {
        ShapeDrawable star = new ShapeDrawable(new OvalShape());
        star.setIntrinsicWidth(size);
        star.setIntrinsicHeight(size);
        star.getPaint().setColor(Color.parseColor(color));
        return star;
    }

    private void showMkhToast() {
        TextView toastText = new TextView(this);
        toastText.setText("MKH");
        toastText.setTextColor(Color.WHITE);
        toastText.setTextSize(22);
        toastText.setTypeface(Typeface.DEFAULT_BOLD);
        toastText.setGravity(Gravity.CENTER);
        toastText.setPadding(60, 36, 60, 36);

        GradientDrawable toastBackground = new GradientDrawable();
        toastBackground.setColor(Color.parseColor("#DD0F172A"));
        toastBackground.setCornerRadius(32);
        toastBackground.setStroke(2, Color.parseColor("#60A5FA"));
        toastText.setBackground(toastBackground);

        Toast toast = new Toast(getApplicationContext());
        toast.setView(toastText);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
