package mattirv.soniqueue;

/**
 * Created by Matthew on 11/15/14.
 */
public class Song {
    public String imageURL;
    public String songName;
    public String artist;
    public String album;
    public String queuedBy;

    public Song(String imageURL, String songName, String artist, String album, String queuedBy) {
        this.imageURL = imageURL;
        this.songName = songName;
        this.artist = artist;
        this.album = album;
        this.queuedBy = queuedBy;
    }
}
