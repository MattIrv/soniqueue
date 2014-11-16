package mattirv.soniqueue;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by Matthew on 11/15/14.
 */
public class Song {
    public String imageURL;
    public String songName;
    public String artist;
    public String album;
    public String queuedBy;
    public String id;
    public Bitmap image;

    public Song(String imageURL, String songName, String artist, String album, String queuedBy) {
        this.imageURL = imageURL;
        this.songName = songName;
        this.artist = artist;
        this.album = album;
        this.queuedBy = queuedBy;
    }

    public Song(String imageURL, String songName, String artist, String album, String queuedBy, String id) {
        this.imageURL = imageURL;
        this.songName = songName;
        this.artist = artist;
        this.album = album;
        this.queuedBy = queuedBy;
        this.id = id;
    }

    public Song() {}

    /**
     * ONLY USE THIS FROM NON-GUI THREADS OR IT WILL FAIL #SAFETYFIRST
     * @param url The url of the bitmap
     */
    public void setImage(String url) {
        this.imageURL = url;
        try {
            image = MakeRequest.downloadBitmap(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
