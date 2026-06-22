package com.example.volumetile;

import android.media.AudioManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class VolumeUpTileService extends TileService {

    @Override
    public void onStartListening() {
        super.onStartListening();

        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("Volume Up");
            tile.updateTile();
        }
    }

    @Override
    public void onClick() {
        super.onClick();

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        if (audioManager != null) {
            audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND
            );
        }
    }
}
