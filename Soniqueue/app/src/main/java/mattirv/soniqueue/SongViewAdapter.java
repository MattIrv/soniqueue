package mattirv.soniqueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Matthew on 11/15/14.
 */
public class SongViewAdapter extends ArrayAdapter<Song> {

    private final Context context;
    private final List<Song> objects;
    private final boolean showQueuedBy;

    public SongViewAdapter(Context context, List<Song> objects, boolean showQueuedBy) {
        super(context, R.layout.song_list_view, objects);
        this.context = context;
        this.objects = objects;
        this.showQueuedBy = showQueuedBy;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.song_list_view, parent, false);
        ImageView albumArtView = (ImageView) rowView.findViewById(R.id.imageview_album_art);
        if (song.image != null) {
            albumArtView.setImageBitmap(song.image);
        }
        TextView songNameView = (TextView) rowView.findViewById(R.id.textview_song_name);
        songNameView.setText(song.songName);
        TextView artistNameView = (TextView) rowView.findViewById(R.id.textview_artist_name);
        artistNameView.setText(song.artist);
        TextView albumNameView = (TextView) rowView.findViewById(R.id.textview_album_name);
        albumNameView.setText(song.album);
        TextView queuedByView = (TextView) rowView.findViewById(R.id.textview_queued_by);
        if (showQueuedBy)
            queuedByView.setText("Queued by: " + song.queuedBy);
        else
            queuedByView.setText("");
        return rowView;
    }
}
