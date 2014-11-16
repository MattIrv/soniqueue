package mattirv.soniqueue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.spotify.sdk.android.playback.Player;

public class MusicPlayer extends Service {

    static Player player;
    static MusicPlayer instance;
    static Song nowPlaying;

    public MusicPlayer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;
        player = MainMenu.player;
        nowPlaying = null;
        if (player == null) return 1;
        startup();
        return 0;
    }

    public static MusicPlayer getInstance() {
        return instance;
    }

    public void startup() {
        final MusicPlayer context = this;
        while(nowPlaying == null) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    while(MainMenu.partyID == -1) {
                        try { wait(5000); } catch (InterruptedException e) { }
                    }
                    MakeRequest.playNextSong(context, MainMenu.partyID);
                    try {
                        wait(5000);
                    }
                    catch (InterruptedException e) {

                    }
                }
            });
            thread.start();
        }
    }

    public void play(Song nowPlaying) {
        if (player == null) return;
        this.nowPlaying = nowPlaying;
        Log.d("MusicPlayer", "Trying to play a song...");
        player.play("spotify:track:40LQiUUUKXVGyNs09lHVjW");
        //player.play("spotify:track:" + nowPlaying.spotify_id);
    }
}
