package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;

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
}
