package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;

import java.io.File;

public class AudioFile {
    // https://developer.android.com/reference/android/media/MediaPlayer.html
    MediaPlayer mp = new MediaPlayer();
    String file = null;
    boolean has_played_before = false;

    public AudioFile(String name)
    {
        file = name;
        try {
            mp.setDataSource(file);
        } catch (Exception e) {
            ;
        }
    }

    public AudioFile(String path, String name)
    {
        file = path + File.separatorChar + name;
        try {
            mp.setDataSource(file);
        } catch (Exception e) {
            ;
        }
    }

    public void play()
    {
        try {
            if (has_played_before) {
                mp.stop();
            }
            mp.prepare();
            mp.start();
            while (mp.isPlaying()) {
                ;
            }
            has_played_before = true;
        } catch (Exception e) {
            ;
        }
    }

    public void async_play()
    {
        try {
            if (has_played_before) {
                mp.stop();
            }
            mp.prepare();
            mp.start();
            has_played_before = true;
        } catch (Exception e) {
            ;
        }
    }

    public boolean isPlaying()
    {
        return mp.isPlaying();
    }
}
